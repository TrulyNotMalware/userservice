package com.cotae.platform.userservice.global.error.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCodeImpl implements ErrorCode{
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST,"Invalid Parameter included"),
    INVALID_ARGUMENT_TYPE(HttpStatus.BAD_REQUEST, "Invalid Parameter type included."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Request Method type is not allowed.")
    ;
    private final HttpStatus httpStatus;
    private final String message;
}