package com.cimatic.mc_jwt.application.port.out;

import java.time.LocalDateTime;

public interface JwtDataOutRepository {
    void addToBlacklist(String jti, LocalDateTime expiration, String reason);
    boolean isBlacklisted(String jti);
}
