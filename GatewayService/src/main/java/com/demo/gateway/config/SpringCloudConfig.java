//package com.demo.gateway.config;
//
//import com.demo.gateway.filter.AuthFilter;
//import com.demo.gateway.filter.LoggingFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
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
//
//    @Autowired
//    private AuthFilter authFilter;
//
//    @Autowired
//    private LoggingFilter loggingFilter;
//
//    @Bean
//    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
//        GatewayFilter logging = loggingFilter.apply((LoggingFilter.Config) null);
//        GatewayFilter auth = authFilter.apply((AuthFilter.Config) null);
//        return builder.routes()
//                .route(r -> r.path("/first/**")
//                        //添加的GatewayFilter的顺序就是执行的顺序
//                        .filters(f -> f.filters(logging, auth))
//                        .uri("http://localhost:8081/")
//                )
//                .route(r -> r.path("/second/**")
//                        .filters(f -> f.filters(auth, logging))
//                        .uri("http://localhost:8082/"))
//                .build();
//    }
//}
