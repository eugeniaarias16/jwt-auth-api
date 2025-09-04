package com.eugeniaArias.spring_security_jwt.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequestDto(
      @NotBlank String username,
      @NotBlank  String password
) { }
