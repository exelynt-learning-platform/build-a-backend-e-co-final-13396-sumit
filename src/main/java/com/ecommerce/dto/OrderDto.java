package com.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private Long userId;
    private List<OrderItemDto> items;
    private double totalPrice;
    private String shippingAddress;
    private String status;
}
