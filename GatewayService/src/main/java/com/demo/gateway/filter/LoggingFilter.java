package com.demo.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String clientIp = request.getRemoteAddress().toString();
            String uri = request.getURI().toString();
            LOGGER.info("Received: {} from {}", uri, clientIp);

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                //Custom Post Filter. Suppose we can call error response handler based on error code.
                ServerHttpResponse response = exchange.getResponse();
                int statusCode = response.getRawStatusCode();
                LOGGER.info("Response: {}", statusCode);
            }));
        };
    }

    public static class Config {
        // Put the configuration properties here
    }
}