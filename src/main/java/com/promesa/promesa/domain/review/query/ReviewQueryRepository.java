package com.promesa.promesa.domain.review.query;

import com.promesa.promesa.domain.review.dto.response.ReviewQueryDto;
import com.querydsl.core.ResultTransformer;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.promesa.promesa.domain.review.domain.QReview.review;
import static com.promesa.promesa.domain.review.domain.QReviewImage.reviewImage;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                .where(review.item.id.eq(itemId))
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
}
