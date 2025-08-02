package com.promesa.promesa.domain.review.query;

import com.promesa.promesa.domain.delivery.domain.Delivery;
import com.promesa.promesa.domain.delivery.domain.DeliveryStatus;
import com.promesa.promesa.domain.order.domain.OrderStatus;
import com.promesa.promesa.domain.order.dto.response.OrderItemSummary;
import com.promesa.promesa.domain.review.dto.response.ReviewDetailResponse;
import com.promesa.promesa.domain.review.dto.response.ReviewQueryDto;
import com.promesa.promesa.domain.review.dto.response.ReviewResponse;
import com.querydsl.core.ResultTransformer;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.promesa.promesa.domain.delivery.domain.QDelivery.*;
import static com.promesa.promesa.domain.item.domain.QItemImage.itemImage;
import static com.promesa.promesa.domain.order.domain.QOrderItem.orderItem;
import static com.promesa.promesa.domain.order.domain.QOrder.order;
import static com.promesa.promesa.domain.review.domain.QReview.review;
import static com.promesa.promesa.domain.review.domain.QReviewImage.reviewImage;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepository {
    private final JPAQueryFactory queryFactory;

    private static final ResultTransformer<List<ReviewQueryDto>> REVIEW_TRANSFORMER =
            groupBy(review.id).list(
                    Projections.fields(ReviewQueryDto.class,
                            review.id.as("reviewId"),
                            review.content.as("content"),
                            review.item.id.as("itemId"),
                            review.orderItem.id.as("orderItemId"),
                            review.member.id.as("reviewerId"),
                            review.member.name.as("reviewerName"),
                            review.rating.as("rating"),
                            list(reviewImage.key).as("reviewImages"),
                            review.createdAt,
                            review.updatedAt
                    )
            );

    public Page<ReviewQueryDto> findAllReviews(Long itemId, Pageable pageable) {
        List<Long> reviewIds = queryFactory
                .select(review.id)
                .from(review)
                .where(
                        review.item.id.eq(itemId),
                        review.member.isDeleted.eq(false) // 탈퇴 회원 리뷰 제외
                )
                .orderBy(
                        review.rating.desc(),   // 리뷰 높은 순
                        Expressions.stringTemplate("char_length({0})", review.content).desc(),   // 글자 수 많은 순
                        review.createdAt.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (reviewIds.isEmpty()) {
            return Page.empty(pageable);
        }

        Map<Long, Integer> idOrderMap = new HashMap<>();
        for (int i = 0; i < reviewIds.size(); i++) {
            idOrderMap.put(reviewIds.get(i), i);    // 리뷰 아이디, 정렬된 순서
        }

        List<ReviewQueryDto> results = queryFactory
                .from(review)
                .leftJoin(review.member)
                .leftJoin(review.reviewImages, reviewImage)
                .where(review.id.in(reviewIds))
                .transform(REVIEW_TRANSFORMER);

        results.sort(Comparator.comparingInt(dto -> idOrderMap.get(dto.getReviewId())));        // 정렬 순서로 복원

        JPAQuery<Long> countQuery = queryFactory
                .select(review.count())
                .from(review)
                .where(review.item.id.eq(itemId));

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    /**
     * 작성한 리뷰 조회
     * @param memberId
     * @return
     */
    public List<ReviewDetailResponse> findMyReviews(Long memberId) {
        List<Long> reviewIds = queryFactory
                .select(review.id)
                .from(review)
                .where(review.member.id.eq(memberId))
                .orderBy(
                        review.createdAt.desc(),    // 리뷰 최신순
                        review.id.desc()
                )
                .fetch();

        if (reviewIds.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, Integer> idOrderMap = new HashMap<>();
        for (int i = 0; i < reviewIds.size(); i++) {
            idOrderMap.put(reviewIds.get(i), i);    // 리뷰 아이디, 정렬된 순서
        }

        List<ReviewDetailResponse> results = queryFactory
                .from(review)
                .join(review.orderItem, orderItem)
                .join(orderItem.order, order)
                .leftJoin(itemImage).on(
                        itemImage.item.eq(orderItem.item)
                                .and(itemImage.isThumbnail.eq(true))
                )
                .leftJoin(review.reviewImages, reviewImage)
                .where(review.id.in(reviewIds))
                .transform(
                        groupBy(review.id).list(
                                Projections.constructor(
                                        ReviewDetailResponse.class,
                                        // 첫 번째 인자: OrderItemSummary
                                        Projections.fields(OrderItemSummary.class,
                                                orderItem.order.id.as("orderId"),
                                                orderItem.id.as("orderItemId"),
                                                orderItem.item.id.as("itemId"),
                                                orderItem.item.name.as("itemName"),
                                                orderItem.item.artist.name.as("artistName"),
                                                itemImage.imageKey.as("itemThumbnail"),
                                                orderItem.order.orderDate.as("orderDate"),
                                                orderItem.order.orderStatus.as("orderStatus"),
                                                orderItem.quantity.as("quantity")
                                        ),
                                        // 두 번째 인자: ReviewResponse
                                        Projections.constructor(ReviewResponse.class,
                                                review.id,
                                                review.content,
                                                review.item.id,
                                                review.orderItem.id,
                                                review.member.id,
                                                review.member.name,
                                                review.rating,
                                                list(reviewImage.key), // reviewImages
                                                review.createdAt,
                                                review.updatedAt
                                        )
                                )
                        )
                );

        // groupBy는 정렬을 무시하므로 원래 순서로 정렬 복원
        results.sort(Comparator.comparingInt(r -> idOrderMap.get(r.getReviewResponse().getReviewId())));

        return results;
    }

    /**
     * 작성 가능한 리뷰 조회
     * @param memberId
     * @return
     */
    public List<OrderItemSummary> getMyEligibleReviews(Long memberId) {
        List<OrderItemSummary> results = queryFactory
                .select(Projections.fields(OrderItemSummary.class,
                        orderItem.order.id.as("orderId"),
                        orderItem.id.as("orderItemId"),
                        orderItem.item.id.as("itemId"),
                        orderItem.item.name.as("itemName"),
                        orderItem.item.artist.name.as("artistName"),
                        itemImage.imageKey.as("itemThumbnail"),
                        orderItem.order.orderDate.as("orderDate"),
                        delivery.deliveryStatus.as("deliveryStatus"),
                        orderItem.quantity.as("quantity")
                ))
                .from(orderItem)
                .join(orderItem.order, order)
                .join(order.delivery, delivery)
                .leftJoin(itemImage).on(
                        itemImage.item.eq(orderItem.item)
                                .and(itemImage.isThumbnail.eq(true))
                )
                .leftJoin(review).on(review.orderItem.id.eq(orderItem.id))
                .where(
                        order.member.id.eq(memberId),   // 현재 로그인한 회원
                        delivery.deliveryStatus.eq(DeliveryStatus.DELIVERED),    // 배송 완료된 주문만 조회
                        review.orderItem.isNull()
                )
                .orderBy(
                        order.orderDate.desc(), // 주문 최신순
                        orderItem.id.asc()  // 같은 주문 안에서는 상품 ID 오름차순
                )
                .fetch();

        return results;
    }
}
