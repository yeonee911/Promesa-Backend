package com.promesa.promesa.domain.wish.dao;

import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.wish.domain.TargetType;
import com.promesa.promesa.domain.wish.domain.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {
    boolean existsByMemberAndTargetTypeAndTargetId(Member member, TargetType targetType, Long targetId);
}
