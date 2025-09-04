package com.eugeniaArias.spring_security_jwt.controller;

import com.eugeniaArias.spring_security_jwt.dto.request.AuthLoginRequestDto;
import com.eugeniaArias.spring_security_jwt.dto.request.AuthRegisterDto;
import com.eugeniaArias.spring_security_jwt.dto.response.AuthLoginResponseDto;
import com.eugeniaArias.spring_security_jwt.dto.response.UserNotPasswordResponseDto;
import com.eugeniaArias.spring_security_jwt.security.utils.SystemInitializer;
import com.eugeniaArias.spring_security_jwt.service.IRoleService;
import com.eugeniaArias.spring_security_jwt.service.UserDetailsServiceImp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final UserDetailsServiceImp userDetailsServiceImp;
    private final SystemInitializer systemInitializer;


    @PostMapping("/register")
    public ResponseEntity<UserNotPasswordResponseDto> register(@RequestBody AuthRegisterDto authRegisterDto){
        log.info("Attempting to register new user: {}", authRegisterDto.username());


        try {
            // Register user
            UserNotPasswordResponseDto responseDto = userDetailsServiceImp.registerUser(authRegisterDto);
            log.info("User {} registered successfully", authRegisterDto.username());

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

        } catch (Exception ex) {
            log.error("Registration failed for user {}: {}", authRegisterDto.username(), ex.getMessage());
            throw ex;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponseDto>login(@RequestBody AuthLoginRequestDto authLoginRequestDto){
        AuthLoginResponseDto responseDto=userDetailsServiceImp.loginUser(authLoginRequestDto);
        return ResponseEntity.ok(responseDto);
    }

}
