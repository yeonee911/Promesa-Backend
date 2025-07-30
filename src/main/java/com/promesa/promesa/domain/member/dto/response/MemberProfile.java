package com.promesa.promesa.domain.member.dto.response;

import com.promesa.promesa.domain.member.domain.Gender;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.shippingAddress.domain.ShippingAddress;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@Builder
public class MemberProfile {
    private String name;
    private String provider;
    private String providerId;

    private String phone;
    private Boolean smsAgree;
    private Gender gender;

    private Integer birthYear;
    private Integer birthMonth;
    private Integer birthDay;
    private Boolean isSolar;

    private String recipientName;
    private String zipCode;
    private String addressMain;
    private String addressDetails;
    private String recipientPhone;

    public static MemberProfile from(Member member) {
        ShippingAddress addr = member.getShippingAddress();

        return MemberProfile.builder()
                .name(member.getName())
                .provider(member.getProvider())
                .providerId(member.getProviderId())
                .phone(member.getPhone())
                .smsAgree(member.getSmsAgree())
                .gender(member.getGender())
                .birthYear(member.getBirthYear())
                .birthMonth(member.getBirthMonth())
                .birthDay(member.getBirthDay())
                .isSolar(member.getIsSolar())
                .recipientName(addr != null ? addr.getRecipientName() : null)
                .zipCode(addr != null ? addr.getZipCode() : null)
                .addressMain(addr != null ? addr.getAddressMain() : null)
                .addressDetails(addr != null ? addr.getAddressDetails() : null)
                .recipientPhone(addr != null ? addr.getRecipientPhone() : null)
                .build();
    }

    // OAuth2 로그인 사용자용
    public static MemberProfile from(OAuth2User user) {
        return MemberProfile.builder()
                .name((String) user.getAttribute("name"))
                .provider((String) user.getAttribute("provider"))
                .providerId((String) user.getAttribute("providerId"))
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .name(this.name)
                .provider(this.provider)
                .providerId(this.providerId)
                .build();
    }
}
