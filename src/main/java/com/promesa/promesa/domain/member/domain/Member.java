package com.promesa.promesa.domain.member.domain;

import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.wish.domain.Wish;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "wish", cascade = CascadeType.ALL)
    private List<Wish> wishes;

    @OneToOne(mappedBy = "artist", cascade = CascadeType.ALL)
    private Artist artist;
}
