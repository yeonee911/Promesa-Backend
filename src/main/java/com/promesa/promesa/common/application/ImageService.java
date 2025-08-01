package com.promesa.promesa.common.application;

import com.promesa.promesa.common.dto.s3.PresignedUrlRequest;
import com.promesa.promesa.common.dto.s3.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {
    private final S3Service s3Service;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    /**
     * 이미지 업로드용 presigned url 생성
     * @param request
     * @return
     */
    public List<PresignedUrlResponse> getPresignedPutUrls(PresignedUrlRequest request) {

        return s3Service.createPresignedPutUrl(
                bucketName,
                request.imageType(),
                request.referenceId(),
                request.subType(),
                request.subReferenceId(),
                request.fileNames(),
                request.metadata()
        );
    }

    /**
     * 이미지 삭제
     * @param key
     */
    public void deleteImage(String key) {
        s3Service.deleteObject(bucketName, key);
    }

    public String transferImage(String sourceKey, Long referenceId) {
        String targetKey = sourceKey.replaceFirst(  // 키 생성
                "([^/]+/)tmp/",
                "$1" + referenceId + "/"
        );
        s3Service.copyObject(bucketName, sourceKey, targetKey); // 객체 이동
        s3Service.deleteObject(bucketName, sourceKey);  // 기존 객체 삭제

        return targetKey;
    }
}