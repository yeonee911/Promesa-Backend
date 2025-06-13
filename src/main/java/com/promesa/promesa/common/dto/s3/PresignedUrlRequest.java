package com.promesa.promesa.common.dto.s3;

import java.util.List;
import java.util.Map;

public record PresignedUrlRequest (
  List<String> keyNames,
  Map<String, String> metadata
){}