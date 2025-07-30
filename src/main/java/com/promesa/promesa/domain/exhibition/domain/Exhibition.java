package com.promesa.promesa.domain.exhibition.domain;

import com.promesa.promesa.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="exhibition")
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

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = true)
    private LocalDate endDate;

    @NotBlank
    @Column(name = "thumbnail_image_key", nullable = false)
    private String thumbnailImageKey;

    @Column(name = "detailed_image_key")    // UPCOMING일 경우 아직 상세페이지 제작 가능성 염두
    private String detailedImageKey;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(name = "exhibition_status", nullable = false)
    private ExhibitionStatus status;

    @OneToMany(mappedBy = "exhibition", cascade = CascadeType.ALL)
    private List<ExhibitionItem> exhibitionItems = new ArrayList<>();

    @OneToMany(mappedBy = "exhibition", cascade = CascadeType.ALL)
    private List<ExhibitionArtist> exhibitionArtists = new ArrayList<>();

    public void setStatus(ExhibitionStatus status) {
        this.status = status;
    }
}
