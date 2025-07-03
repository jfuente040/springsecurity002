package com.jfuente040.springsecurity002.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.jfuente040.springsecurity002.persistence.entity.PermissionEntity;
import com.jfuente040.springsecurity002.persistence.entity.PermissionEnum;

import java.util.Optional;

public interface PermissionRepository extends CrudRepository<PermissionEntity, Long> {
    Optional<PermissionEntity> findByPermissionName(PermissionEnum permissionName);
}
