package com.cotae.platform.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class JwtDto {
    private String accessToken;
    private String refreshToken;
    private Date accessTokenExpiredDate;
}
