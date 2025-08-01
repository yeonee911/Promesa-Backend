package com.promesa.promesa.common.api;

import com.promesa.promesa.common.application.ImageService;
import com.promesa.promesa.common.dto.s3.PresignedUrlRequest;
import com.promesa.promesa.common.dto.s3.PresignedUrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping()
    @Operation(summary = "이미지 업로드용 url 발급")
    public ResponseEntity<List<PresignedUrlResponse>> getPresignedPutUrls(@RequestBody PresignedUrlRequest request) {
        return ResponseEntity.ok(imageService.getPresignedPutUrls(request));
    }

    @DeleteMapping()
    @Operation(summary = "이미지 삭제")
    public ResponseEntity<Void> deleteImage(
            @RequestParam String key
    ){
        imageService.deleteImage(key);
        return ResponseEntity.ok(null);
    }
}
