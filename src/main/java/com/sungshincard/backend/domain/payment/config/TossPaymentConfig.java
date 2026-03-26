package com.sungshincard.backend.domain.payment.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "toss")
@Getter
@Setter
public class TossPaymentConfig {
    private String clientKey;
    private String secretKey;
}
