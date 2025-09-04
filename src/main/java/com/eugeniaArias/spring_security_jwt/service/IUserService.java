package com.eugeniaArias.spring_security_jwt.service;

import com.eugeniaArias.spring_security_jwt.dto.request.CreateUserRequestDto;
import com.eugeniaArias.spring_security_jwt.dto.response.UserNotPasswordResponseDto;
import com.eugeniaArias.spring_security_jwt.entity.UserSec;

import java.util.*;

public interface IUserService {


    //Public Methods
    List<UserNotPasswordResponseDto>findAllUsers();



    // Basic CRUD
    List<UserSec> findAll();
    UserSec findById(Long id);
    UserSec save(UserSec userSec);
    void deleteById(Long id);
    UserSec update(Long userId, Map<String, Object> updates);

    //Specific User's Methods
    String encryptPassword(String password);
    UserSec findByUserName(String username);
    UserSec createUserWithRoles(CreateUserRequestDto userRequestDto);
    void updatePassword(Long userId, String password,String newPassword);
    void toggleUserStatus(Long userId);
    boolean existsByUserName(String username);
    long countActiveUsers();


}
