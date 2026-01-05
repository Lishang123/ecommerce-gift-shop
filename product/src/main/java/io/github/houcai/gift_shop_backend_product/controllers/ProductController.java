package io.github.houcai.gift_shop_backend_product.controllers;

import io.github.houcai.gift_shop_backend_product.dtos.ProductRequest;
import io.github.houcai.gift_shop_backend_product.dtos.ProductResponse;
import io.github.houcai.gift_shop_backend_product.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        return new ResponseEntity<ProductResponse>(
                productService.createProduct(productRequest),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID id,
            @RequestBody ProductRequest productRequest) {
        return productService.updateProduct(id, productRequest).map(ResponseEntity::ok).orElseGet(
                () -> ResponseEntity.notFound().build()
        );
    }

    @PostMapping("/deactivate/{id}")
    public ResponseEntity<ProductResponse> deactivateProduct(@PathVariable UUID id){
        boolean found = productService.setActive(id, false);
        return found ? ResponseEntity.noContent().build(): ResponseEntity.notFound().build();
    }

    @PostMapping("/activate/{id}")
    public ResponseEntity<ProductResponse> activateProduct(@PathVariable UUID id){
        boolean found = productService.setActive(id, true);
        return found ? ResponseEntity.noContent().build(): ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable UUID id){
        boolean deleted = productService.deleteProduct(id);
        return deleted ? ResponseEntity.noContent().build(): ResponseEntity.notFound().build();
    }



}
