package com.promesa.promesa.domain.member.domain;

import com.promesa.promesa.common.domain.BaseTimeEntity;
import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.member.dto.request.MemberUpdateRequest;
import com.promesa.promesa.domain.shippingAddress.domain.ShippingAddress;
import com.promesa.promesa.domain.wish.domain.Wish;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name="member")
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;    // 로그인한 사용자 이름

    private String provider;    // 사용자가 로그인한 서비스 (ex.google, kakao, naver)

    private String providerId;

    private String phone;
    private Boolean smsAgree = true;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer birthYear;
    private Integer birthMonth;
    private Integer birthDay;
    private Boolean isSolar;

    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Wish> wishes;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Artist artist;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id", unique = true)
    private ShippingAddress shippingAddress;

    // 사용자의 이름 업데이트하는 메소드
    public Member updateMember(String name){
        this.name = name;
        return this;
    }

    /**
     * 기본 배송지를 업데이트
     * @param shippingAddress
     */
    public void updateShippingAddress(ShippingAddress shippingAddress){
        this.shippingAddress = shippingAddress;
    }

    public void updateProfile(MemberUpdateRequest memberUpdateRequest) {
        this.phone = memberUpdateRequest.phone();
        this.smsAgree = memberUpdateRequest.smsAgree();
        this.gender = memberUpdateRequest.gender();
        this.birthYear = memberUpdateRequest.birth().year();
        this.birthMonth = memberUpdateRequest.birth().month();
        this.birthDay = memberUpdateRequest.birth().day();
        this.isSolar = memberUpdateRequest.birth().solar();
    }

    public void withdraw() {
        this.isDeleted = true;
    }

    public void restore() {
        this.isDeleted = false;
    }

}
