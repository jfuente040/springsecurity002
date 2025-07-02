package com.jfuente040.springsecurity001.service;

import com.jfuente040.springsecurity001.persistence.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductEntity> findAll();
    Optional<ProductEntity> findById(Long id);
    ProductEntity save(ProductEntity product);
    void deleteById(Long id);
}
