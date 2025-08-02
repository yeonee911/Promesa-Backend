package com.promesa.promesa.domain.exhibition.dto.request;

import com.promesa.promesa.domain.item.dto.request.ItemImageRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record AddExhibitionRequest(
        @NotBlank(message = "제목은 필수입니다")
        String title,

        @NotBlank(message = "설명은 필수입니다")
        String description,

        @NotNull(message = "시작일은 필수입니다")
        LocalDate startDate,
        LocalDate endDate,

        @NotBlank(message = "썸네일은 필수입니다")
        String thumbnailKey,    // 썸네일 1장 고정

        @Valid
        List<ExhibitionImageRequest> detailImageKeys,   // 상세 이미지 키만 포함

        @NotNull
        @Size(min = 1, message = "최소 1개의 작품 등록은 필수입니다")
        List<Long> itemIds
) {}
