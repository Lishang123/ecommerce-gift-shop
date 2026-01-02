package io.github.houcai.gift_shop_backend.service;

import io.github.houcai.gift_shop_backend.dto.ProductRequest;
import io.github.houcai.gift_shop_backend.dto.ProductResponse;
import io.github.houcai.gift_shop_backend.mapper.ProductMapper;
import io.github.houcai.gift_shop_backend.model.Product;
import io.github.houcai.gift_shop_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    // private List<Product> productList =  new ArrayList<>();
    private final ProductRepository productRepository;

    /**
     * Request of creating a new product in the database.
     * @param productRequest the product to create
     * @return a product
     */
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.updateFrom(productRequest);
        //productList.add(product);
        Product savedProduct = productRepository.save(product);
        // no need to create mapper instance for static method!
        return ProductMapper.toResponse(savedProduct);
    }


    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(ProductMapper::toResponse).collect(Collectors.toList());
    }

    public Optional<ProductResponse> updateProduct(UUID id, ProductRequest productRequest) {
        return productRepository.findById(id).map(
                existingProduct -> {
                    existingProduct.updateFrom(productRequest);
                    return ProductMapper.toResponse(productRepository.save(existingProduct));
                }
        );
    }

    public boolean deleteProduct(UUID id){
        return productRepository.findById(id).map(
                product -> {
                    productRepository.deleteById(id);
                    return true;
                }
        ).orElse(false);
    }

    public boolean setActive(UUID id, boolean active){
        return productRepository.findById(id).map(
                product -> {
                    product.setActive(active);
                    productRepository.save(product);
                    return true;
                }
        ).orElse(false);
    }

//    public Optional<Product> getProductById(UUID id) {
//        return productList.stream()
//                .filter(product -> product.getId().equals(id)).findFirst();
//    }
}
