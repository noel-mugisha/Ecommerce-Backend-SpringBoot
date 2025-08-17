package com.store.ecommercebackend.controllers;

import com.store.ecommercebackend.dto.request.ProductRequest;
import com.store.ecommercebackend.dto.response.ProductDto;
import com.store.ecommercebackend.exceptions.CategoryNotFoundException;
import com.store.ecommercebackend.exceptions.ProductNotFoundException;
import com.store.ecommercebackend.mappers.ProductMapper;
import com.store.ecommercebackend.repositories.CategoryRepository;
import com.store.ecommercebackend.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    // Get all products
    @GetMapping()
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestParam(name = "categoryId", required = false) Byte categoryId
    ) {
        if (categoryId == null) {
            return ResponseEntity.ok(productService.getAllProducts());
        }
        return ResponseEntity.ok(productService.getAllProducts(categoryId));
    }

    // Get a single product
    @GetMapping("{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        var product = productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found!.."));
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    // Create a product
    @PostMapping
    public ResponseEntity<ProductDto> addProduct(
            @RequestBody ProductRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var id = request.getCategoryId();
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + id + " not found!.."));
        var productDto = productService.createProduct(request, category);
        var uri = uriBuilder.path("/api/v1/products/{id}")
                .buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }

    // Update a product
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request
    ) {
        var category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + id + " not found!.."));
        var productDto = productService.updateProduct(id, request, category);
        return ResponseEntity.ok(productDto);
    }

    // Delete a product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
