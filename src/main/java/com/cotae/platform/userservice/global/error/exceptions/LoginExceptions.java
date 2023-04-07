package com.cotae.platform.userservice.global.error.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LoginExceptions extends RuntimeException{
    private final LoginErrorCodeImpl errorCode;
    private final List<ArgumentError> errors;

    public LoginExceptions(LoginErrorCodeImpl errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errors = new ArrayList<>();
    }

    public LoginExceptions(LoginErrorCodeImpl errorCode, List<ArgumentError> errors){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errors = errors;
    }

    public LoginExceptions(String message,LoginErrorCodeImpl errorCode){
        super(message);
        this.errorCode = errorCode;
        this.errors = new ArrayList<>();
    }

    public LoginExceptions(String message,LoginErrorCodeImpl errorCode, List<ArgumentError> errors){
        super(message);
        this.errorCode = errorCode;
        this.errors = errors;
    }
}