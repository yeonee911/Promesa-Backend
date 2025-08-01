package com.promesa.promesa.domain.order.dao;

import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.order.domain.Order;
import com.promesa.promesa.domain.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMemberOrderByCreatedAtDesc(Member member);
    Optional<Order> findByIdAndMember(Long id, Member member);

    List<Order> findByOrderStatus(OrderStatus orderStatus);
}

