package com.cotae.platform.userservice.aggregate.auth.configurations.filters;


import com.cotae.platform.userservice.aggregate.auth.configurations.CookieProvider;
import com.cotae.platform.userservice.aggregate.auth.configurations.JwtTokenProvider;
import com.cotae.platform.userservice.aggregate.auth.domain.RefreshTokenDomain;
import com.cotae.platform.userservice.aggregate.auth.dto.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    //User Request 정보로 UserPasswordAuthenticationToken 발급, AuthenticationManager 에게 전달하고, Provider 의 인증로직을 거침.
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieProvider cookieProvider;
    private final RefreshTokenDomain refreshToken;

    @Override //Login Request 의 Value를 LoginRequestDto class 로 Mapping. ( 정합성 여부는 Gateway 가 검사해 줄 것임. )
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            log.info("AttemptAuthentication");
            LoginRequestDto loginRequestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
            return this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //When Login success,
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult
    ) throws IOException {
        User user = (User) authResult.getPrincipal();
        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String userId = user.getUsername();
        //중복 로그인 금지.
        if(this.refreshToken.isDuplicateRefreshToken(userId)){
            //FIXME if duplicate login detected, delete exists token?
            throw new RuntimeException("Multiple Login detected.");
        }

        String accessToken = this.jwtTokenProvider.createJwtAccessToken(userId, request.getRequestURI(), roles);
        String refreshToken = this.jwtTokenProvider.createJwtRefreshToken();
        Date expiredTime = this.jwtTokenProvider.getClaimsFromJwtToken(accessToken).getExpiration();

        //Update Refresh Token - 중복로그인 허용할지 말지?
        this.refreshToken.updateRefreshToken(
                Long.valueOf(userId),
                this.jwtTokenProvider.getClaimsFromJwtToken(refreshToken).get("value").toString()
        );

        //Set Cookie
        ResponseCookie refreshTokenCookie = this.cookieProvider.createRefreshTokenCookie(refreshToken);
        Cookie cookie = this.cookieProvider.of(refreshTokenCookie);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.addCookie(cookie);

        // body 설정
        Map<String, Object> tokens = Map.of(
                "accessToken", accessToken,
                "expiredTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expiredTime),
                "message", "login success"
        );
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}