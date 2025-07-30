package com.promesa.promesa.domain.member.application;

import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.member.dto.request.MemberUpdateRequest;
import com.promesa.promesa.domain.member.dto.response.MemberResponse;
import com.promesa.promesa.domain.shippingAddress.application.ShippingAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ShippingAddressService shippingAddressService;

    public MemberResponse updateProfile(Member member, MemberUpdateRequest request) {
        member.updateProfile(request);
        shippingAddressService.addOrUpdateShippingAddress(request.address(), member);
        memberRepository.save(member);
        return MemberResponse.from(member);
    }

    public void withdraw(Member member) {
        member.withdraw();
        memberRepository.save(member);
    }

}
