package com.store.ecommercebackend.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDto {
    private Long id;
    private String name;
    private String email;

}
