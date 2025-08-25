package com.jobmatrix.test_utils.factory;

import com.common.entity.Client;
import com.jobmatrix.dto.ClientAuditDTO;
import com.jobmatrix.dto.ClientUpdateRequest;

import java.util.UUID;
public class AuditLogsTestDataFactory {

    public static UUID sampleClientId() {
        return UUID.fromString("22222222-2222-2222-2222-222222222222");
    }

    public static ClientUpdateRequest createUpdateRequest() {
        return ClientUpdateRequest.builder()
                .userId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .companyName("New Co")
                .build();
    }

    public static Client createOldClient() {
        Client client = new Client();
        client.setClientId(sampleClientId());
        client.setCompanyName("Old Co");
        return client;
    }

    public static Client createNewClient() {
        Client client = new Client();
        client.setClientId(sampleClientId());
        client.setCompanyName("New Co");
        return client;
    }

    public static ClientAuditDTO createOldAuditDTO() {
        return ClientAuditDTO.builder()
                .companyName("Old Co")
                .build();
    }

    public static ClientAuditDTO createNewAuditDTO() {
        return ClientAuditDTO.builder()
                .companyName("New Co")
                .build();
    }
}
