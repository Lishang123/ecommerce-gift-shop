package io.github.houcai.gift_shop_backend_order.clients;

import io.micrometer.tracing.Tracer;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.propagation.Propagator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

/**
 * The config class exposing the RestClient.Builder as a bean so it can be re-used.
 */
@Configuration
public class RestClientConfig {

    @Autowired(required = false)
    private ObservationRegistry observationRegistry;

    @Autowired(required = false)
    private Tracer tracer;

    @Autowired(required = false)
    private Propagator propagator;

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
        RestClient.Builder builder = RestClient.builder();

        // attach a request interceptor.
        if (observationRegistry != null){
            builder.requestInterceptor(createTracingInterceptor());
        }

        return builder;
    }

    /**
     * Create a ClientHttpRequestInterceptor (hook) that can intercept and modify outgoing HTTP
     * requests made by RestTemplate / RestClient (Spring 6), before they’re executed.
     * @return ClientHttpRequestInterceptor
     */
    private ClientHttpRequestInterceptor createTracingInterceptor() {
        return ((request, body, execution) -> {
            if (tracer != null && propagator != null
                    && tracer.currentSpan() != null) {
                propagator.inject(tracer.currentTraceContext().context(),
                        request.getHeaders(),
                        // the carrier is Spring’s HttpHeaders object attached to the outgoing request.
                        (carrier, key, value) -> carrier.add(key, value));
            }
            // Continues the interceptor chain and actually performs the HTTP request.
            return execution.execute(request, body);
        }
        );
    }

}
