package com.eugeniaArias.spring_security_jwt.service;

import com.eugeniaArias.spring_security_jwt.dto.response.PermissionResponseDto;
import com.eugeniaArias.spring_security_jwt.entity.Permission;

import java.util.List;
import java.util.Set;

public interface IPermissionService {
    //Basic CRUD
    List<Permission>findAll();
    List<PermissionResponseDto>findAllPermissionsDto();
    Permission findById(Long id);
    void deleteById(Long id);
    Permission update(Long id, String permissionName);
    Permission save(Permission permission);
    Permission create(String permissionName);

    //Specific Permission's Methods
    Set<Permission>findByIds(Set<Long>permissionIds);
    Permission findByName(String permissionName);
    void createBasicPermission();
    boolean existsByName(String permissionName);

}
