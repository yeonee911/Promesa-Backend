package com.promesa.promesa.domain.order.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.cartItem.dao.CartItemRepository;
import com.promesa.promesa.domain.cartItem.domain.CartItem;
import com.promesa.promesa.domain.cartItem.exception.CartItemNotFoundException;
import com.promesa.promesa.domain.delivery.domain.Delivery;
import com.promesa.promesa.domain.delivery.domain.DeliveryStatus;
import com.promesa.promesa.domain.delivery.exception.DeliveryNotFoundException;
import com.promesa.promesa.domain.item.dao.ItemRepository;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.exception.ItemNotFoundException;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.order.dao.OrderItemRepository;
import com.promesa.promesa.domain.order.dao.OrderRepository;
import com.promesa.promesa.domain.order.domain.Order;
import com.promesa.promesa.domain.order.domain.OrderItem;
import com.promesa.promesa.domain.order.domain.OrderItemStatus;
import com.promesa.promesa.domain.order.domain.OrderStatus;
import com.promesa.promesa.domain.order.dto.request.PaymentRequest;
import com.promesa.promesa.domain.order.dto.response.OrderItemDetail;
import com.promesa.promesa.domain.order.dto.response.OrderResponse;
import com.promesa.promesa.domain.order.dto.request.OrderItemRequest;
import com.promesa.promesa.domain.order.dto.request.OrderRequest;
import com.promesa.promesa.domain.order.exception.InvalidOrderQuantityException;
import com.promesa.promesa.domain.order.exception.OrderItemNotFoundException;
import com.promesa.promesa.domain.order.exception.OrderNotFoundException;
import com.promesa.promesa.domain.shippingAddress.dto.request.AddressRequest;
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
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartRepository;
    private final S3Service s3Service;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public OrderResponse createOrder(OrderRequest request, Member member) {
        LocalDateTime now = LocalDateTime.now();
        List<OrderItem> orderItems;

        PaymentRequest payment = request.payment();

        Order order = Order.builder()
                .member(member)
                .orderStatus(OrderStatus.WAITING_FOR_PAYMENT)
                .orderDate(now)
                .depositDeadline(now.plusDays(1)) // 입금 기한 = 주문 날짜 + 1일
                .bankName(payment.bankName())
                .depositorName(payment.depositorName())
                .build();

        if ("SINGLE".equalsIgnoreCase(request.type())) {
            // 수량 검증
            for (OrderItemRequest item : request.items()) {
                if (item.quantity() < 1) {
                    throw InvalidOrderQuantityException.EXCEPTION;
                }
            }

            orderItems = request.items().stream()
                    .map(req -> {
                        Item item = itemRepository.findById(req.itemId())
                                .orElseThrow(() -> ItemNotFoundException.EXCEPTION);
                        item.decreaseStock(req.quantity()); // 재고 감소
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
                    .map(cartItem -> {
                        Item item = cartItem.getItem();
                        item.decreaseStock(cartItem.getQuantity()); // 재고 감소
                        return OrderItem.of(order, item, cartItem.getQuantity(), item.getPrice());
                    }).toList();

            cartRepository.deleteAll(cartItems);
        } else {
            throw OrderNotFoundException.EXCEPTION;
        }

        // 연관 관계 설정
        orderItems.forEach(order::addOrderItem);

        // 총 금액 및 수량 계산
        int totalAmount = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
        int totalQuantity = orderItems.stream().mapToInt(OrderItem::getQuantity).sum();

        // 배송비 계산 (총 금액이 70,000원 미만이면 3,000원 추가)
        int deliveryFee = totalAmount < 70_000 ? 3000 : 0;

        // 필드 수동 설정
        order.getOrderItems().clear();
        orderItems.forEach(order::addOrderItem);
        order.setTotalAmount(totalAmount);
        order.setTotalQuantity(totalQuantity);

        AddressRequest addr = request.address();

        Delivery delivery = Delivery.builder()
                .receiverName(addr.getRecipientName())
                .receiverPhone(addr.getRecipientPhone())
                .zipCode(addr.getZipCode())
                .address(addr.getAddressMain())
                .addressDetail(addr.getAddressDetails())
                .deliveryStatus(DeliveryStatus.READY)
                .deliveryFee(deliveryFee)
                .build();

        order.setDelivery(delivery);
        orderRepository.save(order);

        return OrderResponse.of(order, delivery, s3Service, bucketName);
    }

    public List<OrderResponse> getOrders(Member member) {
        List<Order> orders = orderRepository.findByMemberOrderByCreatedAtDesc(member);

        return orders.stream()
                .map(order -> {
                    Delivery delivery = order.getDelivery();
                    return OrderResponse.of(order, delivery, s3Service, bucketName);
                })
                .toList();
    }


    public OrderResponse getOrderDetails(Member member, Long orderId) {
        Order order = orderRepository.findByIdAndMember(orderId, member)
                .orElseThrow(() -> OrderNotFoundException.EXCEPTION);

        Delivery delivery = order.getDelivery();

        return OrderResponse.of(order, delivery, s3Service, bucketName);
    }

    public List<OrderResponse> getOrders(OrderStatus orderStatus, OrderItemStatus itemStatus) {
        List<Order> orders = orderRepository.findWithItemsByStatus(orderStatus);

        return orders.stream()
                .map(order -> {
                    Delivery delivery = order.getDelivery();
                    if (delivery == null) throw DeliveryNotFoundException.EXCEPTION;
                    return OrderResponse.of(order, delivery, s3Service, bucketName);
                })
                .map(response -> {
                    if (itemStatus == null) return response;
                    List<OrderItemDetail> filtered = response.items().stream()
                            .filter(i -> i.itemStatus() == itemStatus)
                            .toList();
                    return new OrderResponse(response.summary(), response.deposit(), response.delivery(), filtered);
                })
                .filter(response -> !response.items().isEmpty()) // itemStatus 필터가 있을 때 빈 리스트 제거
                .toList();
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> OrderNotFoundException.EXCEPTION);

        order.changeStatus(newStatus);

        // 재고 복구
        if (newStatus == OrderStatus.CANCEL_NO_PAYMENT) {
            for (OrderItem orderItem : order.getOrderItems()) {
                Item item = orderItem.getItem();
                item.increaseStock(orderItem.getQuantity());
            }
        }
    }

    @Transactional
    public void updateOrderItemStatus(Long orderItemId, OrderItemStatus newStatus) {
        OrderItem item = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> OrderItemNotFoundException.EXCEPTION);

        item.changeStatus(newStatus);

        // 재고 복구
        if (newStatus == OrderItemStatus.CANCELLED || newStatus == OrderItemStatus.RETURNED) {
            Item product = item.getItem();
            product.increaseStock(item.getQuantity());
        }
    }
}