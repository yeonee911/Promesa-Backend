package com.promesa.promesa.domain.order.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.cartItem.dao.CartItemRepository;
import com.promesa.promesa.domain.cartItem.domain.CartItem;
import com.promesa.promesa.domain.cartItem.exception.CartItemNotFoundException;
import com.promesa.promesa.domain.delivery.dao.DeliveryRepository;
import com.promesa.promesa.domain.delivery.domain.Delivery;
import com.promesa.promesa.domain.delivery.domain.DeliveryStatus;
import com.promesa.promesa.domain.delivery.exception.DeliveryNotFoundException;
import com.promesa.promesa.domain.item.dao.ItemRepository;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.exception.ItemNotFoundException;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.order.dao.OrderRepository;
import com.promesa.promesa.domain.order.domain.Order;
import com.promesa.promesa.domain.order.domain.OrderItem;
import com.promesa.promesa.domain.order.domain.OrderStatus;
import com.promesa.promesa.domain.order.dto.response.OrderResponse;
import com.promesa.promesa.domain.order.dto.request.OrderItemRequest;
import com.promesa.promesa.domain.order.dto.request.OrderRequest;
import com.promesa.promesa.domain.order.dto.response.OrderSummary;
import com.promesa.promesa.domain.order.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Value("${order.deposit.bank-name}")
    private String defaultBankName;
    @Value("${order.deposit.account-number}")
    private String defaultAccountNumber;
    @Value("${order.deposit.depositor-name}")
    private String defaultDepositorName;

    @Transactional
    public OrderResponse createOrder(OrderRequest request, Member member) {
        LocalDateTime now = LocalDateTime.now();
        List<OrderItem> orderItems;

        Order order = Order.builder()
                .member(member)
                .orderStatus(OrderStatus.WAITING_FOR_PAYMENT)
                .orderDate(now)
                .depositDeadline(now.plusDays(1))
                .bankName(defaultBankName)
                .accountNumber(defaultAccountNumber)
                .depositorName(defaultDepositorName)
                .build();

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

        // 총 금액 및 수량 계산
        int totalAmount = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
        int totalQuantity = orderItems.stream().mapToInt(OrderItem::getQuantity).sum();

        // 필드 수동 설정
        order.getOrderItems().clear();
        orderItems.forEach(order::addOrderItem);
        order.setTotalAmount(totalAmount);
        order.setTotalQuantity(totalQuantity);

        orderRepository.save(order);

        Delivery delivery = Delivery.builder()
                .order(order)
                .receiverName(request.receiverName())
                .receiverPhone(request.receiverPhone())
                .zipCode(request.zipCode())
                .address(request.address())
                .addressDetail(request.addressDetail())
                .deliveryStatus(DeliveryStatus.READY)
                .build();

        deliveryRepository.save(delivery);

        return OrderResponse.of(order, delivery, s3Service, bucketName);
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

    public OrderResponse getOrderDetail(Member member, Long orderId) {
        Order order = orderRepository.findByIdAndMember(orderId, member)
                .orElseThrow(() -> OrderNotFoundException.EXCEPTION);

        Delivery delivery = deliveryRepository.findByOrder(order)
                .orElseThrow(() -> DeliveryNotFoundException.EXCEPTION);

        return OrderResponse.of(order, delivery, s3Service, bucketName);
    }

}