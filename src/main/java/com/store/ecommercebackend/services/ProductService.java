package com.store.ecommercebackend.services;

import com.store.ecommercebackend.dto.ProductDto;
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

}
