package com.promesa.promesa.domain.cartItem.dao;

import com.promesa.promesa.domain.cartItem.domain.CartItem;
import com.promesa.promesa.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByIdInAndMember(List<Long> ids, Member member);
}
