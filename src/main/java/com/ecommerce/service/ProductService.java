package com.ecommerce.service;

import com.ecommerce.Repository.ProductRepository;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;

    public ProductDto create(ProductDto productDto) {
        if (productDto.getPrice() <= 0) {
            throw new RuntimeException("Invalid price");
        }
        if (productDto.getStockQuantity() < 0) {
            throw new RuntimeException("Invalid stock");
        }
        Product product = toEntity(productDto);
        return toDto(repo.save(product));
    }

    public List<ProductDto> getAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public ProductDto getById(Long id) {
        return repo.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public ProductDto update(Long id, ProductDto updatedDto) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        p.setName(updatedDto.getName());
        p.setDescription(updatedDto.getDescription());
        p.setPrice(updatedDto.getPrice());
        p.setStockQuantity(updatedDto.getStockQuantity());
        p.setImageUrl(updatedDto.getImageUrl());

        return toDto(repo.save(p));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        repo.deleteById(id);
    }

    private Product toEntity(ProductDto dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setImageUrl(dto.getImageUrl());
        return product;
    }

    private ProductDto toDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setImageUrl(product.getImageUrl());
        return dto;
    }
}