package io.github.houcai.gift_shop_backend_gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product-service",
                        r -> r.path("/api/products/**")
                                .uri("lb://PRODUCT-SERVICE"))
                .route("user-service",
                        r -> r.path("/api/users/**")
                                .uri("lb://USER-SERVICE"))
                .route("order-service",
                        r -> r.path("/api/orders/**", "/api/orders/**")
                                .uri("lb://ORDER-SERVICE"))
                .build();
    }
}
