package com.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CartDto {
    private Long id;
    private Long userId;
    private List<CartItemDto> items;
}
