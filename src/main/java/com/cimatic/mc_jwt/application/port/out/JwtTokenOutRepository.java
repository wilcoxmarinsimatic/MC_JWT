package com.cimatic.mc_jwt.application.port.out;

public interface JwtTokenOutRepository {
    String generateAndSaveToken(String email);
    String getEmail(String token);
    boolean validateToken(String token, String email);
    boolean isTokenExpired(String token);
    void addToBlackList(String reason, String token);
}
