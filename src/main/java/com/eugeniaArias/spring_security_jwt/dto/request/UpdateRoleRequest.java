package com.eugeniaArias.spring_security_jwt.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record UpdateRoleRequest(
        @NotBlank String roleName,
        Set<Long>permissionsIds
) {
}
