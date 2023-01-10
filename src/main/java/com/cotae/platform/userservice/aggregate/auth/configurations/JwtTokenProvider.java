package com.cotae.platform.userservice.aggregate.auth.configurations;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class JwtTokenProvider {
    @Value("${token.access-expired-time}")
    private long ACCESS_EXPIRED_TIME;
    @Value("${token.refresh-expired-time}")
    private long REFRESH_EXPIRED_TIME;
    @Value("${token.secret}")
    private String SECRET;

    public String createJwtAccessToken(String userId, String uri, List<String> roles){
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("roles", roles);

        return Jwts.builder()
                .addClaims(claims)
                .setExpiration(
                        new Date(System.currentTimeMillis() + ACCESS_EXPIRED_TIME)
                )
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setIssuer(uri)
                .compact();
    }

    public String createJwtRefreshToken(){
        Claims claims = Jwts.claims();
        claims.put("value", UUID.randomUUID());

        return Jwts.builder()
                .addClaims(claims)
                .setExpiration(
                        new Date(System.currentTimeMillis() + REFRESH_EXPIRED_TIME)
                )
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public Claims getClaimsFromJwtToken(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (Exception e) {
            log.error("Error in refresh");
            e.printStackTrace();
            throw new RuntimeException("Claims Parse error.");
        }
    }

    public boolean isExpiredToken(String token){
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getExpiration();
            return false;
        }catch (ExpiredJwtException e){
            log.error("Expired JWT Tokens : {}",token);
            return true;
        }
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            return false;
        }
    }

    public boolean equalRefreshTokenId(String refreshTokenId, String refreshToken) {
        String compareToken = this.getClaimsFromJwtToken(refreshToken).get("value").toString();
        return refreshTokenId.equals(compareToken);
    }
}
