package com.ecommerce.service;

import com.ecommerce.Repository.CartRepository;
import com.ecommerce.Repository.OrderRepository;
import com.ecommerce.Repository.ProductRepository;
import com.ecommerce.Repository.UserRepository;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.dto.OrderItemDto;
import com.ecommerce.entity.*;
import com.ecommerce.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepo;
    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    @Transactional
    public OrderDto createOrder(Long userId, String address) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(address);
        order.setStatus("PENDING");

        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem ci : cart.getItems()) {
            Product product = ci.getProduct();

            if (product.getStockQuantity() < ci.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            product.setStockQuantity(product.getStockQuantity() - ci.getQuantity());
            productRepo.save(product);

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(product);
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(product.getPrice());

            total += product.getPrice() * ci.getQuantity();
            orderItems.add(oi);
        }

        order.setItems(orderItems);
        order.setTotalPrice(total);

        Order savedOrder = orderRepo.save(order);

        cart.getItems().clear();
        cartRepo.save(cart);

        return toDto(savedOrder);
    }

    private OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setItems(order.getItems().stream().map(this::toDto).collect(Collectors.toList()));
        dto.setTotalPrice(order.getTotalPrice());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setStatus(order.getStatus());
        return dto;
    }

    private OrderItemDto toDto(OrderItem orderItem) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(orderItem.getId());
        dto.setProductId(orderItem.getProduct().getId());
        dto.setProductName(orderItem.getProduct().getName());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        return dto;
    }
}