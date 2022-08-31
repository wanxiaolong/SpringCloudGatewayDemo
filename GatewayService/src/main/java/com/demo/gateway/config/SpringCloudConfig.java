//package com.demo.gateway.config;
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 用代码的方式来配置网关的路由规则。
// * 注意：如果选择用代码来配置路由，则需要注释掉application.yml中的路由配置
// */
//@Configuration
//public class SpringCloudConfig {
//    @Bean
//    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route(r -> r.path("/first/**")
//                        .uri("http://localhost:8081/"))
//                .route(r -> r.path("/second/**")
//                        .uri("http://localhost:8082/"))
//                .build();
//    }
//}
