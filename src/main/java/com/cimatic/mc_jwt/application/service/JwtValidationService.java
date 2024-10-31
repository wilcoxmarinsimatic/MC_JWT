package com.cimatic.mc_jwt.application.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import com.cimatic.mc_jwt.application.port.out.JwtTokenOutRepository;
import com.cimatic.mc_jwt.domain.JwtRequest;
import com.cimatic.mc_jwt.infrastructure.configuration.RabbitMQConfig;

import io.jsonwebtoken.ExpiredJwtException;

@Service
public class JwtValidationService {

    private final JwtTokenOutRepository jwtTokenRepository;

    public JwtValidationService(JwtTokenOutRepository jwtTokenRepository) {
        this.jwtTokenRepository = jwtTokenRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.JWT_VALIDATION_QUEUE)
    public Map<String, Object> validateJwtToken(JwtRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("correlationId", request.getCorrelationId());
        try {
            boolean isValid = jwtTokenRepository.validateToken(request.getToken(), request.getUsername());
            response.put("valid", isValid);
        } catch (IllegalArgumentException e) {
            response.put("valid", false);
        }catch (ExpiredJwtException e) {
            response.put("valid", false);
        }
        return response;
    }

    @RabbitListener(queues = RabbitMQConfig.JWT_GENERATE_QUEUE)
    public Map<String, Object> generateJwtToken(JwtRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("correlationId", request.getCorrelationId());
        try {
            String token = jwtTokenRepository.generateAndSaveToken(request.getUsername());
            response.put("valid", true);
            response.put("token", token);
        } catch (IllegalArgumentException e) {
            response.put("valid", false);
            response.put("token", "");
        } catch (ExpiredJwtException e) {
            response.put("valid", false);
            response.put("token", "");
        }
        return response;
    }

    @RabbitListener(queues = RabbitMQConfig.JWT_EXPIRED_QUEUE)
    public Map<String, Object> expiredJwtToken(JwtRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("correlationId", request.getCorrelationId());
        try {
            Boolean expired = jwtTokenRepository.isTokenExpired(request.getToken());
            response.put("valid", expired);
            response.put("token", request.getToken());
        } catch (IllegalArgumentException e) {
            response.put("valid", true);
            response.put("token", request.getToken());
        }catch (ExpiredJwtException e) {
            response.put("valid", true);
            response.put("token", request.getToken());
        }
        
        return response;
    }

    @RabbitListener(queues = RabbitMQConfig.JWT_SUBJECT_QUEUE)
    public Map<String, Object> subjectJwtToken(JwtRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("correlationId", request.getCorrelationId());

        try {
            String user = jwtTokenRepository.getEmail(request.getToken());
            response.put("valid", true);
            response.put("token", request.getToken());
            response.put("username", user);
        } catch (IllegalArgumentException e) {
            response.put("valid", false);
            response.put("token", request.getToken());
            response.put("username", "");
        }catch (ExpiredJwtException e) {
            response.put("valid", false);
            response.put("token", request.getToken());
            response.put("username", "");
        }

        return response;
    }

    @RabbitListener(queues = RabbitMQConfig.JWT_REVOKE_QUEUE)
    public void revokeJwtToken(JwtRequest request) {
        try {
            jwtTokenRepository.addToBlackList(request.getReason(), request.getToken());
        } catch (IllegalArgumentException e) {
            System.out.println("error");
        }catch (ExpiredJwtException e) {
            System.out.println("token expiro");
        }
        
    }

}
