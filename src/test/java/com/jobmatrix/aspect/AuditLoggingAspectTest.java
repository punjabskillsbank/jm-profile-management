package com.jobmatrix.aspect;

import com.common.entity.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobmatrix.dto.ClientAuditDTO;
import com.jobmatrix.dto.ClientUpdateRequest;
import com.jobmatrix.kafka.AuditKafkaProducer;
import com.jobmatrix.repository.ClientProfileRepository;
import com.jobmatrix.test_utils.factory.AuditLogsTestDataFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuditLoggingAspectTest {

    private ClientProfileRepository clientProfileRepository;
    private AuditKafkaProducer kafkaProducer;
    private ObjectMapper objectMapper;
    private ModelMapper modelMapper;
    private ClientAuditAspect auditAspect;

    @BeforeEach
    void setUp() {
        clientProfileRepository = mock(ClientProfileRepository.class);
        kafkaProducer = mock(AuditKafkaProducer.class);
        objectMapper = mock(ObjectMapper.class);
        modelMapper = mock(ModelMapper.class);

        auditAspect = new ClientAuditAspect(
                clientProfileRepository,
                kafkaProducer,
                objectMapper,
                modelMapper
        );
    }

    @Test
    void testAuditLogSuccess() throws Throwable {
        UUID clientId = AuditLogsTestDataFactory.sampleClientId();
        ClientUpdateRequest request = AuditLogsTestDataFactory.createUpdateRequest();
        Client oldClient = AuditLogsTestDataFactory.createOldClient();
        Client newClient = AuditLogsTestDataFactory.createNewClient();
        ClientAuditDTO oldDTO = AuditLogsTestDataFactory.createOldAuditDTO();
        ClientAuditDTO newDTO = AuditLogsTestDataFactory.createNewAuditDTO();

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Signature signature = mock(Signature.class);
        when(signature.toShortString()).thenReturn("updateClientProfile");
        when(joinPoint.getSignature()).thenReturn(signature);
        when(joinPoint.getArgs()).thenReturn(new Object[]{clientId, request});
        when(joinPoint.proceed()).thenReturn("result");

        when(clientProfileRepository.findById(clientId))
                .thenReturn(Optional.of(oldClient))
                .thenReturn(Optional.of(newClient));

        when(modelMapper.map(oldClient, ClientAuditDTO.class)).thenReturn(oldDTO);
        when(modelMapper.map(newClient, ClientAuditDTO.class)).thenReturn(newDTO);
        when(objectMapper.writeValueAsString(any())).thenReturn("{json}");

        Object result = auditAspect.auditClientUpdate(joinPoint);

        assertEquals("result", result);
        verify(kafkaProducer).sendAuditLog("{json}");
        verify(objectMapper).writeValueAsString(any());
        verify(modelMapper, times(2)).map(any(), eq(ClientAuditDTO.class));
        verify(clientProfileRepository, times(2)).findById(clientId);
    }

    @Test
    void testClientNotFoundThrowsException() {
        UUID clientId = AuditLogsTestDataFactory.sampleClientId();
        ClientUpdateRequest request = AuditLogsTestDataFactory.createUpdateRequest();

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Signature signature = mock(Signature.class);
        when(signature.toShortString()).thenReturn("updateClientProfile");
        when(joinPoint.getSignature()).thenReturn(signature);
        when(joinPoint.getArgs()).thenReturn(new Object[]{clientId, request});

        when(clientProfileRepository.findById(clientId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> auditAspect.auditClientUpdate(joinPoint));

        assertEquals("Client not found: " + clientId, exception.getMessage());
        verifyNoInteractions(kafkaProducer);
    }

    @Test
    void testAuditSkippedWhenThreadLocalSet() throws Throwable {
        UUID clientId = AuditLogsTestDataFactory.sampleClientId();
        ClientUpdateRequest request = AuditLogsTestDataFactory.createUpdateRequest();

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Signature signature = mock(Signature.class);
        when(signature.toShortString()).thenReturn("updateClientProfile");
        when(joinPoint.getSignature()).thenReturn(signature);
        when(joinPoint.getArgs()).thenReturn(new Object[]{clientId, request});
        when(joinPoint.proceed()).thenReturn("skipped");

        // Simulate thread-local set
        setAuditInProgress(true);

        try {
            Object result = auditAspect.auditClientUpdate(joinPoint);
            assertEquals("skipped", result);
            verifyNoInteractions(clientProfileRepository);
            verifyNoInteractions(kafkaProducer);
        } finally {
            setAuditInProgress(false);
        }
    }

    private void setAuditInProgress(boolean value) {
        try {
            Field field = ClientAuditAspect.class.getDeclaredField("AUDIT_IN_PROGRESS");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            ThreadLocal<Boolean> auditInProgress = (ThreadLocal<Boolean>) field.get(null);
            if (value) {
                auditInProgress.set(true);
            } else {
                auditInProgress.remove();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to set AUDIT_IN_PROGRESS", e);
        }
    }
}
