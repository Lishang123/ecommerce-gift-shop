package io.github.houcai.gift_shop_backend.service;

import io.github.houcai.gift_shop_backend.dto.ProductRequest;
import io.github.houcai.gift_shop_backend.dto.ProductResponse;
import io.github.houcai.gift_shop_backend.mapper.ProductMapper;
import io.github.houcai.gift_shop_backend.model.Product;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private List<Product> productList =  new ArrayList<>();


    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.updateFrom(productRequest);
        productList.add(product);
        // no need to create mapper instance for static method!
        return ProductMapper.toResponse(product);
    }


    public List<ProductResponse> getAllProducts() {
        return productList.stream().map(ProductMapper::toResponse).collect(Collectors.toList());
    }

    public Optional<ProductResponse> updateProduct(UUID id, ProductRequest productRequest) {
        return getProductById(id).map(
                existingProduct -> {
                    existingProduct.updateFrom(productRequest);
                    return ProductMapper.toResponse(existingProduct);
                }
        );
    }

    public Optional<Product> getProductById(UUID id) {
        return productList.stream()
                .filter(product -> product.getId().equals(id)).findFirst();
    }
}
