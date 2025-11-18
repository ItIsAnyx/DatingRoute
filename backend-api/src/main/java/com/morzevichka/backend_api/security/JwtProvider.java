package com.morzevichka.backend_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties properties;

    private SecretKey key;

    @PostConstruct
    void setKey() {
        byte[] keyBytes = Decoders.BASE64.decode(properties.getSecretKey());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public TokenType extractType(String token) {
        return extractClaim(token, claims -> TokenType.valueOf(claims.get("type").toString()));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }

    public String generateToken(Map<String, Object> extraClaims, String username) {
        return buildToken(extraClaims, username, properties.getAccessExpiration(), TokenType.ACCESS);
    }

    public String generateRefreshToken(String username) {
        return buildToken(new HashMap<>(), username, properties.getRefreshExpiration(), TokenType.REFRESH);
    }

    private String buildToken(Map<String, Object> extraClaims, String username, Long expiration, TokenType type) {
        extraClaims.put("type", type);

        return Jwts
                .builder()
                .header()
                    .add("typ", "JWT")
                    .and()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        return extractType(token).equals(TokenType.ACCESS);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
