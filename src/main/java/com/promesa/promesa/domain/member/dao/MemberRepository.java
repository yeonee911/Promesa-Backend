package com.promesa.promesa.domain.member.dao;

import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.member.domain.Role;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByProviderAndProviderId(String provider, String providerId);

    @Query("""
    SELECT m FROM Member m
    WHERE :admin NOT MEMBER OF m.roles
      AND :artist NOT MEMBER OF m.roles
      AND m.isDeleted = false
    """)
    List<Member> findAllExcludingAdminAndArtist(@Param("admin") Role admin, @Param("artist") Role artist);
}