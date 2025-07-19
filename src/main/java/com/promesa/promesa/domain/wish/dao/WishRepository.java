package com.promesa.promesa.domain.wish.dao;

import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.wish.domain.TargetType;
import com.promesa.promesa.domain.wish.domain.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    boolean existsByMemberAndTargetTypeAndTargetId(Member member, TargetType targetType, Long targetId);
    void deleteByMemberAndTargetTypeAndTargetId(Member member, TargetType targetType, Long targetId);
    List<Wish> findByMemberAndTargetType(Member member, TargetType targetType);
}
