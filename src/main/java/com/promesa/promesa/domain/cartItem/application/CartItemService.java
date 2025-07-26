package com.promesa.promesa.domain.cartItem.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.cartItem.dao.CartItemRepository;
import com.promesa.promesa.domain.cartItem.domain.CartItem;
import com.promesa.promesa.domain.cartItem.dto.CartItemAddRequest;
import com.promesa.promesa.domain.cartItem.dto.CartItemResponse;
import com.promesa.promesa.domain.cartItem.dto.CartItemUpdateRequest;
import com.promesa.promesa.domain.cartItem.exception.CartItemNotFoundException;
import com.promesa.promesa.domain.item.dao.ItemRepository;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.exception.ItemNotFoundException;
import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.member.exception.MemberNotFoundException;
import com.promesa.promesa.domain.order.exception.InvalidOrderQuantityException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public CartItemResponse addToCart(Member member, CartItemAddRequest request) {
        Member persistentMember = getPersistentMember(member);

        if (request.quantity() <= 0) throw InvalidOrderQuantityException.EXCEPTION;

        Item item = itemRepository.findById(request.itemId())
                .orElseThrow(() -> ItemNotFoundException.EXCEPTION);

        CartItem cartItem = cartItemRepository.findByMemberAndItem(persistentMember, item)
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + request.quantity());
        } else {
            cartItem = cartItemRepository.save(CartItem.builder()
                    .member(persistentMember)
                    .item(item)
                    .quantity(request.quantity())
                    .build());
        }
        return CartItemResponse.of(cartItem, s3Service, bucketName);
    }

    @Transactional
    public CartItemResponse updateCartItem(Member member, CartItemUpdateRequest request) {
        Member persistentMember = getPersistentMember(member);

        if (request.quantity() <= 0) throw InvalidOrderQuantityException.EXCEPTION;

        Item item = itemRepository.findById(request.itemId())
                .orElseThrow(() -> ItemNotFoundException.EXCEPTION);

        CartItem cartItem = cartItemRepository.findByMemberAndItem(persistentMember, item)
                .orElseThrow(()-> CartItemNotFoundException.EXCEPTION);

        cartItem.setQuantity(request.quantity());

        return CartItemResponse.of(cartItem, s3Service, bucketName);
    }

    @Transactional
    public CartItemResponse deleteCartItem(Member member, Long itemId) {
        Member persistentMember = getPersistentMember(member);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> ItemNotFoundException.EXCEPTION);

        CartItem cartItem = cartItemRepository.findByMemberAndItem(persistentMember, item)
                .orElseThrow(()-> CartItemNotFoundException.EXCEPTION);

        cartItemRepository.delete(cartItem);

        return CartItemResponse.of(cartItem, s3Service, bucketName);
    }

    public List<CartItemResponse> getCartItems(Member member) {
        Member persistentMember = getPersistentMember(member);

        List<CartItem> items = cartItemRepository.findByMember(persistentMember);

        return items.stream()
                .map(ci -> CartItemResponse.of(ci, s3Service, bucketName))
                .toList();
    }

    private Member getPersistentMember(Member member) {
        return memberRepository.findById(member.getId())
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

}
