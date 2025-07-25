package com.promesa.promesa.domain.cartItem.dao;

import com.promesa.promesa.domain.cartItem.domain.CartItem;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByIdInAndMember(List<Long> ids, Member member);
    Optional<CartItem> findByMemberAndItem(Member member, Item item);
    List<CartItem> findByMember(Member member);
}
