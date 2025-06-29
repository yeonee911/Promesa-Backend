package com.promesa.promesa.domain.exhibition.domain;

import com.promesa.promesa.common.domain.BaseTimeEntity;
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
public class Exhibition extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exhibition_id")
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotBlank
    @Column(name = "image_key", nullable = false)
    private String imageKey;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(name = "exhibition_status", nullable = false)
    private ExhibitionStatus status;

    @OneToMany(mappedBy = "exhibition", cascade = CascadeType.ALL)
    private List<ExhibitionItem> exhibitionItems = new ArrayList<>();

    @OneToMany(mappedBy = "exhibition", cascade = CascadeType.ALL)
    private List<ExhibitionArtist> exhibitionArtists = new ArrayList<>();
}
