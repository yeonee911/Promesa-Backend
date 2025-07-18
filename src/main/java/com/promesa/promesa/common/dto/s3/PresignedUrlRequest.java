package com.promesa.promesa.common.dto.s3;

import com.promesa.promesa.common.consts.ImageType;
import com.promesa.promesa.common.consts.SubType;

import java.util.List;
import java.util.Map;

public record PresignedUrlRequest (
        ImageType imageType,
        Long referenceId,
        SubType subType,
        Long subReferenceId,
        List<String> fileNames,
        Map<String, String> metadata
){}