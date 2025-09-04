package com.eugeniaArias.spring_security_jwt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(ResourceNotFound.class)
    ResponseEntity<Map<String,String>>handleNotFound(ResourceNotFound ex){
    Map<String,String>error=new HashMap<>();
    error.put("error","Resource Not Found");
    error.put("message", ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
