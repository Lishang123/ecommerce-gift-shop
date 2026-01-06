package io.github.houcai.gift_shop_backend_order.clients;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

/**
 * The config class exposing the RestClient.Builder as a bean so it can be re-used.
 */
@Configuration
public class RestClientConfig {

    @Bean
    @Primary //mark not load-balanced bean as primary so that it is not used internally by eureka.
    /*
    Issue:
    DefaultEndpoint{ serviceUrl='http://localhost:7777/eureka/},
    exception=No instances available for localhost stacktrace=java.lang.IllegalStateException:
    No instances available for localhost

    Reason:
    @LoadBalanced is being applied to the HTTP client
    that Eureka uses internally. When that happens,
    any call to http://localhost:7777/eureka/ gets
    intercepted by Spring Cloud LoadBalancer, which
    treats localhost as a service id => tries to look
     it up in discovery → finds none → No instances available for localhost.
     */
    public RestClient.Builder restClientBuilder(){
        return RestClient.builder();
    }

    @Bean(name="lbRestClientBuilder")
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder(){
        return RestClient.builder();
    }

}
