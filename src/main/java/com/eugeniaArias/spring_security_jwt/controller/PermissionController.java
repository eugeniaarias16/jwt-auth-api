package com.eugeniaArias.spring_security_jwt.controller;

import com.eugeniaArias.spring_security_jwt.dto.response.PermissionResponseDto;
import com.eugeniaArias.spring_security_jwt.entity.Permission;
import com.eugeniaArias.spring_security_jwt.service.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final IPermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<PermissionResponseDto>>getAll(){
        List<PermissionResponseDto>permissions=permissionService.findAllPermissionsDto();
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<PermissionResponseDto>getById(@PathVariable Long id){
        Permission permission=permissionService.findById(id);
        return ResponseEntity.ok(PermissionResponseDto.fromEntity(permission));
    }

    @GetMapping("/name/{permissionName}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<PermissionResponseDto>getByName(@PathVariable String permissionName){
        Permission permission=permissionService.findByName(permissionName);
        return ResponseEntity.ok(PermissionResponseDto.fromEntity(permission));
    }


    @DeleteMapping("/id/{id}")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<String>deleteById(@PathVariable Long id){
        permissionService.deleteById(id);
        return ResponseEntity.ok("Permission successfully deleted.");
    }

    @PostMapping("/createBasicPermissions")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<String>createBasicPermissions(){
        permissionService.createBasicPermission();
        return ResponseEntity.ok("Basic Permissions Created.");
    }
    @PostMapping("/create/{permissionName}")
    public ResponseEntity<PermissionResponseDto>create(@PathVariable String permissionName){
        Permission permission=permissionService.create(permissionName);
        return ResponseEntity.ok(PermissionResponseDto.fromEntity(permission));
    }

    @PatchMapping("/update/id/{id}/{newPermissionName}")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<PermissionResponseDto>update(@PathVariable Long id,
                                                       @PathVariable String newPermissionName){
        Permission permission=permissionService.update(id,newPermissionName);
        return ResponseEntity.ok(PermissionResponseDto.fromEntity(permission));

    }


}
