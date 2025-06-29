package com.promesa.promesa.domain.wish.domain;

import com.promesa.promesa.common.domain.BaseTimeEntity;
import com.promesa.promesa.domain.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wish extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "wish_id")
    private Long id;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(name = "target_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
