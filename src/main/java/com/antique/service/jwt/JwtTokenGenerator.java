package com.antique.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenGenerator {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expire-length}")
    private long expireTimeMilliSecond;

    // JWT 생성
    public String generateToken(final Long userId) {
        final Claims claims = Jwts.claims();
        claims.put("userId", userId); // Payload에 userId 추가
        final Date now = new Date();
        final Date expiredDate = new Date(now.getTime() + expireTimeMilliSecond);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // JWT에서 userId 추출
    public Long extractUserId(final String token) {
        try {
            return Long.parseLong(Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("userId").toString()); // "userId" 필드를 추출하여 변환
        } catch (final Exception error) {
            throw new RuntimeException("Invalid Access Token");
        }
    }
}
