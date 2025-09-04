package com.eugeniaArias.spring_security_jwt.repository;

import com.eugeniaArias.spring_security_jwt.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface IRoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByRoleName(String roleName);
    boolean existsByRoleName(String roleName);

}
