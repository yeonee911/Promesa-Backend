package com.promesa.promesa.domain.order.application;

import com.promesa.promesa.domain.cartItem.dao.CartItemRepository;
import com.promesa.promesa.domain.cartItem.domain.CartItem;
import com.promesa.promesa.domain.cartItem.exception.CartItemNotFoundException;
import com.promesa.promesa.domain.item.dao.ItemRepository;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.exception.ItemNotFoundException;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.order.dao.OrderRepository;
import com.promesa.promesa.domain.order.domain.Order;
import com.promesa.promesa.domain.order.domain.OrderItem;
import com.promesa.promesa.domain.order.domain.OrderStatus;
import com.promesa.promesa.domain.order.dto.OrderItemRequest;
import com.promesa.promesa.domain.order.dto.OrderRequest;
import com.promesa.promesa.domain.order.dto.OrderResponse;
import com.promesa.promesa.domain.order.exception.UnknownOrderTypeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest request, Member member) {

        Order order = new Order(member, OrderStatus.WAITING_FOR_PAYMENT);

        List<OrderItem> orderItems;

        if ("SINGLE".equalsIgnoreCase(request.type())) {
            orderItems = request.items().stream()
                    .map(req -> {
                        Item item = itemRepository.findById(req.itemId())
                                .orElseThrow(() -> ItemNotFoundException.EXCEPTION);
                        return OrderItem.of(order, item, req.quantity(), item.getPrice());
                    }).toList();

        } else if ("CART".equalsIgnoreCase(request.type())) {
            List<Long> cartItemIds = request.items().stream()
                    .map(OrderItemRequest::itemId)
                    .toList();

            List<CartItem> cartItems = cartRepository.findAllByIdInAndMember(cartItemIds, member);

            Set<Long> foundIds = cartItems.stream()
                    .map(CartItem::getId)
                    .collect(Collectors.toSet());

            List<Long> notFound = cartItemIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            if (!notFound.isEmpty()) {
                log.warn("존재하지 않는 cartItemId: {}", notFound);
                throw CartItemNotFoundException.EXCEPTION;
            }

            orderItems = cartItems.stream()
                    .map(cartItem -> OrderItem.of(order, cartItem.getItem(), cartItem.getQuantity(), cartItem.getItem().getPrice()))
                    .toList();

            cartRepository.deleteAll(cartItems);
        } else {
            throw UnknownOrderTypeException.EXCEPTION;
        }

        // 연관관계 설정
        orderItems.forEach(order::addOrderItem);

        orderRepository.save(order);
        return OrderResponse.of(order.getId(), order.getTotalPrice());
    }

}