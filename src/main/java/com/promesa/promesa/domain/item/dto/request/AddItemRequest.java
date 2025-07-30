package com.promesa.promesa.domain.item.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class AddItemRequest {
    @NotBlank
    private String itemName;

    @Min(1)
    private int price;

    @Min(1)
    private int stock;

    @NotBlank
    private String productCode;

    private int width;
    private int height;
    private int depth;

    @NotNull
    private Long artistId;

    @NotNull
    private Long categoryId;

    @NotNull
    private List<String> imageKeys;

    @NotNull
    private String thumbnailKey;
}
