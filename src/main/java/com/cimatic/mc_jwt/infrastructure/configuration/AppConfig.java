package com.cimatic.mc_jwt.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cimatic.mc_jwt.application.service.JwtValidationService;
import com.cimatic.mc_jwt.infrastructure.out.persistence.JwtTokenRepository;

@Configuration
public class AppConfig {

    @Bean
    public JwtValidationService jwtValidationService(JwtTokenRepository jwtTokenRepository){
        return new JwtValidationService(jwtTokenRepository);
    }

}

// activacion de tren superior
