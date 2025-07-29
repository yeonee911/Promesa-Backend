package com.promesa.promesa.domain.exhibition.dto.response;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.exhibition.domain.Exhibition;
import com.promesa.promesa.domain.exhibition.domain.ExhibitionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExhibitionSummary(
        Long id,
        ExhibitionStatus status,
        String title,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        String thumbnailImageKey,
        String thumbnailImageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ExhibitionSummary of(Exhibition exhibition, String imageUrl) {
        final S3Service s3Service;
        return new ExhibitionSummary(
                exhibition.getId(),
                exhibition.getStatus(),
                exhibition.getTitle(),
                exhibition.getDescription(),
                exhibition.getStartDate(),
                exhibition.getEndDate(),
                exhibition.getThumbnailImageKey(),
                imageUrl,
                exhibition.getCreatedAt(),
                exhibition.getUpdatedAt()
        );
    }
}