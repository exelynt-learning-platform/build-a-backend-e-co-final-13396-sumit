package com.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private String imageUrl;
}
