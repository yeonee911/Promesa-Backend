package com.promesa.promesa.domain.order.application;

import com.promesa.promesa.domain.cart.dao.CartRepository;
import com.promesa.promesa.domain.cart.domain.Cart;
import com.promesa.promesa.domain.cart.exception.CartNotFoundException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

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
            List<Long> cartIds = request.items().stream()
                    .map(OrderItemRequest::itemId)
                    .toList();

            List<Cart> cartItems = cartRepository.findAllByIdInAndMember(cartIds, member);

            if (cartItems.size() != cartIds.size()) {
                throw CartNotFoundException.EXCEPTION;
            }

            orderItems = cartItems.stream()
                    .map(cart -> OrderItem.of(order, cart.getItem(), cart.getQuantity(), cart.getItem().getPrice()))
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