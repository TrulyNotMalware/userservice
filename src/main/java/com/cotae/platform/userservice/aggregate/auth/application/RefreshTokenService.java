package com.cotae.platform.userservice.aggregate.auth.application;

import com.cotae.platform.userservice.aggregate.auth.dto.JwtDto;
import org.springframework.http.ResponseCookie;

public interface RefreshTokenService {

    //RefreshToken
    ResponseCookie createRefreshToken(String refreshToken);
    JwtDto refreshToken(String accessToken, String refreshToken);
    ResponseCookie logoutToken(String accessToken);
}
