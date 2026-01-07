package io.github.houcai.gift_shop_backend_gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback/products")
    public ResponseEntity<ProblemDetail> productServiceFallback(ServerHttpRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);
        pd.setTitle("Upstream service unavailable");
        pd.setDetail("Product service is unavailable. Please try again later.");
        pd.setProperty("service", "product-service");
        pd.setProperty("path", request.getURI().getPath());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(pd);
    }

    @GetMapping("/fallback/users")
    public ResponseEntity<ProblemDetail> userServiceFallback(ServerHttpRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);
        pd.setTitle("Upstream service unavailable");
        pd.setDetail("User service is unavailable. Please try again later.");
        pd.setProperty("service", "user-service");
        pd.setProperty("path", request.getURI().getPath());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(pd);
    }

    @GetMapping("/fallback/order")
    public ResponseEntity<ProblemDetail> orderServiceFallback(ServerHttpRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);
        pd.setTitle("Upstream service unavailable");
        pd.setDetail("Order service is unavailable. Please try again later.");
        pd.setProperty("service", "order-service");
        pd.setProperty("path", request.getURI().getPath());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(pd);
    }
}
