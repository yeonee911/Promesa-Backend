package com.promesa.promesa.domain.wish.dto;

import com.promesa.promesa.domain.wish.domain.TargetType;
import lombok.Builder;

@Builder
public record WishToggleResponse(
        String message,      // "추가되었습니다" / "삭제되었습니다"
        boolean wished,      // true: 추가됨, false: 삭제됨
        Target target        // 대상 정보: 타입 + ID + count만
) {
    @Builder
    public record Target(
            TargetType targetType,
            Long targetId,
            int wishCount
    ) {}
}
