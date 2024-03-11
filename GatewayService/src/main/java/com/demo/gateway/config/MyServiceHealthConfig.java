package com.demo.gateway.config;

import com.demo.gateway.health.MyServiceHealthIndicator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class MyServiceHealthConfig {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private RoutesConfig routesConfig;


    // need to create a bean for customized health indicator.
    // we can access below uri to check subsystem health status:
    // /actuator/health/custom/my-services
    @Bean("my-services")
    public ReactiveHealthContributor myServices() {
        return CompositeReactiveHealthContributor.fromMap(
            routesConfig.getRoutes().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> createServiceHealthIndicator(entry.getKey(), entry.getValue()))));
    }


    //example: serviceId=first-service, route=http://localhost:8081
    private MyServiceHealthIndicator createServiceHealthIndicator(String serviceId, String route) {
        String healthPath = "/" + serviceId + "/actuator/health/gateway";
        URI healthCheckUri = null;
        try {
            healthCheckUri = new URI(route).resolve(healthPath);
        } catch (URISyntaxException e) {
            log.error("Unable to resolve uri. route={}, path={}", route, healthPath);
            throw new RuntimeException(String.format("Unable to resolve uri."));
        }
        return new MyServiceHealthIndicator(webClientBuilder, healthCheckUri);
    }
}




