package com.eugeniaArias.spring_security_jwt.dto.response;

import com.eugeniaArias.spring_security_jwt.entity.Permission;
import com.eugeniaArias.spring_security_jwt.entity.Role;

import java.util.Set;
import java.util.stream.Collectors;

public record RoleResponseDto(
        Long id,
        String roleName,
        Set<String>permissionsName
) {
    public static RoleResponseDto fromEntity(Role role){
        Set<String>permissionsName= role.getPermissions().stream()
                .map(Permission::getPermissionName)
                .collect(Collectors.toSet());
        return new RoleResponseDto(
                role.getId(),
                role.getRoleName(),
                permissionsName
        );

    }

}
