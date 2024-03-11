package com.demo.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@ConfigurationProperties
@Configuration
@Data
public class RoutesConfig {
    private Map<String, String> routes;
}
