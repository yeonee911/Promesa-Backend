package com.promesa.promesa.domain.shippingAddress.dao;

import com.promesa.promesa.domain.shippingAddress.domain.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {
}
