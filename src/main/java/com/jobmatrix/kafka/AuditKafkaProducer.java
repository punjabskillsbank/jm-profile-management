package com.jobmatrix.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service responsible for sending audit logs to Kafka.
 *
 * Kafka topic must be created manually before sending messages.
 *
 *  Make sure Kafka and Zookeeper servers are running before executing this command.
 *  You can start them using the following commands:
 *
 *  # Start Zookeeper
 *  bin/zookeeper-server-start.sh config/zookeeper.properties
 *
 *  # In a separate terminal, start Kafka
 *  bin/kafka-server-start.sh config/server.properties
 *
 * Use the following bash command to create the topic:
 *
 * kafka-topics.sh --create --topic audit-log-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${audit.logging.topic}")
    private String topic;

    public void sendAuditLog(String message) {
        kafkaTemplate.send(topic, message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Successfully sent audit message to topic '{}': {}", topic, message);
                    } else {
                        log.error("Failed to send audit message to topic '{}': {}", topic, message, ex);
                    }
                });
    }
}
