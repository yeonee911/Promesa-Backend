package com.promesa.promesa.domain.review.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.common.dto.s3.PresignedUrlRequest;
import com.promesa.promesa.common.dto.s3.PresignedUrlResponse;
import com.promesa.promesa.domain.item.dao.ItemRepository;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.exception.ItemNotFoundException;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.review.dao.ReviewRepository;
import com.promesa.promesa.domain.review.domain.Review;
import com.promesa.promesa.domain.review.domain.ReviewImage;
import com.promesa.promesa.domain.review.dto.request.AddReviewRequest;
import com.promesa.promesa.domain.review.dto.request.UpdateReviewRequest;
import com.promesa.promesa.domain.review.dto.response.ReviewResponse;
import com.promesa.promesa.domain.review.exception.ReviewAccessDeniedException;
import com.promesa.promesa.domain.review.exception.ReviewDuplicateException;
import com.promesa.promesa.domain.review.exception.ReviewItemMismatchException;
import com.promesa.promesa.domain.review.exception.ReviewNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final S3Service s3Service;
    private final ItemRepository itemRepository;
    private final ReviewRepository reviewRepository;

    private static final String BUCKET = "ceos-promesa";

    public List<PresignedUrlResponse> getPresignedPutUrls(PresignedUrlRequest request) {
        return s3Service.createPresignedPutUrl(
                BUCKET,
                request.imageType(),
                request.referenceId(),
                request.fileNames(),
                request.metadata()
        );
    }

    @Transactional
    public void deleteReviewImage(String key) {
        s3Service.deleteObject(BUCKET, key);
    }

    /**
     * 리뷰 작성하기
     * @param request
     * @return
     */
    @Transactional
    public ReviewResponse addReview(AddReviewRequest request, Long itemId, Member member) {
        // 상품 조회
        Item item = itemRepository.findById(itemId)
                .orElseThrow(()-> ItemNotFoundException.EXCEPTION);

        // 실제 주문 내역이 있는지 검증

        // 리뷰 중복 등록 검증
        if (reviewRepository.existsByItemIdAndMemberId(itemId, member.getId())) {
            throw ReviewDuplicateException.EXCEPTION;
        }

        // 리뷰 생성
        Review review = Review.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .item(item)
                .member(member)
                .build();

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

        // 상품 평점 업데이트 및 상품의 리뷰 개수 추가
        item.addReview(request.getRating());

        return ReviewResponse.from(savedReview, s3Service, BUCKET);
    }

    /**
     * 리뷰 삭제
     *
     * @param itemId
     * @param reviewId
     */
    @Transactional
    public void deleteReview(Long itemId, Long reviewId, Member member) {
        Review target = reviewRepository.findById(reviewId)
                .orElseThrow(() -> ReviewNotFoundException.EXCEPTION);

        if (!target.getItem().getId().equals(itemId)) {
            throw ReviewItemMismatchException.EXCEPTION;
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(()-> ItemNotFoundException.EXCEPTION);

        if (!target.getMember().getId().equals(member.getId())) {
            throw ReviewAccessDeniedException.EXCEPTION;
        }

        for (ReviewImage reviewImage : target.getReviewImages()) {  // 리뷰 이미지 삭제
            deleteReviewImage(reviewImage.getKey());
        }

        item.removeReview(target.getRating());  // 평점 삭제
        reviewRepository.delete(target);
    }

    /**
     * 리뷰 업데이트
     * @param itemId
     * @param reviewId
     * @param member
     * @return
     */
    public ReviewResponse updateReview(UpdateReviewRequest request, Long itemId, Long reviewId, Member member) {
        Review target = reviewRepository.findById(reviewId)
                .orElseThrow(() -> ReviewNotFoundException.EXCEPTION);

        if (!target.getItem().getId().equals(itemId)) {
            throw ReviewItemMismatchException.EXCEPTION;
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(()-> ItemNotFoundException.EXCEPTION);

        if (!target.getMember().getId().equals(member.getId())) {
            throw ReviewAccessDeniedException.EXCEPTION;
        }

        if (request.getContent() != null) {
            target.setContent(request.getContent());
        }

        if (request.getRating() != null) {
            item.removeReview(target.getRating());
            target.setRating(request.getRating());
            item.addReview(target.getRating());
        }

        if (request.getImageKeys() != null) {
            List<ReviewImage> images = request.getImageKeys().stream()
                    .map(key -> ReviewImage.builder()
                            .key(key)
                            .build())
                    .toList();

            target.setReviewImages(images);
        }

        return ReviewResponse.from(target, s3Service, BUCKET);
    }
}