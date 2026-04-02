package com.ecommerce.controller;

import com.ecommerce.dto.CartDto;
import com.ecommerce.service.CartService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService service;

    @GetMapping
    public ResponseEntity<CartDto> getCart(@RequestParam Long userId) {
        return ResponseEntity.ok(service.getUserCart(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<CartDto> add(@RequestParam Long userId,
                                       @RequestParam Long productId,
                                       @RequestParam int qty) {
        return ResponseEntity.ok(service.addToCart(userId, productId, qty));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CartDto> remove(@RequestParam Long userId,
                                          @RequestParam Long productId) {
        return ResponseEntity.ok(service.removeFromCart(userId, productId));
    }
}