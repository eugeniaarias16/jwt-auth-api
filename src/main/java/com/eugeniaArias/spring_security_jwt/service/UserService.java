package com.eugeniaArias.spring_security_jwt.service;

import com.eugeniaArias.spring_security_jwt.dto.request.CreateUserRequestDto;
import com.eugeniaArias.spring_security_jwt.dto.response.UserNotPasswordResponseDto;
import com.eugeniaArias.spring_security_jwt.entity.Role;
import com.eugeniaArias.spring_security_jwt.entity.UserSec;
import com.eugeniaArias.spring_security_jwt.exceptions.ResourceNotFound;
import com.eugeniaArias.spring_security_jwt.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements IUserService{

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleService roleService;

    //Public Methods
    @Override
    public List<UserNotPasswordResponseDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserNotPasswordResponseDto::fromEntity)
                .toList();

    }


    //Internal Methods
    @Override
    public List<UserSec> findAll() {
        return userRepository.findAll();
    }



    @Override
    public UserSec findById(Long id) {
       UserSec userSec=userRepository.findById(id)
               .orElseThrow(()->new ResourceNotFound("User with id"+id+" not found."));
       return userSec;
    }

    @Override
    @Transactional
    public UserSec save(UserSec userSec) {
        UserSec savedUser=userRepository.save(userSec);
        log.debug("User successfully saved: {}",savedUser);
        return savedUser;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        findById(id);
        log.debug("Deleting user with id {} ...",id);
        userRepository.deleteById(id);
        log.debug("User successfully deleted.");
    }

    @Override
    @Transactional
    public UserSec update(Long userId, Map<String, Object> updates) {
        UserSec existingUser = findById(userId);

        updates.forEach((key,value)->{
            switch (key){
                case "username"->{
                    String newUsername=(String) value;
                    if(!existingUser.getUsername().equals(newUsername) && existsByUserName(newUsername)){
                        throw new IllegalArgumentException("Username already exists: " + newUsername);
                    }
                    existingUser.setUsername(newUsername);
                }
                case "enabled"->existingUser.setEnabled((Boolean)value);
                case "accountNotExpired"->existingUser.setAccountNotExpired((Boolean)value);
                case "accountNotLocked"-> existingUser.setAccountNotLocked((Boolean)value);
                case "credentialNotExpired"->existingUser.setCredentialNotExpired((Boolean)value);
                default -> log.warn("Field {} not recognized.",key);
            }
        });

        return save(existingUser);
    }

    @Override
    @Transactional
    public String encryptPassword(String password) {
        log.debug("Encrypting password...");
        return passwordEncoder.encode(password);

    }

    @Override
    public UserSec findByUserName(String username) {
        UserSec userSec=userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User "+username+" not found."));
        return userSec;
    }

    @Override
    @Transactional
    public UserSec createUserWithRoles(CreateUserRequestDto userRequestDto) {
        if(existsByUserName(userRequestDto.username())){
            throw new IllegalArgumentException("Username already exists: "+userRequestDto.username());
        }
        UserSec newUser= new UserSec();
        newUser.setUsername(userRequestDto.username());
        newUser.setPassword(encryptPassword(userRequestDto.password()));
        Set<Role>roleSet=roleService.findByIds(userRequestDto.rolesIds());
        if(roleSet.isEmpty()){
            throw new IllegalArgumentException("The roles list must have at least one id valid");
        }
        newUser.setRoleList(roleSet);
        newUser.setEnabled(true);
        newUser.setAccountNotLocked(true);
        newUser.setCredentialNotExpired(true);
        newUser.setAccountNotExpired(true);
        UserSec savedUser= save(newUser);
        log.debug("User {} created successfully.",savedUser.getUsername());
        return savedUser;


    }

    @Override
    @Transactional
    public void updatePassword(Long userId, String password, String newPassword) {
        UserSec existingUser=findById(userId);
        //verify if the password is correct
        if(!passwordEncoder.matches(password,existingUser.getPassword())){
            throw new IllegalArgumentException("Password Incorrect");
        }
        existingUser.setPassword(encryptPassword(newPassword));
        save(existingUser);
        log.info("User's password successfully updated.");
    }

    @Override
    @Transactional
    public void toggleUserStatus(Long userId) {
        UserSec existingUser=findById(userId);
        log.debug("Changing the status of user {} to status: {}",existingUser.getUsername(),existingUser.isEnabled()?"Disable":"Enable");
        existingUser.setEnabled(!existingUser.isEnabled());
        save(existingUser);
        log.info("User {} status changed to: {}", existingUser.getUsername(),
                existingUser.isEnabled() ? "ENABLED" : "DISABLED");
    }

    @Override
    public boolean existsByUserName(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public long countActiveUsers() {
       return userRepository.findAll().stream()
                .filter(UserSec::isEnabled)
                .count();
    }


}
