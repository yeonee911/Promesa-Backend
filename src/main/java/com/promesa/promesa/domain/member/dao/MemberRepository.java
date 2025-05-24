package com.promesa.promesa.domain.member.dao;

import com.promesa.promesa.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
