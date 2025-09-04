package com.eugeniaArias.spring_security_jwt.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordRequestDto(
        @NotBlank String password,
        @NotBlank String newPassword
) {
}
