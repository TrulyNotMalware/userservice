package com.cotae.platform.userservice.global.error.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LoginErrorCodeImpl implements ErrorCode{
    USER_ID_NOT_VALID(HttpStatus.BAD_REQUEST,"User ID is invalid.");

    private final HttpStatus httpStatus;
    private final String message;
}
