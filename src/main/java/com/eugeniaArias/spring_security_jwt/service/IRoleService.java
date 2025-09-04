package com.eugeniaArias.spring_security_jwt.service;


import com.eugeniaArias.spring_security_jwt.dto.response.RoleResponseDto;
import com.eugeniaArias.spring_security_jwt.entity.Role;

import java.util.List;
import java.util.Set;

public interface IRoleService {
    //Basic CRUD
    public List<RoleResponseDto>finAllRolesDto();
    List<Role>findAll();
    Role findById(Long id);
    void deleteById(Long id);
    Role update(Long id,Role role);
    Role createRole(String roleName);
    Role save(Role role);
    //Specific Role's Methods
    Set<Role>findByIds(Set<Long>ids);
    Role findByName(String roleName);
    Role createRoleWithPermissions(String roleName, Set<Long>permissionsIds);
    Role removePermissionsFromRole(String roleName, Set<Long>permissionsIds);
    boolean existsByName(String roleName);
    long count();

}
