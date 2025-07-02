package com.jfuente040.springsecurity001.persistence.repository;

import com.jfuente040.springsecurity001.persistence.entity.RoleEntity;
import com.jfuente040.springsecurity001.persistence.entity.RoleEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleName(RoleEnum roleName);
}
