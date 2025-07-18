package com.promesa.promesa.domain.cart.dao;

import com.promesa.promesa.domain.cart.domain.Cart;
import com.promesa.promesa.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByIdInAndMember(List<Long> ids, Member member);
}
