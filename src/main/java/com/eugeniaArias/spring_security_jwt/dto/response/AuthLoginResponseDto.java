package com.eugeniaArias.spring_security_jwt.dto.response;

public record AuthLoginResponseDto(
        String username,
        String message,
        String token
) {
}
