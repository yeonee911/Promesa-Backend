package com.promesa.promesa.domain.artist.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
public class UpdateArtistImageRequest {
    @NotBlank(message = "프로필은 필수입니다.")
    private String profileImageKey;
}
