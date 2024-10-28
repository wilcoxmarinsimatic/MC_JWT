package com.cimatic.mc_jwt.infrastructure.out.persistence;

import java.util.Date;
import java.util.UUID;


import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cimatic.mc_jwt.application.port.out.JwtTokenOutRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Repository
public class JwtTokenRepository implements JwtTokenOutRepository{

    private final String secretKey = "3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b";

    @Autowired
    private JwtDataRepository jwtDataRepository;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateAndSaveToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .setId(UUID.randomUUID().toString())
                .compact();
    }

    @Override
    public boolean validateToken(String token, String email) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

                String jti = claims.getId();

            if (jwtDataRepository.isBlacklisted(jti)) {
                return false;  // El token está revocado
            }
            String emailFromToken = claims.getSubject();

        return email.equals(emailFromToken) && !isTokenExpired(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    @Override
    public String getEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
    }

    @Override
    public void addToBlackList(String reason, String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();

            String jti = claims.getId();  // Obtener el 'jti' del token
            LocalDateTime expiration = claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            
            jwtDataRepository.addToBlacklist(jti, expiration, reason);
    }

}
