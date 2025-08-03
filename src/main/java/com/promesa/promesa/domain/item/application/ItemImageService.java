package com.promesa.promesa.domain.item.application;

import com.promesa.promesa.common.application.ImageService;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.domain.ItemImage;
import com.promesa.promesa.domain.item.dto.request.ItemImageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemImageService {

    private final ImageService imageService;

    public void uploadAndLinkImages(Item item, List<ItemImageRequest> reqs, String thumbnailKey, boolean needTransfer) {
        for (var req : reqs) {
            String targetKey = needTransfer
                    ? imageService.transferImage(req.key(), item.getId())
                    : req.key();

            ItemImage newItemImg = ItemImage.builder()
                    .imageKey(targetKey)
                    .isThumbnail(req.key().equals(thumbnailKey))
                    .sortOrder(req.sortOrder())
                    .item(item)
                    .build();
            item.addItemImage(newItemImg);
        }
    }
}
