package com.demo.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            //Custom Pre Filter. Suppose we can extract JWT and perform Authentication
            LOGGER.info("AuthFilter pre Handle: validating token.");
            ServerHttpRequest request = exchange.getRequest();
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("Missing auth information");
            }
            String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String[] authParts = authHeader.split(" ");
            if (authParts.length != 2 || !"Bearer".equals(authParts[0])) {
                throw new RuntimeException("Incorrect auth information");
            }
            String token = authParts[1];
            LOGGER.info("Token: " + token);

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                //Custom Post Filter. Suppose we can call error response handler based on error code.
                LOGGER.info("AuthFilter post Handle");
            }));
        };
    }

    public static class Config {
        // Put the configuration properties here
    }
}