package com.demo.gateway.filter;

import com.demo.gateway.service.DBService;
import com.demo.gateway.util.DataBufferUtil;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    @Autowired
    private DBService dbService;

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String clientIp = request.getRemoteAddress().toString();
            String uri = request.getURI().toString();
            log.info("Received: {} from {}", uri, clientIp);

            //read request body from ServerHttpRequest. Note that the body could be read for only once.
            String reqBody = resolveBodyFromRequest(request);
            log.info("RequestBody: {}", reqBody);

            //save request body to DB asynchronously.
            //Note this method should be annotated with @Async and it should returns Future
            //Because when we saving response body, we need to use this id to update some DB fields.
            Future<Integer> idFuture = dbService.saveRequestAsync(request.getURI().toString(), reqBody);

            //after we read body from original request, we need to set it back to the request so that
            //subsequent filters could read body again. To do so, we need to decorate the original
            //request and override the getBody() method, so that when subsequent filters trying to
            //get request body, this getBody() method should be invoked instead.
            ServerHttpRequest decoratedRequest = new ServerHttpRequestDecorator(request) {
                @Override
                public Flux<DataBuffer> getBody() {
                    return Flux.just(DataBufferUtil.stringToDataBuffer(reqBody));
                }
            };

            //here we also decorate the original response object and override the writeWith() method.
            //because we need to extract response body when the body is been written to the client.
            ServerHttpResponse response = exchange.getResponse();
            DataBufferFactory bufferFactory = response.bufferFactory();
            ServerHttpResponse decoratedResponse = new ServerHttpResponseDecorator(response) {
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>)body;
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            StringBuilder builder = new StringBuilder();
                            dataBuffers.forEach(dataBuffer -> {
                                builder.append(DataBufferUtil.dataBufferToString(dataBuffer));
                            });
                            String responseBody = builder.toString();
                            log.info("ResponseBody: {}", responseBody);

                            //save response body to DB asynchronously. Note here we need to use the
                            //idFuture parameters which previously returned from saving request body
                            dbService.saveResponseAsync(idFuture, response.getRawStatusCode(), responseBody);

                            // return the original response body to the client.
                            return bufferFactory.wrap(responseBody.getBytes(StandardCharsets.UTF_8));
                        }));
                    }
                    return super.writeWith(body);
                }
            };

            //set  the decorated request and response back to the ServerWebExchange object.
            //so that the override methods could be invoked by subsequent filters.
            ServerWebExchange decoratedExchange = exchange.mutate()
                    .request(decoratedRequest)
                    .response(decoratedResponse)
                    .build();
            return chain.filter(decoratedExchange);
            //set current filter order before the NettyWriteResponseFilter, otherwise the
            //response data could be write to the client before our LoggingFilter and thus
            //the override methods could never been invoked.
            //see: https://github.com/spring-cloud/spring-cloud-gateway/issues/1771
        }, NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1);
    }

    public static class Config {
        // Put the configuration properties here
    }

    private String resolveBodyFromRequest(ServerHttpRequest request) {
        Flux<DataBuffer> body = request.getBody();
        StringBuilder builder = new StringBuilder();
        //read request body from ServerHttpRequest.
        //Note that the body could be read for only once.
        body.subscribe(dataBuffer -> builder.append(
                DataBufferUtil.dataBufferToString(dataBuffer)));
        return builder.toString();
    }
}