package io.github.houcai.gift_shop_backend_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity // Turns on Spring Security for reactive (WebFlux) applications
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        return serverHttpSecurity
                // CSRF is disabled.
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // every request must be authenticated.
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/api/products/**").hasRole("PRODUCT")
                        .pathMatchers("/api/users/**").hasRole("USER")
                        .anyExchange().authenticated())
                // this application is an OAuth2 Resource Server
                // a custom converter extracts roles (the user’s authorities)
                .oauth2ResourceServer(oauth2 -> oauth2
                    // The OAuth2 resource server uses JWT tokens, not opaque tokens.
                    // Spring now: Decodes JWT, Verifies signature, Parses claims
//                                .jwt(Customizer.withDefaults())
                    .jwt(jwt ->
                            // After decoding the JWT, use THIS converter to turn it into an Authentication object.
                            jwt.jwtAuthenticationConverter(
                                    //Given a JWT → produce a reactive Authentication token
                                    grantedAuthoritiesExtractor()
                            )
                    )
                )
                .build();
    }

    /**
     * This method defines how Spring Security turns a JWT into an authenticated
     * user with roles in a reactive (WebFlux) application.
     * It extracts roles from a specific place in the JWT (resource_access → oauth2-pkce → roles)
     * and converts them into Spring Security authorities (ROLE_*).
     *
     * Spring Security will call this after:
     * signature verification
     * expiration check
     * issuer/audience validation
     * So at this point, the JWT is already trusted.
     *
     * @return Authentication object
     */
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor(){
        // Spring Security’s standard helper for: taking a JWT producing a JwtAuthenticationToken
        ReactiveJwtAuthenticationConverter jwtAuthenticationConverter =
                new ReactiveJwtAuthenticationConverter();
        // But by default, it does not know where your roles are, so we customize it next
        // “Given this JWT, what authorities (roles) should the user have?”
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter( jwt -> {
            List<String> roles = jwt
                    .getClaimAsMap("resource_access")
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().equals("oauth2-pkce"))
                    .flatMap(entry -> ((Map<String, List<String>>) entry.getValue())
                        .get("roles").stream())
                    .toList();

            System.out.println("Extracted Role: " + roles);
            // Flux is a reactive sequence that can emit 0, 1, or many values.
            // Mono<T> → 0 or 1 value
            // Flux<T> → 0…N values
            return Flux
                    // Create a reactive stream that will emit "user" -> "admin" -> ...
                    .fromIterable(roles)
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role));
        });
        return jwtAuthenticationConverter;
    }



}
