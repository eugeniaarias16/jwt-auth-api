package com.eugeniaArias.spring_security_jwt.controller;

import com.eugeniaArias.spring_security_jwt.dto.request.RoleWithPermissionsRequestDto;
import com.eugeniaArias.spring_security_jwt.dto.response.RoleResponseDto;
import com.eugeniaArias.spring_security_jwt.entity.Role;
import com.eugeniaArias.spring_security_jwt.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleService roleService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<RoleResponseDto>>getAllRoles(){
        List<RoleResponseDto>rolesDto=roleService.finAllRolesDto();
        return ResponseEntity.ok(rolesDto);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<RoleResponseDto>getById(@PathVariable Long id){
        Role role=roleService.findById(id);
        return ResponseEntity.ok(RoleResponseDto.fromEntity(role));
    }

    @GetMapping("/name/{roleName}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<RoleResponseDto>getByName(@PathVariable String roleName){
        Role role=roleService.findByName(roleName);
        return ResponseEntity.ok(RoleResponseDto.fromEntity(role));
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<String>count(){
        long count=roleService.count();
        String response= "There are "+count+" roles.";
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/id/{id}")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<String>deleteById(@PathVariable Long id){
        roleService.deleteById(id);
        return ResponseEntity.ok("Role successfully deleted.");
    }

    @PostMapping("/create/{roleName}")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<RoleResponseDto>createRole(@PathVariable String roleName){
        Role role=roleService.createRole(roleName);
        return ResponseEntity.ok(RoleResponseDto.fromEntity(role));
    }

    @PostMapping("/createWithPermissions")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<RoleResponseDto>createRoleWithPermissions(@RequestBody RoleWithPermissionsRequestDto dto){
        Role role= roleService.createRoleWithPermissions(dto.roleName(), dto.permissionIds());
        return ResponseEntity.ok(RoleResponseDto.fromEntity(role));
    }

    @PatchMapping("/removePermissions")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<RoleResponseDto>removePermission(@RequestBody RoleWithPermissionsRequestDto dto){
        Role role=roleService.removePermissionsFromRole(dto.roleName(), dto.permissionIds());
        return ResponseEntity.ok(RoleResponseDto.fromEntity(role));
    }
}
