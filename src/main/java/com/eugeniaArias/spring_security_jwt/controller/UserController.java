package com.eugeniaArias.spring_security_jwt.controller;

import com.eugeniaArias.spring_security_jwt.dto.request.CreateUserRequestDto;
import com.eugeniaArias.spring_security_jwt.dto.request.UpdatePasswordRequestDto;
import com.eugeniaArias.spring_security_jwt.dto.response.UserNotPasswordResponseDto;
import com.eugeniaArias.spring_security_jwt.entity.UserSec;
import com.eugeniaArias.spring_security_jwt.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final IUserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<UserNotPasswordResponseDto>>getAllUsers(){
        List<UserNotPasswordResponseDto>userList=userService.findAllUsers();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<UserNotPasswordResponseDto>getById(@PathVariable Long id){
        UserSec userSec=userService.findById(id);
        return ResponseEntity.ok(UserNotPasswordResponseDto.fromEntity(userSec));
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<UserNotPasswordResponseDto> getByUsername(@PathVariable String username){
        UserSec userSec=userService.findByUserName(username);
        return ResponseEntity.ok(UserNotPasswordResponseDto.fromEntity(userSec));

    }

    @GetMapping("/active-count")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<String>countActiveUsers(){
        Long countsActive=userService.countActiveUsers();
        String response="There are "+countsActive+" active.";
        return  ResponseEntity.ok(response);

    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<String>deleteById(@PathVariable Long id){
        userService.deleteById(id);
       return ResponseEntity.ok("User successfully deleted.");
    }

    @PutMapping("updatePassword/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<String>updatePassword(@PathVariable Long id,
                                                @RequestBody UpdatePasswordRequestDto dto){
        userService.updatePassword(id,dto.password(),dto.newPassword());
        return ResponseEntity.ok("Password successfully updated.");
    }

    @PostMapping("/createWithRoles")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<UserNotPasswordResponseDto>createUserWithRoles(@RequestBody CreateUserRequestDto createUserRequestDto){
        UserSec userSec=userService.createUserWithRoles(createUserRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserNotPasswordResponseDto.fromEntity(userSec));

    }

    @PatchMapping("/toggleUserStatus/{id}")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<String>toggleUserStatus(@PathVariable Long id){
        userService.toggleUserStatus(id);
        return ResponseEntity.ok("Status successfully modify.");
    }
    @PatchMapping("update/{id}")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<UserNotPasswordResponseDto>updateUSer(@PathVariable Long id,
                                                                @RequestBody Map<String, Object> updates){
        UserSec userSec=userService.update(id,updates);
        return ResponseEntity.ok(UserNotPasswordResponseDto.fromEntity(userSec));

    }


}
