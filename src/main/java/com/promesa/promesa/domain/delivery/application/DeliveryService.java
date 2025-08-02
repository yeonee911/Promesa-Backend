package com.promesa.promesa.domain.delivery.application;

import com.promesa.promesa.domain.delivery.dao.DeliveryRepository;
import com.promesa.promesa.domain.delivery.domain.Delivery;
import com.promesa.promesa.domain.delivery.dto.request.DeliveryRequest;
import com.promesa.promesa.domain.delivery.dto.response.DeliveryResponse;
import com.promesa.promesa.domain.delivery.exception.DeliveryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryResponse getDelivery(Long orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> DeliveryNotFoundException.EXCEPTION);
        return DeliveryResponse.from(delivery);
    }

    @Transactional
    public DeliveryResponse updateDelivery(Long deliveryId, DeliveryRequest request) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> DeliveryNotFoundException.EXCEPTION);

        delivery.updateFrom(request);

        return DeliveryResponse.from(delivery);
    }


}

