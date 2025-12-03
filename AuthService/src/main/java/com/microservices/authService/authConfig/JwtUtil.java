package com.microservices.authService.authConfig;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final JwtConfig jwtConfig;
    private Key secretKey;

    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @PostConstruct
    public void init() {
        // Initialize secretKey after bean creation
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
        System.out.println("🔐 Loaded JWT secret key.");
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) throws ExpiredJwtException {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            System.out.println("🧪 Validating token...");
            Claims claims = extractClaims(token);
            System.out.println("✅ Token is valid. Subject: " + claims.getSubject());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("❌ JWT validation error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return false;
        }
    }
}
