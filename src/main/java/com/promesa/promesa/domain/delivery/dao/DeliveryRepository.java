package com.promesa.promesa.domain.delivery.dao;

import com.promesa.promesa.domain.delivery.domain.Delivery;
import com.promesa.promesa.domain.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByOrder(Order order);
}
