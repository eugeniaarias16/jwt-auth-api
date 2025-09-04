package com.eugeniaArias.spring_security_jwt.service;

import com.eugeniaArias.spring_security_jwt.dto.response.RoleResponseDto;
import com.eugeniaArias.spring_security_jwt.entity.Permission;
import com.eugeniaArias.spring_security_jwt.entity.Role;
import com.eugeniaArias.spring_security_jwt.exceptions.ResourceNotFound;
import com.eugeniaArias.spring_security_jwt.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService implements  IRoleService{

 private final IRoleRepository roleRepository;
 private final IPermissionService permissionService;

    public List<RoleResponseDto>finAllRolesDto(){
        return findAll().stream()
                .map(RoleResponseDto::fromEntity)
                .toList();
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findById(Long id) {
        Role role=roleRepository.findById(id)
                .orElseThrow(()->new ResourceNotFound("Role with id "+id+" not found"));
        return role;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        findById(id);
        log.debug("Deleting role with id: {}", id);
        roleRepository.deleteById(id);
        log.info("Role successfully deleted");

    }

    @Override
    @Transactional
    public Role update(Long id,Role role) {
        Role existingRole= findById(id);
        if(!existingRole.getRoleName().equals(role.getRoleName()) && existsByName(role.getRoleName())){
            throw new IllegalArgumentException("Role with name: "+ role.getRoleName()+" already exists.");
        }
        existingRole.setRoleName(role.getRoleName().toUpperCase());
        Role savedRole=save(existingRole);
        log.info("Role successfully updated");
        return savedRole;
    }


    @Override
    @Transactional
    public Role createRole(String roleName) {
        log.debug("Creating new Role: {}",roleName);
        if(existsByName(roleName)){
            throw new IllegalArgumentException("Role already exists.");
        }
        Role newRole= new Role();
        newRole.setRoleName(roleName.toUpperCase());
        Role savedRole=save(newRole);
        log.info("New Role created: {}",savedRole);
        return savedRole;
    }

    @Override
    @Transactional
    public Role save(Role role) {
        Role savedRole=roleRepository.save(role);
        log.debug("Role {} successfully saved", role.getRoleName());
        return savedRole;
    }



    @Override
    public Set<Role> findByIds(Set<Long> ids) {
        List<Role>roleSet=roleRepository.findAllById(ids);
        if(roleSet.size()!=ids.size()){
            Set<Long>rolesIdsFounds= roleSet.stream()
                    .map(Role::getId)
                    .collect(Collectors.toSet());
            Set<Long>idsNotFound=ids.stream()
                    .filter(id->!rolesIdsFounds.contains(id))
                    .collect(Collectors.toSet());
            log.debug("These ids:{} not found.",idsNotFound);
        }
        return new HashSet<>(roleSet);
    }

    @Override
    public Role findByName(String roleName) {
        Role role=roleRepository.findByRoleName(roleName)
                .orElseThrow(()-> new IllegalArgumentException("Role "+roleName+" not found."));
        return role;
    }

    @Override
    @Transactional
    public Role createRoleWithPermissions(String roleName, Set<Long> permissionsIds) {
        Role newRole= createRole(roleName);
        Set<Permission> permissionSet=permissionService.findByIds(permissionsIds);
        if(permissionSet.isEmpty()){
            throw new IllegalArgumentException("No valid permission were found for the role: "+roleName);
        }
        newRole.setPermissions(permissionSet);
        Role savedRole=save(newRole);
        log.info("New Role {} was successfully created with permissions: {}",roleName, permissionSet);
        return savedRole;

    }

    @Override
    @Transactional
    public Role removePermissionsFromRole(String roleName, Set<Long> permissionsIds) {
       Role existingRole=findByName(roleName);
       Set<Permission>permissionsToRemove=permissionService.findByIds(permissionsIds);
       if ((permissionsToRemove.isEmpty())){
           throw new IllegalArgumentException("No permissions found to remove");
       }
       existingRole.getPermissions().removeAll(permissionsToRemove);
       log.info("Permissions:{} remove from role {}",permissionsToRemove,roleName);
       Role savedRole=save(existingRole);
       log.debug("Role {} updated {}",roleName,savedRole);
       return savedRole;

    }

    @Override
    public boolean existsByName(String roleName) {
        return roleRepository.existsByRoleName(roleName);
    }

    @Override
    public long count() {
        return roleRepository.count();
    }
}
