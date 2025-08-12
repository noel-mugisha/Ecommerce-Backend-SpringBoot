package com.store.ecommercebackend.services;

import com.store.ecommercebackend.dto.request.ProductRequest;
import com.store.ecommercebackend.dto.response.ProductDto;
import com.store.ecommercebackend.entities.Category;
import com.store.ecommercebackend.mappers.ProductMapper;
import com.store.ecommercebackend.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    // Get all products
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .toList();
    }

    // Sort products using categoryId
    public List<ProductDto> getAllProducts(Byte id) {
        return productRepository.findByCategoryId(id).stream()
                .map(productMapper::toDto)
                .toList();
    }

    // Get a single product
    public ProductDto getProductById(Long productId) {
        return productRepository.findById(productId)
                .map(productMapper::toDto)
                .orElse(null);
    }

    // Creating a product
    public ProductDto createProduct(ProductRequest request, Category category) {
        var product = productMapper.toEntity(request);
        product.setCategory(category);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    // Updating a product
    public ProductDto updateProduct(Long id, ProductRequest request, Category category) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null)
            return null;
        productMapper.updateProduct(product, request);
        product.setCategory(category);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    // Deleting a product
    public boolean deleteProduct (Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null)
            return false;
        productRepository.delete(product);
        return true;
    }

}
