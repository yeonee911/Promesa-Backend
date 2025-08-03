package com.promesa.promesa.domain.order.dao;

import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.order.domain.Order;
import com.promesa.promesa.domain.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMemberOrderByCreatedAtDesc(Member member);
    Optional<Order> findByIdAndMember(Long id, Member member);

    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.orderItems i JOIN FETCH i.item it WHERE (:status IS NULL OR o.orderStatus = :status)")
    List<Order> findWithItemsByStatus(@Param("status") OrderStatus orderStatus);
}

