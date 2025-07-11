package com.promesa.promesa.domain.artist.domain;

import com.promesa.promesa.common.domain.BaseTimeEntity;
import com.promesa.promesa.domain.exhibition.domain.ExhibitionArtist;
import com.promesa.promesa.domain.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="artist")
public class Artist extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_id")
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(name = "profileImage_key")
    private String profileImageKey;

    @NotBlank
    @Column(nullable = false)
    private String description;

    private String insta;

    @Column(name = "wish_count")
    private int wishCount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<ExhibitionArtist> exhibitionArtists = new ArrayList<>();

    public void increaseWishCount() {
        this.wishCount++;
    }

    public void decreaseWishCount() {
        if (this.wishCount > 0) {
            this.wishCount -= 1;
        } else {
            this.wishCount = 0; // wishCount가 음수가 되지 않도록 방지
        }
    }
}