package com.jfuente040.springsecurity001.persistence.repository;

import com.jfuente040.springsecurity001.persistence.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
}
