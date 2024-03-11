package com.demo.gateway.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

public class MyServiceHealthIndicator implements ReactiveHealthIndicator {

    private final WebClient webClient;
    private final URI healthCheckUri;

    public MyServiceHealthIndicator(WebClient.Builder webClientBuilder, URI healthCheckUri) {
        this.webClient = webClientBuilder.build();
        this.healthCheckUri = healthCheckUri;
    }

    // need to call subsystem to collect their status:
    // - if success, build a service up response
    // - if error, build a service down response
    @Override
    public Mono<Health> health() {
        return webClient
                .get()
                .uri(healthCheckUri)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::serviceUp)
                .onErrorResume(e -> Mono.just(serviceDown(e)));
    }

    private Health serviceUp(String response) {
        return new Health.Builder()
                .up()
                .withDetail("response", response)
                .withDetail("uri", healthCheckUri)
                .build();
    }

    private Health serviceDown(Throwable t) {
        return new Health.Builder()
                .down(t)
                .withDetail("uri", healthCheckUri)
                .build();
    }
}
