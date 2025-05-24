package com.example.smartagriculture.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;  // 秒

    @Value("${jwt.issuer}")
    private String issuer;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    /** 生成 Token，带 userId 和 username 两个 Claim */
    public String generateToken(Long userId, String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiration * 1000);
        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(now)
                .withExpiresAt(exp)
                .withClaim("id", userId)
                .withClaim("username", username)
                .sign(getAlgorithm());
    }

    /** 验证并解码 Token */
    public DecodedJWT verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(getAlgorithm())
                    .withIssuer(issuer)
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            // 验证失败（过期、签名错等）
            return null;
        }
    }

    /** 从 Token 获取 username */
    public String getUsername(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt != null ? jwt.getClaim("username").asString() : null;
    }

    /** 从 Token 获取 userId */
    public Long getUserId(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt != null ? jwt.getClaim("id").asLong() : null;
    }

    /** Token 是否有效 */
    public boolean validate(String token) {
        return verifyToken(token) != null;
    }

    /** 对外暴露的过期秒数 */
    public long getExpiration() {
        return expiration;
    }
}
