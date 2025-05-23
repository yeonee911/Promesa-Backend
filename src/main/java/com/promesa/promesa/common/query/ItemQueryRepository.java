package com.promesa.promesa.common.query;

import com.promesa.promesa.domain.home.dto.ItemPreviewResponse;
import com.promesa.promesa.domain.wish.domain.TargetType;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.promesa.promesa.domain.artist.domain.QArtist.artist;
import static com.promesa.promesa.domain.image.domain.QItemImage.itemImage;
import static com.promesa.promesa.domain.item.domain.QExhibitionItem.exhibitionItem;
import static com.promesa.promesa.domain.item.domain.QItem.item;
import static com.promesa.promesa.domain.wish.domain.QWish.wish;

@Repository
@RequiredArgsConstructor
public class ItemQueryRepository {
    private final JPAQueryFactory queryFactory;

    /**
     * 기획전 상품을 위시리스트 여부와 함께 반환
     * @param memberId
     * @return
     */
    public List<ItemPreviewResponse> findExhibitionItem(Long memberId, Long exhibitionId) {
        return queryFactory
                .select(Projections.constructor(ItemPreviewResponse.class,
                        item.id,
                        item.name,
                        item.description,
                        item.price,
                        itemImage.imageUrl,
                        artist.name,
                        wish.id.isNotNull()
                ))
                .from(exhibitionItem)
                .join(exhibitionItem.item, item)
                .join(item.artist, artist)
                .leftJoin(item.itemImages, itemImage)
                    .on(itemImage.isThumbnail.isTrue())
                .leftJoin(wish)
                    .on(
                            wish.member.id.eq(memberId), // 로그인한 사용자인지
                            wish.targetType.eq(TargetType.ITEM),    // 위시리스트 type이 상품인지
                            wish.targetId.eq(item.id)   // 해당 상품이 위시리스트인지
                    )
                .where(exhibitionItem.exhibition.id.eq(exhibitionId))
                .fetch();
    }
}
