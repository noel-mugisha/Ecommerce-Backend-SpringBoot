package com.store.ecommercebackend.controllers;

import com.store.ecommercebackend.dto.request.ProductRequest;
import com.store.ecommercebackend.dto.response.ProductDto;
import com.store.ecommercebackend.repositories.CategoryRepository;
import com.store.ecommercebackend.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Controller
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryRepository categoryRepository;

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
    public ResponseEntity<?> getProductById (@PathVariable Long id) {
        var product = productService.getProductById(id);
        if (product != null)
            return ResponseEntity.ok(product);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Product with id " + id + " not found..." );
    }

    // Create a product
    @PostMapping
    public ResponseEntity<ProductDto> addProduct (
            @RequestBody ProductRequest request,
            UriComponentsBuilder  uriBuilder
    ) {
        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if (category == null)
            return ResponseEntity.badRequest().build();

        var productDto = productService.createProduct(request, category);
        var uri = uriBuilder.path("/api/v1/products/{id}")
                .buildAndExpand(productDto.getId()).toUri();

        return ResponseEntity.created(uri).body(productDto);
    }

    // Update a product
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct (
            @PathVariable Long id,
            @RequestBody ProductRequest request
    ) {
        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if (category == null)
            return ResponseEntity.badRequest().build();

        var productDto = productService.updateProduct(id, request, category);
        if (productDto == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(productDto);
    }

    // Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct (@PathVariable Long id) {
        var isDeleted = productService.deleteProduct(id);
        if (!isDeleted)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
