package com.demo.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 实现了GlobalFilter的Filter将会被用在所有的route上。
 * 当一个request匹配一个route的时候，Filter的web处理器会
 * 将所有GlobalFilter和Route上声明的特殊的GatewayFilter放入一个FilterChain，
 * 这些Filter会根据Ordered接口进行排序。
 * 因此这里需要实现Ordered接口，来控制这个Filter的执行顺序。
 */
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        LOGGER.info("CustomGlobalFilter starts");
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            LOGGER.info("CustomGlobalFilter ends");
        }));
    }

    @Override
    public int getOrder() {
        //对于所有的GlobalFilter，顺序可以规定为负数
        //对于所有的GatewayFilter，顺序可以规定为正数
        //这样就能确保GlobalFilter总是在GatewayFilter之前执行
        return -1;
    }
}
