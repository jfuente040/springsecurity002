package com.jfuente040.springsecurity002.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.jfuente040.springsecurity002.persistence.entity.ProductEntity;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
}
