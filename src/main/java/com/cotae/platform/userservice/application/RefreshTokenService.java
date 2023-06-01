package com.cotae.platform.userservice.application;

import com.cotae.platform.userservice.dto.JwtDto;
import org.springframework.http.ResponseCookie;

public interface RefreshTokenService {

    //RefreshToken
    ResponseCookie createRefreshToken(String refreshToken);
    JwtDto refreshToken(String accessToken, String refreshToken);
    ResponseCookie logoutToken(String accessToken);
}
