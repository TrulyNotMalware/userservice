package com.cotae.platform.userservice.domain;

import com.cotae.platform.userservice.configurations.JwtTokenProvider;
import com.cotae.platform.userservice.dto.JwtDto;
import com.cotae.platform.userservice.dto.UserDto;
import com.cotae.platform.userservice.infrastructure.RefreshTokenInfra;
import com.cotae.platform.userservice.infrastructure.UserInfra;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RefreshTokenDomainImpl implements RefreshTokenDomain{
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserInfra userInfra;
    private final RefreshTokenInfra refreshTokenInfra;

    @Override
    @Transactional
    public void updateRefreshToken(Long id, String uuid) {
        UserDto user = this.userInfra.selectUserById(id);
        this.refreshTokenInfra.pushRefreshToken(user.getId().toString(),uuid);
    }

    //FIXME NEED EXCEPTION HANDLING.
    @Override
    public JwtDto refreshJwtToken(String accessToken, String refreshToken) {
        String userId = this.jwtTokenProvider.getClaimsFromJwtToken(accessToken).getSubject();
        //Refresh Token 이 Database 에 없으면 안됨.
        String findRefreshTokenId = this.refreshTokenInfra.selectRefreshTokenById(userId).getRefreshToken();
        // Refresh Token 검증 1)
        // Access Token 이 만료되지 않았는데, Refresh 를 요청하는것은 탈취의 가능성이 있으므로, Refresh Token 삭제해서 만료시키기.
        if(!this.jwtTokenProvider.isExpiredToken(accessToken)){
            this.refreshTokenInfra.deleteRefreshTokenById(userId);
            throw new RuntimeException("Access Token is Not expired yet. Expire every tokens.");
        }
        //Refresh Token 검증 2) 올바른 Refresh Token 여부
        log.info("refresh-Token : {}",refreshToken);
        if(!this.jwtTokenProvider.validateJwtToken(refreshToken)){
            //검증되지 않은 RefreshToken 은 바로 삭제.
            this.refreshTokenInfra.deleteRefreshTokenById(userId);
            throw new RuntimeException("Not a valid Refresh token");
        }
        // 검증 3) 저장된 RefreshToken Id 와, 제공받은 RefreshToken의 Id가 서로 다른 경우
        if (!this.jwtTokenProvider.equalRefreshTokenId(findRefreshTokenId,refreshToken)){
            //Token 을 탈취당했을 가능성이 있음. 바로 Runtime Exception 발생, 토큰 만료.
            this.refreshTokenInfra.deleteRefreshTokenById(userId);
            throw new RuntimeException("Refresh Token value not same.");
        }

        UserDto user = this.userInfra.selectUserById(Long.valueOf(userId));
        Authentication authentication = getAuthentication(user.getEmail());
        List<String> roles = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        String newAccessToken = this.jwtTokenProvider.createJwtAccessToken(userId,"/api/user/auth/reissue",roles);
        Date expiredTime = this.jwtTokenProvider.getClaimsFromJwtToken(newAccessToken).getExpiration();

        return new JwtDto(newAccessToken,refreshToken,expiredTime);
    }

    @Override
    @Transactional
    public void logoutToken(String accessToken) {
        //Not a valid token,
        if(!this.jwtTokenProvider.validateJwtToken(accessToken)){
            throw new RuntimeException("Access Token is Not valid");
        }
        String userId = this.jwtTokenProvider.getClaimsFromJwtToken(accessToken).getSubject();
        this.refreshTokenInfra.deleteRefreshTokenById(userId);
    }

    @Override
    public boolean isDuplicateRefreshToken(String userId) {
        return this.refreshTokenInfra.refreshTokenIsExistsByUserId(userId);
    }


    private Authentication getAuthentication(String email){
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }
}
