package com.promesa.promesa.domain.item.validator;

import com.promesa.promesa.domain.item.dao.ItemRepository;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.domain.SaleStatus;
import com.promesa.promesa.domain.item.exception.DuplicateProductCodeException;
import com.promesa.promesa.domain.item.exception.InvalidSaleStatusChangeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemValidator {
    private final ItemRepository itemRepository;

    /**
     * 작품 코드 중복 방지
     * @param code
     * @param existingItemId
     */
    public void ensureProductCodeUnique(String code, Long existingItemId) {
        boolean exists = existingItemId == null
                ? itemRepository.existsByProductCode(code)
                : itemRepository.existsByProductCodeAndIdNot(code, existingItemId);
        if (exists) {
            throw DuplicateProductCodeException.EXCEPTION;
        }
    }

    /**
     * 판매 상태 변경 검증
     * @param newStatus
     * @param item
     */
    public void validateSaleStatusChange(SaleStatus newStatus, Item item) {
        if (newStatus == SaleStatus.SOLD_OUT ||
                (newStatus == SaleStatus.ON_SALE && item.getSaleStatus() == SaleStatus.SOLD_OUT)) {
            throw InvalidSaleStatusChangeException.EXCEPTION;
        }
    }
}
