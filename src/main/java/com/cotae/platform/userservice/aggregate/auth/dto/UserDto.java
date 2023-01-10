package com.cotae.platform.userservice.aggregate.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String password;
    private String email;
    protected String dtype;
}