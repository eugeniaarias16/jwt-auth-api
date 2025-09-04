package com.eugeniaArias.spring_security_jwt.security.utils;

import com.eugeniaArias.spring_security_jwt.service.IPermissionService;
import com.eugeniaArias.spring_security_jwt.service.IRoleService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j

public class SystemInitializer {
    private final IPermissionService permissionService;
    private final IRoleService roleService;

    /**
     * Initialize the system by creating basic permissions and roles if they do not exist.
     */
    @PostConstruct
    @Transactional
    public void initializeBasicData() {
        if (roleService.count() == 0) {
            log.info("Initializing basic system data (permissions and roles)");

            // Create basic permissions
            permissionService.createBasicPermission();

            // Create basic roles
            createBasicRoles();

            log.info("Basic system data initialized successfully");
        }
    }

    private void createBasicRoles() {
        //Obtain permission IDs
        Long createId = permissionService.findByName("CREATE").getId();
        Long readId = permissionService.findByName("READ").getId();
        Long updateId = permissionService.findByName("UPDATE").getId();
        Long deleteId = permissionService.findByName("DELETE").getId();

        // Create ADMIN role with all permissions
        if (!roleService.existsByName("ADMIN")) {
            Set<Long> adminPermissions = Set.of(createId, readId, updateId, deleteId);
            roleService.createRoleWithPermissions("ADMIN", adminPermissions);
            log.info("Created ADMIN role with full permissions");
        }

        // Create USER role only with READ
        if (!roleService.existsByName("USER")) {
            Set<Long> userPermissions = Set.of(readId);
            roleService.createRoleWithPermissions("USER", userPermissions);
            log.info("Created USER role with read permission");
        }
    }
}
