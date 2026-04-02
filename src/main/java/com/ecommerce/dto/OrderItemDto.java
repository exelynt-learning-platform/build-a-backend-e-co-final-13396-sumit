package com.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private int quantity;
    private double price;
}
