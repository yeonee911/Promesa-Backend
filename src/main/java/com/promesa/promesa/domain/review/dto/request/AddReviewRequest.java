package com.promesa.promesa.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddReviewRequest {

        @NotBlank(message = "내용은 비어 있을 수 없습니다.")
        @Size(min = 10, max = 1000, message = "내용은 최소 10자, 최대 1000자입니다.")
        private String content;

        private int rating;

        private List<String> imageKeys;
}
