package com.ecommerce.service;

import com.ecommerce.Repository.CartRepository;
import com.ecommerce.Repository.ProductRepository;
import com.ecommerce.Repository.UserRepository;
import com.ecommerce.dto.CartDto;
import com.ecommerce.dto.CartItemDto;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    @Transactional
    public CartDto getUserCart(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Cart cart = cartRepo.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepo.save(newCart);
        });
        return toDto(cart);
    }

    @Transactional
    public CartDto addToCart(Long userId, Long productId, int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        Cart cart = findCartByUserId(userId);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (product.getStockQuantity() < qty) {
            throw new RuntimeException("Not enough stock");
        }

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + qty);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(qty);
            cart.getItems().add(newItem);
        }

        return toDto(cartRepo.save(cart));
    }

    @Transactional
    public CartDto removeFromCart(Long userId, Long productId) {
        Cart cart = findCartByUserId(userId);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        return toDto(cartRepo.save(cart));
    }

    private Cart findCartByUserId(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return cartRepo.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));
    }

    private CartDto toDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cartDto.setUserId(cart.getUser().getId());
        cartDto.setItems(cart.getItems().stream().map(this::toDto).collect(Collectors.toList()));
        return cartDto;
    }

    private CartItemDto toDto(CartItem cartItem) {
        CartItemDto itemDto = new CartItemDto();
        itemDto.setId(cartItem.getId());
        itemDto.setProductId(cartItem.getProduct().getId());
        itemDto.setProductName(cartItem.getProduct().getName());
        itemDto.setQuantity(cartItem.getQuantity());
        itemDto.setPrice(cartItem.getProduct().getPrice());
        return itemDto;
    }
}