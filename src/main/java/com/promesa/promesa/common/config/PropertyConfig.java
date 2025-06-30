package com.promesa.promesa.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "file:.env.properties", ignoreResourceNotFound = true)
public class PropertyConfig {
}
