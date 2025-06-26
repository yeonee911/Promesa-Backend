package com.promesa.promesa.domain.exhibition.dto.response;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.exhibition.domain.Exhibition;
import com.promesa.promesa.domain.exhibition.domain.ExhibitionStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

public record ExhibitionResponse(
        Long id,
        ExhibitionStatus status,
        String title,
        String description,
        String imageKey,
        String imageUrl
) {
    public static ExhibitionResponse of(Exhibition exhibition, String imageUrl) {
        final S3Service s3Service;
        return new ExhibitionResponse(
                exhibition.getId(),
                exhibition.getStatus(),
                exhibition.getTitle(),
                exhibition.getDescription(),
                exhibition.getImageKey(),
                imageUrl
        );
    }
}