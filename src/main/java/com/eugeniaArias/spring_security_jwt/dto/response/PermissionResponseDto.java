package com.eugeniaArias.spring_security_jwt.dto.response;

import com.eugeniaArias.spring_security_jwt.entity.Permission;

public record PermissionResponseDto(
        Long id,
        String permissionName
) {
    public static PermissionResponseDto fromEntity(Permission permission){
        return new PermissionResponseDto(
                permission.getId(),
                permission.getPermissionName()
        );
    }
}
