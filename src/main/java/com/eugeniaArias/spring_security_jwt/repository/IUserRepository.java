package com.eugeniaArias.spring_security_jwt.repository;

import com.eugeniaArias.spring_security_jwt.entity.UserSec;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserSec,Long> {

    Optional<UserSec> findByUsername(String username);
    boolean existsByUsername(String name);

}

