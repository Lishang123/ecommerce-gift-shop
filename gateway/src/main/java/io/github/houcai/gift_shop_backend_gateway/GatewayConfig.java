package io.github.houcai.gift_shop_backend_gateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service",
                        r -> r.path("/api/users/**")
                                //.filters(f -> f.rewritePath("/users(?<segment>/?.*)",
                                //        "/api/users${segment}"))
                                .filters(f -> f.circuitBreaker(config -> config
                                        .setName("giftshopBreaker")
                                        .setFallbackUri("forward:/fallback/users")))
                                .uri("lb://USER-SERVICE")) //only possible with eureka.
                                //.uri("http://localhost:8081"))
                .route("product-service",
                        r -> r.path("/api/products/**")
                                .filters(f -> f.circuitBreaker(config -> config
                                        .setName("giftshopBreaker")
                                        .setFallbackUri("forward:/fallback/products")))
                                .uri("lb://PRODUCT-SERVICE"))
                                //.uri("http://localhost:8082"))
                .route("order-service",
                        r -> r.path("/api/orders/**", "/api/orders/**")
                                .filters(f -> f.circuitBreaker(config -> config
                                        .setName("giftshopBreaker")
                                        .setFallbackUri("forward:/fallback/order")))
                                .uri("lb://ORDER-SERVICE"))
                                //.uri("http://localhost:8083"))
                .route("eureka-server", r -> r
                        .path("/eureka")
                        // replace localhost:8080/eureka with localhost:7777/
                        .filters(f -> f.rewritePath("/eureka", "/" ))
                        .uri("http://localhost:7777"))
                .route("eureka-static", r -> r
                        .path("/eureka/**")
                        .uri("http://localhost:7777"))
                .build();
    }


}
