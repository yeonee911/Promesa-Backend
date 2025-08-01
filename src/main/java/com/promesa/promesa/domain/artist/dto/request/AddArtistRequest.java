package com.promesa.promesa.domain.artist.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddArtistRequest {
    @NotBlank(message = "작가명은 필수입니다")
    private String artistName;

    private String subName;

    @NotBlank(message = "프로필 등록은 필수입니다")
    private String profileKey;

    @NotBlank(message = "작가 소개는 필수입니다")
    private String description;

    private String insta;

    @NotNull(message = "프로메사 회원만 등록할 수 있습니다")
    private Long memberId;
}
