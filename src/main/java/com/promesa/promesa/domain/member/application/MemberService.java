package com.promesa.promesa.domain.member.application;

import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.member.dto.request.MemberUpdateRequest;
import com.promesa.promesa.domain.member.dto.response.MemberResponse;
import com.promesa.promesa.domain.member.exception.MemberNotFoundException;
import com.promesa.promesa.domain.shippingAddress.application.ShippingAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ShippingAddressService shippingAddressService;

    @Transactional
    public MemberResponse updateProfile(Member member, MemberUpdateRequest request) {
        Member persistentMember = getPersistentMember(member);
        persistentMember.updateProfile(request);
        shippingAddressService.addOrUpdateShippingAddress(request.address(), persistentMember);
        memberRepository.save(persistentMember);
        return MemberResponse.from(persistentMember);
    }

    @Transactional
    public void withdraw(Member member) {
        Member persistentMember = getPersistentMember(member);
        persistentMember.withdraw();
        memberRepository.save(persistentMember);
    }

    private Member getPersistentMember(Member member) {
        return memberRepository.findById(member.getId())
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }
}
