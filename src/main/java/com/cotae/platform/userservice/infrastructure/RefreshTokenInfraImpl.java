package com.cotae.platform.userservice.infrastructure;

import com.cotae.platform.userservice.dao.RefreshTokenRepository;
import com.cotae.platform.userservice.dto.JwtDto;
import com.cotae.platform.userservice.entity.RefreshTokenEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenInfraImpl implements RefreshTokenInfra{
    //JPA Repository
    private final RefreshTokenRepository repository;

    @Override
    @Transactional
    public void pushRefreshToken(String userId, String refreshToken) {
        this.repository.save(new RefreshTokenEntity(userId,refreshToken, LocalDateTime.now()));
    }

    @Override
    public JwtDto selectRefreshTokenById(String userId) {
        RefreshTokenEntity entity = this.repository.findById(userId)
                .orElseThrow(()
                        -> new RuntimeException("Refresh Token Not Found."));

        return new JwtDto(null,entity.getRefreshToken(),null);//AccessToken 관련 Data 는 들어있지 않음.
    }

    @Override
    @Transactional
    public void deleteRefreshTokenById(String userId) {
        this.repository.deleteById(userId);
    }

    @Override
    public boolean refreshTokenIsExistsByUserId(String userId) {
        return this.repository.findById(userId).orElse(null) != null;
    }
}
