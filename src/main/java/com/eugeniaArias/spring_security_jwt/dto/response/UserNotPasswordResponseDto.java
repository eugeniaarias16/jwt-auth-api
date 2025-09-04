package com.eugeniaArias.spring_security_jwt.dto.response;

import com.eugeniaArias.spring_security_jwt.entity.Role;
import com.eugeniaArias.spring_security_jwt.entity.UserSec;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public record UserNotPasswordResponseDto(
        Long id,
        String username,
        boolean enabled,
        boolean accountNotExpired,
        boolean accountNotLocked,
        boolean credentialNotExpired,
        Set<String> roleNames // only the role's names, no the object
) {

    public static UserNotPasswordResponseDto fromEntity(UserSec userSec){
        Set<String>rolesNamesList=userSec.getRoleList()
                .stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());
        return new UserNotPasswordResponseDto(
                userSec.getId(),
                userSec.getUsername(),
                userSec.isEnabled(),
                userSec.isAccountNotExpired(),
                userSec.isAccountNotLocked(),
                userSec.isCredentialNotExpired(),
                rolesNamesList
        );
    }
}
