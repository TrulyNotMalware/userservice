package com.cotae.platform.userservice.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
