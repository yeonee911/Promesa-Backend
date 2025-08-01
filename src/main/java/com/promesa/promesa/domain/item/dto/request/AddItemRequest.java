package com.promesa.promesa.domain.item.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class AddItemRequest {
    @NotBlank(message = "상품명은 필수입니다")
    private String itemName;

    @Min(value = 1, message = "가격은 1원 이상이어야 합니다")
    private int price;

    @Min(value = 1, message = "재고 수량은 1개 이상이어야 합니다")
    private int stock;

    @NotBlank(message = "상품 코드는 필수입니다")
    private String productCode;

    @Min(value = 0, message = "음수가 될 수 없습니다")
    private int width;
    @Min(value = 0, message = "음수가 될 수 없습니다")
    private int height;
    @Min(value = 0, message = "음수가 될 수 없습니다")
    private int depth;

    @NotNull(message = "아티스트 아이디는 필수입니다")
    private Long artistId;

    @NotNull(message = "카테고리 아이디는 필수입니다")
    private Long categoryId;

    @NotNull
    @Size(min = 1, message = "최소 하나 이상의 이미지 정보가 필요합니다")
    @Valid
    private List<ItemImageRequest> imageKeys;

    @NotBlank(message = "썸네일을 지정해주세요")
    private String thumbnailKey;
}
