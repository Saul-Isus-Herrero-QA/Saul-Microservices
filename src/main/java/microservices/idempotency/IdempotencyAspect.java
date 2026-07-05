package com.saul.microservices.common.idempotency;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
public class IdempotencyAspect {

    private static final Logger log = LoggerFactory.getLogger(IdempotencyAspect.class);
    private final JdbcTemplate jdbcTemplate;

    public IdempotencyAspect(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Around("@annotation(com.saul.microservices.common.idempotency.IdempotentConsumer)")
    public Object enforceIdempotency(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        IdempotentConsumer annotation = method.getAnnotation(IdempotentConsumer.class);
        
        String consumerGroup = annotation.consumerGroup();
        String messageId = extractMessageId(joinPoint, method);

        if (messageId == null) {
            log.warn("IdempotentConsumer en {} sin cabecera válida.", method.getName());
            return joinPoint.proceed();
        }

        try {
            insertMessageId(messageId, consumerGroup);
        } catch (DuplicateKeyException e) {
            log.info("Mensaje duplicado detectado: {}. Omitiendo procesamiento.", messageId);
            return null; 
        }

        return joinPoint.proceed();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void insertMessageId(String messageId, String consumerGroup) {
        String sql = "INSERT INTO processed_messages (message_id, consumer_group) VALUES (?, ?)";
        jdbcTemplate.update(sql, messageId, consumerGroup);
    }

    private String extractMessageId(ProceedingJoinPoint joinPoint, Method method) {
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Header headerAnn = parameters[i].getAnnotation(Header.class);
            if (headerAnn != null && (headerAnn.value().equals(KafkaHeaders.RECEIVED_KEY) || headerAnn.value().equals("message_id"))) {
                if (args[i] != null) return args[i].toString();
            }
        }
        return null;
    }
}