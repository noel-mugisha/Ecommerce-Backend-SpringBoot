package com.store.ecommercebackend.controllers;

import com.store.ecommercebackend.dto.response.ProductDto;
import com.store.ecommercebackend.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

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

}
