package com.cotae.platform.userservice.aggregate.auth.application;

import com.cotae.platform.userservice.aggregate.auth.configurations.CookieProvider;
import com.cotae.platform.userservice.aggregate.auth.domain.RefreshTokenDomain;
import com.cotae.platform.userservice.aggregate.auth.dto.JwtDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final RefreshTokenDomain refreshTokenDomain;
    private final CookieProvider cookieProvider;

    @Override
    public JwtDto refreshToken(String accessToken, String refreshToken) {
        return this.refreshTokenDomain.refreshJwtToken(accessToken, refreshToken);
    }

    @Override
    public ResponseCookie logoutToken(String accessToken) {
        //Delete Refresh Tokens.
        log.info("Logout Token : {}",accessToken);
        this.refreshTokenDomain.logoutToken(accessToken);
        return this.cookieProvider.removeRefreshTokenCookie();
    }

    @Override
    public ResponseCookie createRefreshToken(String refreshToken) {
        return this.cookieProvider.createRefreshTokenCookie(refreshToken);
    }
}
