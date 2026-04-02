package com.ecommerce.controller;

import com.ecommerce.dto.OrderDto;
import com.ecommerce.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping("/create")
    public ResponseEntity<OrderDto> create(@RequestParam Long userId,
                                           @RequestParam String address) {
        OrderDto order = service.createOrder(userId, address);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
   