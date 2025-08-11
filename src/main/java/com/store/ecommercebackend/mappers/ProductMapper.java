package com.store.ecommercebackend.mappers;

import com.store.ecommercebackend.dto.response.ProductDto;
import com.store.ecommercebackend.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto (Product product);
}
