package com.promesa.promesa.domain.exhibition.application;

import com.promesa.promesa.common.application.ImageService;
import com.promesa.promesa.domain.exhibition.domain.Exhibition;
import com.promesa.promesa.domain.exhibition.domain.ExhibitionImage;
import com.promesa.promesa.domain.exhibition.dto.request.ExhibitionImageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExhibitionImageService {

    private final ImageService imageService;

    public void uploadAndLinkImages(
            Exhibition exhibition,
            List<ExhibitionImageRequest> reqs,
            String thumbnailKey,
            boolean needTransfer
    ) {
        for (var req : reqs) {
            String targetKey = needTransfer
                    ? imageService.transferImage(req.key(), exhibition.getId())
                    : req.key();

            ExhibitionImage newImage = ExhibitionImage.builder()
                    .imageKey(targetKey)
                    .isThumbnail(req.key().equals(thumbnailKey))
                    .sortOrder(req.sortOrder())
                    .exhibition(exhibition)
                    .build();

            exhibition.addExhibitionImage(newImage);
        }
    }
}
