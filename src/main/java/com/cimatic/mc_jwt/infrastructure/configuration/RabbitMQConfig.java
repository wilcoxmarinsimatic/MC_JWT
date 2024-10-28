package com.cimatic.mc_jwt.infrastructure.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String JWT_VALIDATION_QUEUE = "jwt_validation_queue";
    public static final String JWT_GENERATE_QUEUE = "jwt_generate_queue";
    public static final String JWT_EXPIRED_QUEUE = "jwt_expired_queue";
    public static final String JWT_SUBJECT_QUEUE = "jwt_subject_queue";
    public static final String JWT_REVOKE_QUEUE = "jwt_revoke_queue";


    @Bean
    public Queue validationQueue() {
        return new Queue(JWT_VALIDATION_QUEUE, true);
    }

    @Bean
    public Queue generateQueue() {
        return new Queue(JWT_GENERATE_QUEUE, true);
    }

    @Bean
    public Queue expiredQueue() {
        return new Queue(JWT_EXPIRED_QUEUE, true);
    }

    @Bean
    public Queue subjectQueue() {
        return new Queue(JWT_SUBJECT_QUEUE, true);
    }

    @Bean
    public Queue revokeQueue() {
        return new Queue(JWT_REVOKE_QUEUE, true);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        
        // Aquí se especifican los paquetes confiables para la deserialización
        typeMapper.setTrustedPackages("com.cimatic.mc_jwt.domain", "*");
        converter.setJavaTypeMapper(typeMapper);

        return converter;
    }
    
}
