package com.eugeniaArias.spring_security_jwt.service;

import com.eugeniaArias.spring_security_jwt.dto.request.AuthLoginRequestDto;
import com.eugeniaArias.spring_security_jwt.dto.request.AuthRegisterDto;
import com.eugeniaArias.spring_security_jwt.dto.response.AuthLoginResponseDto;
import com.eugeniaArias.spring_security_jwt.dto.response.UserNotPasswordResponseDto;
import com.eugeniaArias.spring_security_jwt.entity.Role;
import com.eugeniaArias.spring_security_jwt.entity.UserSec;
import com.eugeniaArias.spring_security_jwt.repository.IUserRepository;
import com.eugeniaArias.spring_security_jwt.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserDetailsServiceImp implements UserDetailsService {


    private final IUserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final IRoleService roleService;
    private final IPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Search user in BD
        UserSec userSec= userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username does not exist."));
        //Verify if the account is active
        if(!userSec.isEnabled()){
            log.warn("User {} is not enabled.",userSec.getUsername());
            throw new BadCredentialsException("User is not enabled.");
        }
        //Create authorities list( roles + permissions)
        List<GrantedAuthority>authorityList=new ArrayList<>();

        //Add roles with "ROLE_"
        userSec.getRoleList().forEach(role -> {
            String readRole="ROLE_"+role.getRoleName();
            authorityList.add(new SimpleGrantedAuthority(readRole));
            log.debug("Adding role {} for user {}",role.getRoleName(), username);
        });

        //Add Permissions
        userSec.getRoleList().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission-> permission.getPermissionName())
                .distinct()
                .forEach(permissionName-> {
                    authorityList.add(new SimpleGrantedAuthority(permissionName));
                    log.debug("Adding permission {} for user {}",permissionName,username);
                });


        //return user details
        return  new User(
                userSec.getUsername(),
                userSec.getPassword(),
                userSec.isEnabled(),
                userSec.isAccountNotExpired(),
                userSec.isCredentialNotExpired(),
                userSec.isAccountNotLocked(),
                authorityList
        );

    }

    private Authentication authentication(String username, String password){
        UserDetails userDetails= this.loadUserByUsername(username);
        if (userDetails==null){
            throw new BadCredentialsException("Username or Password incorrect");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Username or Password incorrect");
        }
        log.debug("Valid credentials for user {}", username);
        return  new UsernamePasswordAuthenticationToken(username,
                userDetails.getPassword(),
                userDetails.getAuthorities());
    }

    private Set<Role> assignRoleToNewUser(){
        long userCount = userRepository.count();
        if(userCount == 0){
            log.info("Creating first user with ADMIN role");
            return Set.of(roleService.findByName("ADMIN"));
        } else {
            log.debug("Creating regular user with USER role");
            return Set.of(roleService.findByName("USER"));
        }
    }
    @Transactional
    public UserNotPasswordResponseDto registerUser(AuthRegisterDto authRegisterDto){
        String username=authRegisterDto.username();
        String password=authRegisterDto.password();

        if(userRepository.existsByUsername(username)){
            throw new IllegalArgumentException("Username already exists: "+username);

        }
        UserSec newUser= new UserSec();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setEnabled(true);
        newUser.setAccountNotLocked(true);
        newUser.setCredentialNotExpired(true);
        newUser.setAccountNotExpired(true);
        newUser.setRoleList(assignRoleToNewUser());
        UserSec savedUser=userRepository.save(newUser);
        return UserNotPasswordResponseDto.fromEntity(savedUser);

    }
    @Transactional
    public AuthLoginResponseDto loginUser(AuthLoginRequestDto authLoginRequestDto){

        String username= authLoginRequestDto.username();
        String password= authLoginRequestDto.password();
        try {
            Authentication authentication= this.authentication(username,password);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken=jwtUtils.createToken(authentication);
            log.info("Successful login for user {} ",username);
            return new AuthLoginResponseDto(username,"Successful Login",accessToken);
        }catch (Exception exception){
            log.error("Login error for user {}- {}",username,exception.getMessage());
            throw exception;
        }

    }

}
