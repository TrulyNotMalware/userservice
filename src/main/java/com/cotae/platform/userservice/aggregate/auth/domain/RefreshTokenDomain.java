package com.cotae.platform.userservice.aggregate.auth.domain;

import com.cotae.platform.userservice.aggregate.auth.dto.JwtDto;

public interface RefreshTokenDomain {
    /**
     * RefreshToken 을 Update 합니다.
     * @param id userId
     * @param uuid generated UUID
     */
    void updateRefreshToken(Long id, String uuid);

    /**
     * Refresh Access tokens.
     * @param accessToken access JWT Token
     * @param refreshToken refresh Token.
     * @return JwtDto object
     */
    JwtDto refreshJwtToken(String accessToken, String refreshToken );

    /**
     * Access Token 이 올바르다면, RefreshToken 을 Exprie 시킵니다.
     * @param accessToken access JWT Token
     */
    void logoutToken(String accessToken);

    /**
     * RefreshToken 이 중복되어 있는지 확인합니다.
     * @param id userId
     * @return boolean True/False
     */
    boolean isDuplicateRefreshToken(String id);
}