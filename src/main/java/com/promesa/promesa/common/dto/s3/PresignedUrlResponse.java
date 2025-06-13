package com.promesa.promesa.common.dto.s3;

import java.util.List;

public record PresignedUrlResponse (
        List<String> presignedUrls
){}