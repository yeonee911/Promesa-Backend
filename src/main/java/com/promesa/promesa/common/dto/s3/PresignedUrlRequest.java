package com.promesa.promesa.common.dto.s3;

import java.util.Map;

public record PresignedUrlRequest (
  String keyName,
  Map<String, String> metadata
){}