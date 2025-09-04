package com.eugeniaArias.spring_security_jwt.repository;

import com.eugeniaArias.spring_security_jwt.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface IPermissionRepository extends JpaRepository<Permission,Long> {

    Optional<Permission> findByPermissionName(String permissionName);
    boolean existsByPermissionName(String permissionName);
}
