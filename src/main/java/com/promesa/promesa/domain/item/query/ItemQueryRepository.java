    package com.promesa.promesa.domain.item.query;

    import com.promesa.promesa.domain.artist.domain.QArtist;
    import com.promesa.promesa.domain.home.dto.response.ItemPreviewResponse;
    import com.promesa.promesa.domain.item.domain.Item;
    import com.promesa.promesa.domain.item.domain.QItem;
    import com.promesa.promesa.domain.item.domain.QItemImage;
    import com.promesa.promesa.domain.item.domain.SaleStatus;
    import com.promesa.promesa.domain.member.domain.Member;
    import com.promesa.promesa.domain.wish.domain.TargetType;
    import com.querydsl.core.types.*;
    import com.querydsl.core.types.dsl.BooleanExpression;
    import com.querydsl.core.types.dsl.Expressions;
    import com.querydsl.core.types.dsl.PathBuilder;
    import com.querydsl.jpa.impl.JPAQuery;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import com.querydsl.jpa.impl.JPAQueryFactory;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.Sort;
    import org.springframework.data.support.PageableExecutionUtils;
    import org.springframework.stereotype.Repository;

    import java.util.ArrayList;
    import java.util.List;

    import static com.promesa.promesa.domain.artist.domain.QArtist.artist;
    import static com.promesa.promesa.domain.exhibition.domain.QExhibitionItem.exhibitionItem;
    import static com.promesa.promesa.domain.item.domain.QItem.item;
    import static com.promesa.promesa.domain.item.domain.QItemImage.itemImage;
    import static com.promesa.promesa.domain.itemCategory.domain.QItemCategory.itemCategory;
    import static com.promesa.promesa.domain.wish.domain.QWish.wish;

    @Repository
    @RequiredArgsConstructor
    public class ItemQueryRepository {
        private final JPAQueryFactory queryFactory;

        /**
         * 기획전 상품을 위시리스트 여부와 함께 반환
         * @param member
         * @return
         */
        public List<ItemPreviewResponse> findExhibitionItem(Member member, Long exhibitionId) {
            Expression<Boolean> isWished = isWishedExpression(member);

            JPAQuery<ItemPreviewResponse> query = queryFactory
                    .select(Projections.fields(ItemPreviewResponse.class,
                            item.id.as("itemId"),
                            item.saleStatus.as("saleStatus"),
                            item.name.as("itemName"),
                            item.price,
                            itemImage.imageKey.as("imageUrl"),
                            artist.name.as("artistName"),
                            ExpressionUtils.as(isWished, "isWished"),
                            item.wishCount.as("wishCount")
                    ))
                    .from(exhibitionItem)
                    .join(exhibitionItem.item, item)
                    .join(item.artist, artist)
                    .leftJoin(item.itemImages, itemImage).on(itemImage.isThumbnail.isTrue())
                    .where(exhibitionItem.exhibition.id.eq(exhibitionId));

            if (member != null) {
                query.leftJoin(wish).on(
                        wish.member.id.eq(member.getId())
                                .and(wish.targetType.eq(TargetType.ITEM))
                                .and(wish.targetId.eq(item.id))
                );
            }
            return query.fetch();
        }

        /**
         * 카테고리 작품 조회
         * @param member
         * @param categoryId
         * @return
         */
        public Page<ItemPreviewResponse> findCategoryItem(Member member, Long categoryId, Pageable pageable) {
            Expression<Boolean> isWished = isWishedExpression(member);
            BooleanExpression categoryCondition = (categoryId != 0) ? itemCategory.category.id.eq(categoryId) : null;
            BooleanExpression saleStatusCondition = item.saleStatus.ne(SaleStatus.STOPPED);

            JPAQuery<ItemPreviewResponse> query = queryFactory
                    .select(Projections.fields(ItemPreviewResponse.class,
                            item.id.as("itemId"),
                            item.saleStatus.as("saleStatus"),
                            item.name.as("itemName"),
                            item.price,
                            itemImage.imageKey.as("imageUrl"),
                            artist.name.as("artistName"),
                            ExpressionUtils.as(isWished, "isWished"),
                            item.wishCount.as("wishCount")
                    ));
            if (categoryId != 0) {
                query.from(itemCategory)
                        .join(itemCategory.item, item)
                        .join(item.artist, artist)
                        .leftJoin(item.itemImages, itemImage).on(itemImage.isThumbnail.isTrue())
                        .where(categoryCondition, saleStatusCondition);
            } else {
                query.from(item)
                        .join(item.artist, artist)
                        .leftJoin(item.itemImages, itemImage).on(itemImage.isThumbnail.isTrue())
                        .where(saleStatusCondition);
            }

            if (!pageable.getSort().isEmpty()) {
                List<OrderSpecifier<?>> specifiers = new ArrayList<>(List.of(createOrderSpecifiers(pageable.getSort())));
                specifiers.add(item.id.desc());  // 보조 정렬 기준
                query.orderBy(specifiers.toArray(new OrderSpecifier[0]));
            }

            if (member != null) {
                query.leftJoin(wish).on(
                        wish.member.id.eq(member.getId())
                                .and(wish.targetType.eq(TargetType.ITEM))
                                .and(wish.targetId.eq(item.id))
                );
            }

            query.offset(pageable.getOffset())
                    .limit(pageable.getPageSize());
            List<ItemPreviewResponse> content = query.fetch();

            JPAQuery<Long> countQuery;

            if (categoryId != 0) {
                countQuery = queryFactory
                        .select(item.count())
                        .from(itemCategory)
                        .join(itemCategory.item, item)
                        .where(categoryCondition, saleStatusCondition);
            } else {
                countQuery = queryFactory
                        .select(item.count())
                        .from(item)
                        .where(saleStatusCondition);
            }

            return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
        }

        /**
         * 작가의 카테고리별 작품 조회
         * @param member
         * @param artistId
         * @param categoryId
         * @return
         */
        public List<ItemPreviewResponse> findItemsByArtistAndCategory(
                Member member,
                Long artistId,
                Long categoryId,
                Pageable pageable
        ) {
            Expression<Boolean> isWished = isWishedExpression(member);
            BooleanExpression categoryCondition = (categoryId != 0) ? itemCategory.category.id.eq(categoryId) : null;
            BooleanExpression artistCondition = artist.id.eq(artistId);
            BooleanExpression saleStatusCondition = item.saleStatus.ne(SaleStatus.STOPPED);

            JPAQuery<ItemPreviewResponse> query = queryFactory
                    .select(Projections.fields(ItemPreviewResponse.class,
                            item.id.as("itemId"),
                            item.saleStatus.as("saleStatus"),
                            item.name.as("itemName"),
                            item.price,
                            itemImage.imageKey.as("imageUrl"),
                            artist.name.as("artistName"),
                            ExpressionUtils.as(isWished, "isWished"),
                            item.wishCount.as("wishCount")
                    ))
                    .from(itemCategory)
                    .join(itemCategory.item, item)
                    .join(item.artist, artist)
                    .leftJoin(item.itemImages, itemImage).on(itemImage.isThumbnail.isTrue())
                    .where(artistCondition, categoryCondition, saleStatusCondition);

            if (!pageable.getSort().isEmpty()) {
                List<OrderSpecifier<?>> specifiers = new ArrayList<>(List.of(createOrderSpecifiers(pageable.getSort())));
                specifiers.add(item.id.desc());  // 보조 정렬 기준
                query.orderBy(specifiers.toArray(new OrderSpecifier[0]));
            }

            if (member != null) {
                query.leftJoin(wish).on(
                        wish.member.id.eq(member.getId())
                                .and(wish.targetType.eq(TargetType.ITEM))
                                .and(wish.targetId.eq(item.id))
                );
            }

            return query.fetch();
        }

        private OrderSpecifier<?>[] createOrderSpecifiers(Sort sort) {
            List<OrderSpecifier<?>> specifiers = new ArrayList<>();

            for (Sort.Order order : sort) {
                String property = order.getProperty();  // 정렬 조건
                Order dicrection = order.isAscending() ? Order.ASC : Order.DESC;    // 오름차순, 내림차순
                PathBuilder path = new PathBuilder(Item.class, "item");

                specifiers.add(new OrderSpecifier(dicrection, path.get(property))); // 복합 정렬
            }

            return specifiers.toArray(new OrderSpecifier[0]);   // List를 Array로 변환
        }

        /**
         * member가 null이면 false 아니면 wish.id 존재 여부 반환
         */
        private Expression<Boolean> isWishedExpression(Member member) {
            if (member == null) {
                return Expressions.asBoolean(false);
            }

            return wish.id.isNotNull();
        }

        /**
         * 작품 이름 검색
         * @param keyword
         * @param member
         * @return
         */
        public List<ItemPreviewResponse> searchByItemName(String keyword, Member member) {
            Expression<Boolean> isWished = isWishedExpression(member);

            JPAQuery<ItemPreviewResponse> query = queryFactory
                    .select(Projections.fields(ItemPreviewResponse.class,
                            item.id.as("itemId"),
                            item.saleStatus.as("saleStatus"),
                            item.name.as("itemName"),
                            item.price,
                            itemImage.imageKey.as("imageUrl"),
                            artist.name.as("artistName"),
                            ExpressionUtils.as(isWished, "isWished"),
                            item.wishCount.as("wishCount")
                    ))
                    .from(item)
                    .join(item.artist, artist)
                    .leftJoin(item.itemImages, itemImage).on(itemImage.isThumbnail.isTrue())
                    .where(
                            Expressions.stringTemplate("REPLACE({0}, ' ', '')", item.name)
                                    .containsIgnoreCase(keyword)
                    );

            if (member != null) {
                query.leftJoin(wish).on(
                        wish.member.id.eq(member.getId())
                                .and(wish.targetType.eq(TargetType.ITEM))
                                .and(wish.targetId.eq(item.id))
                );
            }

            return query.fetch();
        }
    }
