package com.jobmatrix.aspect;

import com.common.entity.Client;
import com.jobmatrix.dto.ClientUpdateRequest;
import com.jobmatrix.kafka.AuditKafkaProducer;
import com.jobmatrix.repository.ClientProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j

public class ClientAuditAspect {

    private final ClientProfileRepository clientProfileRepository;
    private final AuditKafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    private static final ThreadLocal<Boolean> AUDIT_IN_PROGRESS = ThreadLocal.withInitial(() -> false);

    @Around("execution(* com.jobmatrix..*.updateClientProfile(..))")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object auditClientUpdate(ProceedingJoinPoint joinPoint) throws Throwable {

        if (AUDIT_IN_PROGRESS.get()) {
            return joinPoint.proceed();
        }
        try {
            AUDIT_IN_PROGRESS.set(true);
            log.info("ClientAuditAspect triggered for: {}", joinPoint.getSignature());

            Object[] args = joinPoint.getArgs();
            UUID clientId = (UUID) args[0];
            ClientUpdateRequest clientUpdateRequest = (ClientUpdateRequest) args[1];

            UUID userId = clientUpdateRequest.getUserId();
            if (userId == null) {
                throw new IllegalArgumentException("ClientId (UserId) cannot be null for audit logging");
            }



            // fetch old entity (in this transaction)
            Client oldClient = clientProfileRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("Client not found: " + clientId));

            // map old to DTO-like object or plain map
            Object oldData = modelMapper.map(oldClient, com.jobmatrix.dto.ClientAuditDTO.class);

            // proceed with original update call
            Object result = joinPoint.proceed();

            // fetch new entity (still in same TX because REQUIRES_NEW)
            Client newClient = clientProfileRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("Client not found after update: " + clientId));

            Object newData = modelMapper.map(newClient, com.jobmatrix.dto.ClientAuditDTO.class);

            // Build audit payload
            Map<String, Object> auditPayload = new java.util.LinkedHashMap<>();
            auditPayload.put("serviceName", "client");    // service key used by audit-logging
            auditPayload.put("userId", userId);
            auditPayload.put("entityId", null);
            auditPayload.put("oldData", oldData);
            auditPayload.put("newData", newData);

            String auditJson = objectMapper.writeValueAsString(auditPayload);
            kafkaProducer.sendAuditLog(auditJson);

            return result;
        } finally {
            AUDIT_IN_PROGRESS.remove();
        }
    }
}