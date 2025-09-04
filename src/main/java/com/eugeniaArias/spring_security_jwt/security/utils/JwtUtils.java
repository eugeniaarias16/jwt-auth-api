package com.eugeniaArias.spring_security_jwt.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    //Variable settings
    @Value("${security.jwt.private.key}")
    private String privateKey;
    @Value("${security.jwt.user.generator}")
    private String generatorUser;

    private Algorithm algorithm;

    @PostConstruct
    public void initAlgorithm(){
        this.algorithm= Algorithm.HMAC256(privateKey);
    }

    //Create Token
    public String createToken(Authentication authentication){

        //get username
        String username = authentication.getName();

        //convert authorities to comma separated string
        String authorities=authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));


        Date today= new Date();
        //create JWT token
        return JWT.create()
                .withIssuer(generatorUser)
                .withSubject(username)
                .withClaim("authorities",authorities)
                .withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(today)
                .withNotBefore(today)
                .withExpiresAt(toMills(30))
                .sign(algorithm);

    }

    //Validate Token
    public DecodedJWT validateToken(String token){
        try {
            JWTVerifier verifier=JWT.require(this.algorithm)
                    .withIssuer(generatorUser)
                    .build();
            return verifier.verify(token);
        }catch (JWTVerificationException  exception){
            throw new JWTVerificationException("Invalid Token.");
        }

    }

    //Extract Methods
    public String extractUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject();

    }
    public Claim extractSpecificClaim(DecodedJWT decodedJWT, String claimName){
        return  decodedJWT.getClaim(claimName);

    }    //Convert date to mills
    public Date toMills(Integer min){
        int minutes=(min!=null)?min:0;
        int minutesToMills=minutes*60000;
        return new Date(System.currentTimeMillis()+minutesToMills);

    }


}
