package com.jfuente040.springsecurity001.persistence.repository;

import com.jfuente040.springsecurity001.persistence.entity.PermissionEntity;
import com.jfuente040.springsecurity001.persistence.entity.PermissionEnum;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface PermissionRepository extends CrudRepository<PermissionEntity, Long> {
    Optional<PermissionEntity> findByPermissionName(PermissionEnum permissionName);
}
