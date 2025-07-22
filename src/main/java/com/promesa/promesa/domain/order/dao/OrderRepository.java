package com.promesa.promesa.domain.order.dao;

import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMember(Member member);
    Optional<Order> findByIdAndMember(Long id, Member member);
}

