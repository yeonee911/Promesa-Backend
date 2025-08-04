package com.promesa.promesa.domain.shippingAddress.application;

import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.member.exception.MemberNotFoundException;
import com.promesa.promesa.domain.shippingAddress.dao.ShippingAddressRepository;
import com.promesa.promesa.domain.shippingAddress.domain.ShippingAddress;
import com.promesa.promesa.domain.shippingAddress.dto.request.AddressRequest;
import com.promesa.promesa.domain.shippingAddress.dto.response.AddressResponse;
import com.promesa.promesa.domain.shippingAddress.exception.ShippingAddressAlreadyExistsException;
import com.promesa.promesa.domain.shippingAddress.exception.ShippingAddressNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingAddressService {

    private final ShippingAddressRepository shippingAddressRepository;
    private final MemberRepository memberRepository;

    /**
     * 기본 배송지 조회
     * @param member
     * @return
     */
    @Transactional(readOnly = true)
    public AddressResponse getShippingAddress(Member member) {
        Member persistentMember = getPersistentMember(member);
        if (persistentMember.getShippingAddress() == null) {
            return null;
        }
        ShippingAddress address = persistentMember.getShippingAddress();
        return AddressResponse.from(address);
    }

    /**
     * 기본 배송지 추가
     * @param request
     * @param member
     * @return
     */
    @Transactional
    public AddressResponse addShippingAddress(@Valid AddressRequest request, Member member) {
        Member persistentMember = getPersistentMember(member);
        if (persistentMember.getShippingAddress() != null)    // 기본 배송지는 하나만 등록 가능
            throw ShippingAddressAlreadyExistsException.EXCEPTION;

        ShippingAddress address = ShippingAddress.builder()
                .recipientName(request.getRecipientName())
                .zipCode(request.getZipCode())
                .addressMain(request.getAddressMain())
                .addressDetails(request.getAddressDetails())
                .recipientPhone(request.getRecipientPhone())
                .build();
        ShippingAddress savedAddress = shippingAddressRepository.save(address);
        persistentMember.updateShippingAddress(savedAddress);
        memberRepository.save(persistentMember);
        return AddressResponse.from(savedAddress);
    }

    /**
     * 기본 배송지 정보 삭제
     * @param member
     */
    @Transactional
    public void deleteShippingAddress(Member member) {
        Member persistentMember = getPersistentMember(member);

        ShippingAddress address = persistentMember.getShippingAddress();
        persistentMember.updateShippingAddress(null);
        memberRepository.save(persistentMember);
        shippingAddressRepository.delete(address);
    }

    /**
     * 기본 배송지 정보 수정
     * @param request
     * @param member
     */
    @Transactional
    public AddressResponse updateShippingAddress(@Valid AddressRequest request, Member member) {
        Member persistentMember = getPersistentMember(member);

        ShippingAddress current = persistentMember.getShippingAddress();
        if (current == null) {
            throw ShippingAddressNotFoundException.EXCEPTION;
        }

        ShippingAddress address = shippingAddressRepository.findById(current.getId())
                .orElseThrow(() -> ShippingAddressNotFoundException.EXCEPTION);

        log.info("Before update: {}", address.getAddressDetails());
        address.update(
                request.getRecipientName(),
                request.getZipCode(),
                request.getAddressMain(),
                request.getAddressDetails(),
                request.getRecipientPhone()
        );
        log.info("After update: {}", address.getAddressDetails());
        shippingAddressRepository.save(address);

        return AddressResponse.from(address);
    }

    /**
     * 기본 배송지를 추가 또는 수정
     * @param request
     * @param member
     * @return
     */
    @Transactional
    public AddressResponse addOrUpdateShippingAddress(@Valid AddressRequest request, Member member) {
        Member persistentMember = getPersistentMember(member);

        if (persistentMember.getShippingAddress() != null)
            return updateShippingAddress(request, persistentMember);
        else
            return addShippingAddress(request, persistentMember);
    }

    private Member getPersistentMember(Member member) {
        return memberRepository.findById(member.getId())
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }
}
