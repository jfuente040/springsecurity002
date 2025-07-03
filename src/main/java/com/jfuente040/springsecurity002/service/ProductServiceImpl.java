package com.jfuente040.springsecurity002.service;

import org.springframework.stereotype.Service;

import com.jfuente040.springsecurity002.persistence.entity.ProductEntity;
import com.jfuente040.springsecurity002.persistence.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductEntity> findAll() {
        return (List<ProductEntity>) productRepository.findAll();
    }

    @Override
    public Optional<ProductEntity> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public ProductEntity save(ProductEntity product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
