package com.store.ecommercebackend.mappers;

import com.store.ecommercebackend.dto.request.ProductRequest;
import com.store.ecommercebackend.dto.response.ProductDto;
import com.store.ecommercebackend.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.name", target = "category")
    ProductDto toDto(Product product);

    Product toEntity (ProductRequest request);

    void updateProduct (@MappingTarget Product product, ProductRequest request);
}
