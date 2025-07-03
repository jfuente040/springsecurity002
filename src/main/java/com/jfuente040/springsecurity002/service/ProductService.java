package com.jfuente040.springsecurity002.service;

import java.util.List;
import java.util.Optional;

import com.jfuente040.springsecurity002.persistence.entity.ProductEntity;

public interface ProductService {
    List<ProductEntity> findAll();
    Optional<ProductEntity> findById(Long id);
    ProductEntity save(ProductEntity product);
    void deleteById(Long id);
}
