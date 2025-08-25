package com.jobmatrix.test_utils.factory;

public class AuditKafkaProducerDataFactory {

    public static String sampleTopic() {
        return "audit-log-topic";
    }

    public static String sampleMessage() {
        return "{\"entityId\": 1, \"eventType\": \"UPDATE\", \"userId\": \"11111111-1111-1111-1111-111111111111\"}";
    }
}
