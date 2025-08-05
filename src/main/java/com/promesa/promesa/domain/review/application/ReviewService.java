package com.promesa.promesa.domain.review.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.delivery.domain.DeliveryStatus;
import com.promesa.promesa.domain.item.dao.ItemRepository;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.exception.ItemNotFoundException;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.order.dao.OrderItemRepository;
import com.promesa.promesa.domain.order.dao.OrderRepository;
import com.promesa.promesa.domain.order.domain.OrderItem;
import com.promesa.promesa.domain.order.domain.OrderStatus;
import com.promesa.promesa.domain.order.dto.response.OrderItemSummary;
import com.promesa.promesa.domain.order.exception.OrderItemNotFoundException;
import com.promesa.promesa.domain.review.dao.ReviewRepository;
import com.promesa.promesa.domain.review.domain.Review;
import com.promesa.promesa.domain.review.domain.ReviewImage;
import com.promesa.promesa.domain.review.dto.request.AddReviewRequest;
import com.promesa.promesa.domain.review.dto.request.UpdateReviewRequest;
import com.promesa.promesa.domain.review.dto.response.*;
import com.promesa.promesa.domain.review.exception.*;
import com.promesa.promesa.domain.review.query.ReviewQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ItemRepository itemRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageService reviewImageService;
    private final ReviewQueryRepository reviewQueryRepository;
    private final S3Service s3Service;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    @Value("${aws.s3.bucket}")  // application.yml 에 정의 필요
    private String bucketName;

    /**
     * 리뷰 작성하기
     * @param request
     * @return
     */
    @Transactional
    public ReviewResponse addReview(AddReviewRequest request, Long itemId, Member member) {
        if (member == null) {
            throw ReviewUnauthorizedException.EXCEPTION;
        }

        // 상품 조회
        Item item = itemRepository.findById(itemId)
                .orElseThrow(()-> ItemNotFoundException.EXCEPTION);

        // 실제 주문 내역이 있는지 검증
        OrderItem orderItem = orderItemRepository.findById(request.getOrderItemId())
                .orElseThrow(() -> OrderItemNotFoundException.EXCEPTION);

        if (!orderItem.getItem().getId().equals(itemId) ||
                !orderItem.getOrder().getMember().getId().equals(member.getId())
        ) {
            throw ReviewOrderItemForbiddenException.EXCEPTION;
        }

        // 아직 배송되지 않은 상품인지 검증
        if (orderItem.getOrder().getDelivery().getDeliveryStatus() != DeliveryStatus.DELIVERED) {
            throw ReviewItemNotDeliveredException.EXCEPTION;
        }

        // 주문 상품에 이미 리뷰를 작성했는지 검증
        if (reviewRepository.existsByOrderItem(orderItem)) {
            throw ReviewDuplicateException.EXCEPTION;
        }

        // 리뷰 생성
        Review review = Review.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .member(member)
                .orderItem(orderItem)
                .build();

        // 상품 평점 업데이트 및 상품의 리뷰 개수 추가
        item.addReview(review, request.getRating());

        // DB에 저장
        Review savedReview = reviewRepository.save(review);

        // 리뷰 이미지 추가
        if (request.getImageKeys() != null) {
            List<ReviewImage> images = request.getImageKeys().stream()
                    .map(key -> ReviewImage.builder()
                            .key(key)
                            .build())
                    .toList();

            review.setReviewImages(images);
        }

        return ReviewResponse.from(savedReview, extractImageResponses(savedReview));
    }

    /**
     * 리뷰 삭제
     * @param itemId
     * @param reviewId
     */
    @Transactional
    public String deleteReview(Long itemId, Long reviewId, Member member) {
        if (member == null) {
            throw ReviewUnauthorizedException.EXCEPTION;
        }

        Review target = getVerifiedReview(itemId, reviewId, member);
        Item item = target.getItem();

        for (ReviewImage reviewImage : target.getReviewImages()) {  // 리뷰 이미지 삭제
            reviewImageService.deleteReviewImage(reviewImage.getKey());
        }

        item.removeReview(target.getRating());  // 평점 삭제
        reviewRepository.delete(target);

        return "성공적으로 삭제되었습니다.";
    }

    /**
     * 리뷰 업데이트
     * @param itemId
     * @param reviewId
     * @param member
     * @return
     */
    @Transactional
    public ReviewResponse updateReview(UpdateReviewRequest request, Long itemId, Long reviewId, Member member) {
        if (member == null) {
            throw ReviewUnauthorizedException.EXCEPTION;
        }

        Review target = getVerifiedReview(itemId, reviewId, member);
        Item item = target.getItem();

        if (request.getContent() != null) {
            target.setContent(request.getContent());
        }

        if (request.getRating() != null) {
            item.updateReviewRating(target.getRating(), request.getRating());
            target.setRating(request.getRating());
        }

        if (request.getImageKeys() != null) {
            List<ReviewImage> images = request.getImageKeys().stream()
                    .map(key -> ReviewImage.builder()
                            .key(key)
                            .build())
                    .toList();

            target.setReviewImages(images);
        }

        return ReviewResponse.from(target, extractImageResponses(target));
    }

    /**
     * 리뷰 조회하기
     * @param itemId
     * @return
     */
    public Page<ReviewResponse> getReviews(Long itemId, Pageable pageable) {
        Page<ReviewQueryDto> results = reviewQueryRepository.findAllReviews(itemId, pageable);
        List<ReviewResponse> responseList = results.getContent().stream()
                .map(dto -> {
                    List<ReviewImageResponse> imageResponses = dto.getReviewImages().stream()
                            .map(key -> ReviewImageResponse.builder()
                                    .key(key)
                                    .url(s3Service.createPresignedGetUrl(bucketName, key))
                                    .build())
                            .toList();

                    return ReviewResponse.from(dto, imageResponses);
                })
                .toList();

        return PageableExecutionUtils.getPage(
                responseList,
                pageable,
                results::getTotalElements
        );
    }

    public Review getVerifiedReview(Long itemId, Long reviewId, Member member) {
        Review target = reviewRepository.findById(reviewId)
                .orElseThrow(() -> ReviewNotFoundException.EXCEPTION);

        if (!target.getItem().getId().equals(itemId)) {
            throw ReviewItemMismatchException.EXCEPTION;
        }

        if (!target.getMember().getId().equals(member.getId())) {
            throw ReviewAccessDeniedException.EXCEPTION;
        }

        return target;
    }

    private List<ReviewImageResponse> extractImageResponses(Review review) {
        return Optional.ofNullable(review.getReviewImages())
                .orElse(List.of())
                .stream()
                .map(ReviewImage::getKey)
                .map(key -> ReviewImageResponse.builder()
                        .key(key)
                        .url(s3Service.createPresignedGetUrl(bucketName, key))
                        .build())
                .toList();
    }

    /**
     * 내가 작성한 리뷰 조회
     * @param member 작성자
     * @return
     */
    public List<ReviewDetailResponse> getMyReviews(Member member) {
        List<ReviewDetailQueryDto> rawResults = reviewQueryRepository.findMyReviews(member.getId());

        return rawResults.stream()
                .map(r -> {
                    OrderItemSummary summary = r.getOrderItemSummary();
                    String thumbnailKey = summary.getItemThumbnail();
                    if (thumbnailKey != null) {
                        summary.setItemThumbnail(s3Service.createPresignedGetUrl(bucketName, thumbnailKey));
                    }

                    List<ReviewImageResponse> images = r.getReview().getReviewImages().stream()
                            .map(key -> ReviewImageResponse.builder()
                                    .key(key)
                                    .url(s3Service.createPresignedGetUrl(bucketName, key))
                                    .build())
                            .toList();

                    ReviewResponse reviewResponse = ReviewResponse.from(r.getReview(), images);

                    return ReviewDetailResponse.of(summary, reviewResponse);
                }).toList();
    }

    /**
     * 작성 가능한 리뷰 조회
     * @param member
     * @return
     */
    public List<OrderItemSummary> getMyEligibleReviews(Member member) {
        List<OrderItemSummary> results = reviewQueryRepository.getMyEligibleReviews(member.getId());
        results.forEach(r -> {
            String imageKey = r.getItemThumbnail();
            if (imageKey != null) {
                String imageUrl = s3Service.createPresignedGetUrl(bucketName, imageKey);
                r.setItemThumbnail(imageUrl);
            }
        });
        return results;
    }
}