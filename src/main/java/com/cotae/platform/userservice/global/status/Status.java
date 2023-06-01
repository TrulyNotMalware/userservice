package com.cotae.platform.userservice.global.status;

import org.springframework.http.HttpStatus;

public interface Status {
    HttpStatus getHttpStatus();
    String getCompactStatusCode();
    String name();
}
