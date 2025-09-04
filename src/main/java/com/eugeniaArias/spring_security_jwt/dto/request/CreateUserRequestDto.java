package com.eugeniaArias.spring_security_jwt.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record CreateUserRequestDto(
       @NotBlank String username,
       @NotBlank String password,
       @NotBlank Set<Long>rolesIds) {
}
