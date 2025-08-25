package com.jobmatrix.kafka;

import com.jobmatrix.test_utils.factory.AuditKafkaProducerDataFactory;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

class AuditKafkaProducerTest {

    private KafkaTemplate<String, String> kafkaTemplate;
    private AuditKafkaProducer producer;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        producer = new AuditKafkaProducer(kafkaTemplate);

        // Set topic manually since @Value won't be injected in unit tests
        String topic = AuditKafkaProducerDataFactory.sampleTopic();
        setTopicViaReflection(producer, topic);
    }

    @Test
    void testSendAuditLogSuccess() {
        String message = AuditKafkaProducerDataFactory.sampleMessage();
        String topic = AuditKafkaProducerDataFactory.sampleTopic();

        CompletableFuture future = new CompletableFuture<>();
        future.complete(mock(RecordMetadata.class));

        when(kafkaTemplate.send(topic, message)).thenReturn(future);

        producer.sendAuditLog(message);

        verify(kafkaTemplate).send(topic, message);
    }

    @Test
    void testSendAuditLogFailure() {
        String message = AuditKafkaProducerDataFactory.sampleMessage();
        String topic = AuditKafkaProducerDataFactory.sampleTopic();

        CompletableFuture future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Kafka send failed"));

        when(kafkaTemplate.send(topic, message)).thenReturn(future);

        producer.sendAuditLog(message);

        verify(kafkaTemplate).send(topic, message);
    }

    // Utility method to set private topic field via reflection
    private void setTopicViaReflection(AuditKafkaProducer producer, String topicValue) {
        try {
            var field = AuditKafkaProducer.class.getDeclaredField("topic");
            field.setAccessible(true);
            field.set(producer, topicValue);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set topic via reflection", e);
        }
    }
}
