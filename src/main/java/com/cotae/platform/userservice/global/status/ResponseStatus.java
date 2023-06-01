package com.cotae.platform.userservice.global.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseStatus implements Status{
    SUCCESSFULLY_WORKED(HttpStatus.OK,"success");

    private final HttpStatus httpStatus;
    private final String compactStatusCode;
}
