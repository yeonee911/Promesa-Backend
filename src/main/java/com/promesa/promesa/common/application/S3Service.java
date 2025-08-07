package com.promesa.promesa.common.application;

import com.promesa.promesa.common.consts.ImageType;
import com.promesa.promesa.common.consts.SubType;
import com.promesa.promesa.common.dto.s3.PresignedUrlResponse;
import com.promesa.promesa.common.exception.InternalServerError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    @Value("${aws.s3.presigned.expire-minutes}")
    private long expireMinutes;

    /**
     * PresignedUrl 생성
     * @param bucketName    S3 버킷 이름
     * @param keyName   객체 키
     * @return  유효한 presigned URL
     */
    private static final String CLOUDFRONT_HOST =
            "https://d2e23qj94u656x.cloudfront.net"; // 배포 도메인

    public String createPresignedGetUrl(String bucketName, String keyName) {
        try {

            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(expireMinutes))  // 만료 시간 설정
                    .getObjectRequest(objectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            String url = presignedRequest.url().toString();

            return url.replace("https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com",
                    CLOUDFRONT_HOST);

        }catch (Exception e){
            log.error("Presigned URL 생성 실패: {}/{}", bucketName, keyName, e);
            throw InternalServerError.EXCEPTION;
        }
    }

    public List<PresignedUrlResponse> createPresignedPutUrl(
            String bucketName,
            ImageType imageType,
            Long referenceId,
            SubType subType,
            Long subReferenceId,
            List<String> fileNames,
            Map<String, String> metadata
    ) {
        try {
            return fileNames.stream()
                    .map(originalFileName -> {
                        try {
                            String key = generateKey(imageType,  referenceId, subType, subReferenceId, originalFileName);

                            PutObjectRequest objectRequest = PutObjectRequest.builder()
                                    .bucket(bucketName)
                                    .key(key)
                                    .metadata(metadata)
                                    .build();

                            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                                    .signatureDuration(Duration.ofMinutes(expireMinutes))
                                    .putObjectRequest(objectRequest)
                                    .build();

                            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
                            log.info("Presigned URL 생성: {}", presignedRequest.url());

                            String s3Url = presignedRequest.url().toExternalForm();
                            String cloudfrontUrl = s3Url.replace("https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com", CLOUDFRONT_HOST);
                            return new PresignedUrlResponse(key, cloudfrontUrl);

                        } catch (Exception e) {
                            log.error("Presigned URL 생성 실패", e);
                            throw InternalServerError.EXCEPTION;
                        }
                    })
                    .toList();
        } catch (Exception e) {
            log.error("Presigned URL 생성 실패", e);
            throw InternalServerError.EXCEPTION;
        }
    }

    public void deleteObject(String bucketName, String key) {
        try {
            s3Client.deleteObject(builder -> builder
                    .bucket(bucketName)
                    .key(key)
                    .build()
            );
            log.info("S3 객체 삭제 완료: {}/{}", bucketName, key);
        } catch (Exception e) {
            log.error("S3 객체 삭제 실패: {}/{}", bucketName, key, e);
            throw InternalServerError.EXCEPTION;
        }
    }

    public void copyObject(String bucketName, String sourceKey, String targetKey) {
        try {
            String encodedSource = URLEncoder.encode(bucketName + "/" + sourceKey, StandardCharsets.UTF_8);

            s3Client.copyObject(builder -> builder
                    .copySource(encodedSource)
                    .destinationBucket(bucketName)
                    .destinationKey(targetKey)
                    .build()
            );
            log.info("S3 객체 복사 완료: {}/{} -> {}/{}", bucketName, sourceKey, bucketName, targetKey);
        } catch (Exception e) {
            log.error("S3 객체 복사 실패: {}/{} -> {}/{}", bucketName, sourceKey, bucketName, targetKey, e);
            throw InternalServerError.EXCEPTION;
        }
    }

    private String generateKey(ImageType imageType, Long referenceId, SubType subType, Long subReferenceId , String originalFileName) {
        String uuid = UUID.randomUUID().toString();

        StringBuilder key = new StringBuilder();
        key.append(imageType.getPath())
                .append("/")
                .append(referenceId == null ? "tmp" : referenceId)  // 등록 api인 경우 아직 id 값이 없으므로 임시 경로 제공
                .append("/")
                .append(subType.getPath());

        if (subReferenceId != null) {
            key.append("/").append(subReferenceId);
        }

        key.append("/").append(uuid).append("-").append(sanitizeFileName(originalFileName));

        return key.toString();
    }

    private String sanitizeFileName(String originalFileName) {
        return originalFileName.replaceAll("[\\\\/]", "_");
    }
}