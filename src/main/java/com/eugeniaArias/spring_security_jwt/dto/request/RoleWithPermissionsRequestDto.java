package com.eugeniaArias.spring_security_jwt.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record RoleWithPermissionsRequestDto(
        @NotBlank String roleName,
        @NotEmpty Set<Long>permissionIds
        ) {
}
