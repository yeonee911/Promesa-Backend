package com.promesa.promesa;

import com.promesa.promesa.security.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.SystemMetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication(
		exclude = SystemMetricsAutoConfiguration.class
)
@EnableConfigurationProperties(JwtProperties.class)
public class PromesaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PromesaApplication.class, args);
	}

}
