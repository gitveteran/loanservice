package com.turbine.loanservice.config;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.turbine.loanservice.config.properties.LoanClientConfigProperties;

@Configuration
public class LoanClientConfig {

    @Bean
    public RestTemplate thirdpartyLoanApiClient(LoanClientConfigProperties loanClientConfigProperties, RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .connectTimeout(Duration.ofMillis(loanClientConfigProperties.getConnectTimeout()))
                .readTimeout(Duration.ofMillis(loanClientConfigProperties.getReadTimeout()))
                .build();
    }
}
