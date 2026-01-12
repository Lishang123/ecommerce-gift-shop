package io.github.houcai.gift_shop_backend_gateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * The gateway configuration class.
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RedisRateLimiter redisRateLimiter(){
        // Allow 10 tokens per second on average.
        // can suddenly handle up to 20 tokens at once.
        // each request consumes 1 token.
        return new RedisRateLimiter(10, 20, 1);
    }

    @Bean
    public KeyResolver hostNameKeyResolver(){
        return exchange ->
                Mono.justOrEmpty(exchange.getRequest().getRemoteAddress())
                        .map(addr -> addr.getHostName())
                        .switchIfEmpty(Mono.just("unknown"));
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                /*
                    Rate limit first, then circuit breaker, then retry:
                    RateLimiter rejects excess traffic immediately (cheap).
                    CircuitBreaker blocks calls when downstream is unhealthy (also cheap).
                    Retry only happens when the call is actually attempted, and you avoid “retrying” 429s / OPEN-breaker fast-fails.
                 */
                .route("user-service",
                        r -> r.path("/api/users/**")
                                //.filters(f -> f.rewritePath("/users(?<segment>/?.*)",
                                //        "/api/users${segment}"))
                                .filters(f ->
                                        f.requestRateLimiter(config -> config
                                                .setRateLimiter(redisRateLimiter())
                                                .setKeyResolver(hostNameKeyResolver()))
                                        .circuitBreaker(config -> config
                                                .setName("giftshopBreaker")
                                                .setFallbackUri("forward:/fallback/users"))
                                        .retry(config -> config
                                                .setRetries(10)
                                                .setMethods(HttpMethod.GET)))
                                .uri("lb://USER-SERVICE")) //only possible with eureka.
                                //.uri("http://localhost:8081"))
                .route("product-service",
                        r -> r.path("/api/products/**")
                                .filters(f ->
                                        f.circuitBreaker(config -> config
                                                .setName("giftshopBreaker")
                                                .setFallbackUri("forward:/fallback/products"))
                                        .retry(config -> config
                                                .setRetries(10)
                                                .setMethods(HttpMethod.GET)
                                                .setBackoff(
                                                        Duration.ofMillis(200),   // first delay
                                                        Duration.ofSeconds(2),    // max delay
                                                        2,                      // multiplier
                                                        false                     // exponential? (false = fixed)
                                                )))
                                .uri("lb://PRODUCT-SERVICE"))
                                //.uri("http://localhost:8082"))
                .route("order-service",
                        r -> r.path("/api/order/**", "/api/cart/**")
                                .filters(f -> f
                                        .circuitBreaker(config -> config
                                            .setName("giftshopBreaker")
                                            .setFallbackUri("forward:/fallback/order"))
                                        .retry(config -> config
                                                .setRetries(5)
                                                .setMethods(HttpMethod.GET)
                                        ))
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
