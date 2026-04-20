package com.pigeonskyrace.security;

import com.pigeonskyrace.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public enum TokenState {
        VALID,
        EXPIRED,
        INVALID
    }

    public String createAccessToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.expirationMs());
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("uid", user.getId().toHexString())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey())
                .compact();
    }

    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public String getUserIdHex(String token) {
        return parseClaims(token).get("uid", String.class);
    }

    /**
     * Distinguishes expired vs malformed tokens for proper 401 messaging.
     */
    public TokenState resolveTokenState(String token) {
        if (token == null || token.isBlank()) {
            return TokenState.INVALID;
        }
        try {
            parseClaims(token);
            return TokenState.VALID;
        } catch (ExpiredJwtException e) {
            return TokenState.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {
            return TokenState.INVALID;
        }
    }

    public boolean validateToken(String token) {
        return resolveTokenState(token) == TokenState.VALID;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey signingKey() {
        byte[] keyBytes = jwtProperties.secret().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException(
                    "JWT secret must be at least 32 UTF-8 bytes. Set app.jwt.secret or JWT_SECRET.");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
