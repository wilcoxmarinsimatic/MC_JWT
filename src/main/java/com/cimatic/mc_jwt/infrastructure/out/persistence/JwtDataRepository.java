package com.cimatic.mc_jwt.infrastructure.out.persistence;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cimatic.mc_jwt.application.port.out.JwtDataOutRepository;
import com.cimatic.mc_jwt.infrastructure.out.entity.JwtBlackListEntity;
import com.cimatic.mc_jwt.infrastructure.out.repository.JwtBlacklistJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JwtDataRepository implements JwtDataOutRepository{

    @Autowired
    private JwtBlacklistJpaRepository jwtBlacklistRepository;

    @Override
    public void addToBlacklist(String jti, LocalDateTime expiration, String reason) {
        JwtBlackListEntity jwtBlacklist = new JwtBlackListEntity();
        jwtBlacklist.setJti(jti);
        jwtBlacklist.setExpiration(expiration);
        jwtBlacklist.setReason(reason);
        
        jwtBlacklistRepository.save(jwtBlacklist);
    }

    @Override
    public boolean isBlacklisted(String jti) {
        Optional<JwtBlackListEntity> blacklistedToken = jwtBlacklistRepository.findByJti(jti);
        return blacklistedToken.isPresent() && blacklistedToken.get().getExpiration().isAfter(LocalDateTime.now());
    }

}
