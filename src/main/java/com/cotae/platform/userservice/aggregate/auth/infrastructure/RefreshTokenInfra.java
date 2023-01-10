package com.cotae.platform.userservice.aggregate.auth.infrastructure;

import com.cotae.platform.userservice.aggregate.auth.dto.JwtDto;

public interface RefreshTokenInfra {
    /**
     * Insert RefreshTokens to DB.
     * @param userId userId
     * @param refreshToken created Refresh Token
     */
    void pushRefreshToken(String userId, String refreshToken);

    /**
     * Select Refresh Tokens by primary Key userId
     * @param userId userId ( Long )
     * @return JwtDto - only has RefreshTokens
     */
    JwtDto selectRefreshTokenById(String userId);

    /**
     * Delete RefreshToken by Primary key userId.
     * @param userId userId
     */
    void deleteRefreshTokenById(String userId);

    /**
     * check refresh token is already exists in table.
     * @param userId userId
     * @return boolean (True/False)
     */
    boolean refreshTokenIsExistsByUserId(String userId);
}
