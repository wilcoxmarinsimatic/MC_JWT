package com.cimatic.mc_jwt.infrastructure.out.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import com.cimatic.mc_jwt.infrastructure.out.entity.JwtBlackListEntity;


public interface JwtBlacklistJpaRepository extends JpaRepository<JwtBlackListEntity, Long> {

    // Buscar un token en la lista negra por su JTI
    Optional<JwtBlackListEntity> findByJti(String jti);
}
