package com.promesa.promesa.domain.order.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.cartItem.dao.CartItemRepository;
import com.promesa.promesa.domain.cartItem.domain.CartItem;
import com.promesa.promesa.domain.cartItem.exception.CartItemNotFoundException;
import com.promesa.promesa.domain.delivery.dao.DeliveryRepository;
import com.promesa.promesa.domain.delivery.domain.Delivery;
import com.promesa.promesa.domain.delivery.exception.DeliveryNotFoundException;
import com.promesa.promesa.domain.item.dao.ItemRepository;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.exception.ItemNotFoundException;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.order.dao.OrderRepository;
import com.promesa.promesa.domain.order.domain.Order;
import com.promesa.promesa.domain.order.domain.OrderItem;
import com.promesa.promesa.domain.order.domain.OrderStatus;
import com.promesa.promesa.domain.order.dto.OrderDetail;
import com.promesa.promesa.domain.order.dto.OrderItemRequest;
import com.promesa.promesa.domain.order.dto.OrderRequest;
import com.promesa.promesa.domain.order.dto.OrderResponse;
import com.promesa.promesa.domain.order.dto.OrderSummary;
import com.promesa.promesa.domain.order.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private final DeliveryRepository deliveryRepository;
    private final S3Service s3Service;

    @Value("${aws.s3.bucket}")
    private String bucketName;

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
            throw OrderNotFoundException.EXCEPTION;
        }

        // 연관관계 설정
        orderItems.forEach(order::addOrderItem);

        orderRepository.save(order);
        return OrderResponse.of(order.getId(), order.getTotalPrice());
    }

    public List<OrderSummary> getOrderSummaries(Member member) {
        List<Order> orders = orderRepository.findByMember(member);

        return orders.stream()
                .map(order -> {
                    Delivery delivery = deliveryRepository.findByOrder(order)
                            .orElseThrow(() -> DeliveryNotFoundException.EXCEPTION);

                    String thumbnailUrl = order.getOrderItems().get(0).getItem().getItemImages().stream()
                            .filter(img -> img.isThumbnail())
                            .map(img -> s3Service.createPresignedGetUrl(bucketName, img.getImageKey()))
                            .findFirst()
                            .orElse(null);

                    return OrderSummary.from(order, thumbnailUrl, delivery);
                })
                .toList();
    }

    public OrderDetail getOrderDetail(Member member, Long orderId) {
        Order order = orderRepository.findByIdAndMember(orderId, member)
                .orElseThrow(() -> OrderNotFoundException.EXCEPTION);

        Delivery delivery = deliveryRepository.findByOrder(order)
                .orElseThrow(() -> DeliveryNotFoundException.EXCEPTION);

        return OrderDetail.of(order, delivery, s3Service, bucketName);
    }

}