package com.company.common.utils;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JWT 토큰을 생성 및 검증하는 유틸리티 클래스
 */
@Component
public class JwtUtil {

	// 토큰 서명용 비밀 키
    private static final String SECRET_KEY = "af60addca9ea3e3c099551e1b6576c9966dce0a33de879dd7e160f86dbd872ca236d6e9ee66fb6e30039fe7c345324a10f3d0741b0600fa7a45df4c6691eff4f4209767ed39f51e37717d8feecd5dd14fc34ebe619e6a29ae91d9ffe134cb5718bec0b3680d6ae7fc09e67763fe7c05d05d3ba69f47211163852633755b7f861132b0c98f8d7c1af9152d547408e676867a0a32fb525a4354180f5fb6b2dc23b5faa4155b8db63385f96259a90b6ee0e74a5b90a4f0f4fa96fafc296c64588b5c009b3829ae2e1d69a1cf7569b50a65fa553350495d18816f785f961c970c0a9cb9c8da25cc5e9fa4a3e9527a132d616b232d1ee21c3bf6dc8d9e3376e2e82c0";

    // 토큰 만료 시간: 1시간
    private static final long EXPIRATION = 1000 * 60 * 60;

    // 사용자 이름을 기반으로 JWT 토큰 생성
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)                              // 토큰 제목에 사용자 이름 저장
                .setIssuedAt(new Date())                          // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // 만료 시간
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)   // 서명 알고리즘과 비밀 키
                .compact();
    }

    // 토큰에서 사용자 이름 추출
    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    // 토큰이 유효한지 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token); // 유효하지 않으면 예외 발생
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
