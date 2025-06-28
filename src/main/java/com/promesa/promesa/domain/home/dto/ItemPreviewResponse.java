package com.promesa.promesa.domain.home.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPreviewResponse {   // ← 반드시 public 으로
    private Long itemId;
    private String itemName;
    private String itemDescription;
    private int price;
    private String imageKey;
    private String imageUrl;
    private String artistName;
    private boolean isWished;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}