package com.promesa.promesa.domain.member.domain;

import com.promesa.promesa.common.domain.BaseTimeEntity;
import com.promesa.promesa.domain.artist.domain.Artist;
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
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;    // 로그인한 사용자 이름

    private String provider;    // 사용자가 로그인한 서비스 (ex.google, kakao, naver)

    private String providerId;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Wish> wishes;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Artist artist;


    // 사용자의 이름 업데이트하는 메소드
    public Member updateMember(String name){
        this.name = name;
        return this;
    }
}
