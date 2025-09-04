package com.eugeniaArias.spring_security_jwt.service;

import com.eugeniaArias.spring_security_jwt.dto.response.PermissionResponseDto;
import com.eugeniaArias.spring_security_jwt.entity.Permission;
import com.eugeniaArias.spring_security_jwt.exceptions.ResourceNotFound;
import com.eugeniaArias.spring_security_jwt.repository.IPermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PermissionService implements IPermissionService{

    private final IPermissionRepository permissionRepository;


    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public List<PermissionResponseDto> findAllPermissionsDto() {
        return findAll().stream()
                .map(PermissionResponseDto::fromEntity)
                .toList();
    }

    @Override
    public Permission findById(Long id) {
        Permission permission= permissionRepository.findById(id)
                .orElseThrow(()->new ResourceNotFound("Permission with id "+id+" not found"));
        return permission;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        findById(id); // ensure that it exists before deleting it
        log.debug("Deleting permission with id: {}", id);
        permissionRepository.deleteById(id);
        log.debug("Permission successfully deleted.");
    }

    @Override
    @Transactional
    public Permission update(Long id, String permissionName) {
        Permission existingPermission=findById(id);
        log.debug("Updating permission with id: {}",id);
        if(existsByName(permissionName)){
            throw new IllegalArgumentException("Permission already exists: "+permissionName);
        }
        existingPermission.setPermissionName(permissionName);
        return save(existingPermission);
    }

    @Override
    @Transactional
    public Permission save(Permission permission) {
       Permission savedPermission= permissionRepository.save(permission);
        log.debug("Permission {} successfully saved",permission.getPermissionName());
        return savedPermission;
    }

    @Override
    @Transactional
    public Permission create(String permissionName) {
        if(existsByName(permissionName)){
            throw new IllegalArgumentException("Permission "+permissionName+" already exists");
        }
        Permission newPermission= new Permission();
        newPermission.setPermissionName(permissionName);
        return save(newPermission);

    }

    @Override
    public Set<Permission> findByIds(Set<Long> permissionIds) {
        List<Permission> permissionList = permissionRepository.findAllById(permissionIds);

        if(permissionList.size() != permissionIds.size()){
            Set<Long> permissionIdsFound = permissionList.stream()
                    .map(Permission::getId)
                    .collect(Collectors.toSet());
            Set<Long> permissionIdsNotFound = permissionIds.stream()
                    .filter(id -> !permissionIdsFound.contains(id))
                    .collect(Collectors.toSet());
            log.warn("These ids: {} not found", permissionIdsNotFound);
        }

        // Convert List to Set
        return new HashSet<>(permissionList);
    }

    @Override
    public Permission findByName(String permissionName) {
        Permission permission=permissionRepository.findByPermissionName(permissionName)
                .orElseThrow(()->new ResourceNotFound("Permission "+permissionName+" not found."));
        return permission;
    }

    @Override
    @Transactional
    public void createBasicPermission() {
        String[] crudPermission={"CREATE","READ","UPDATE","DELETE"};
        for(String perName:crudPermission){
            if(!existsByName(perName)){
                Permission newPermission= new Permission();
                newPermission.setPermissionName(perName);
                save(newPermission);
                log.debug("Permission {} successfully created",perName);
            }
        }
        log.debug("Basic Permissions Created.");
    }

    @Override
    public boolean existsByName(String permissionName) {
        return permissionRepository.existsByPermissionName(permissionName);
    }
}
