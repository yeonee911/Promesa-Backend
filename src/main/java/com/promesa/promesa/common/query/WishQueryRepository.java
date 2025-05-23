package com.promesa.promesa.common.query;

import com.promesa.promesa.domain.home.dto.ItemPreviewResponse;
import com.promesa.promesa.domain.wish.domain.TargetType;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.promesa.promesa.domain.item.domain.QItem.item;

@Repository
@RequiredArgsConstructor
public class WishQueryRepository {
    private final JPAQueryFactory queryFactory;

    /**
     * 기획전 상품을 위시리스트 여부와 함께 반환
     * @param memberId
     * @return
     */
    public List<ItemPreviewResponse> findWithWish(Long memberId) {
        return queryFactory
                .select(Projections.constructor(ItemPreviewResponse.class,
                        item.id,
                        item.name,
                        item.description,
                        item.price,
                        item.thumnailUrl,
                        artist.name,
                        wish.id.isNotNull()  // join이 일어난 경우만 true
                ))
                .from(item)
                .join(artist).on(item.artistId.eq(artist.id))
                .leftJoin(wish)
                .on(
                        wish.memberId.eq(memberId), // 로그인한 사용자인지
                        wish.targetType.eq(TargetType.ITEM),    // 위시리스트 type이 상품인지
                        wish.targetId.eq(item.id)   // 해당 상품이 위시리스트인지
                )
                .fetch();
    }
}
