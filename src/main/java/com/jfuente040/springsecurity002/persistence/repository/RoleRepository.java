package com.jfuente040.springsecurity002.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.jfuente040.springsecurity002.persistence.entity.RoleEntity;
import com.jfuente040.springsecurity002.persistence.entity.RoleEnum;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleName(RoleEnum roleName);
}
