package com.jobmatrix.test_utils.factory;

import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.dto.ClientUpdateRequest;
import com.jobmatrix.entity.Client;
import java.time.LocalDateTime;
import java.util.UUID;

public class ClientTestDataFactory {

    private static final String PHONE_NUMBER = "+919876543210";
    private static final String BIO = "Experienced client";
    private static final String PROFILE_PHOTO_URL = "https://example.com/profile.jpg";
    private static final String COMPANY_NAME = "Tech Innovators Pvt Ltd";
    private static final String COMPANY_SIZE = "10-50";
    private static final String INDUSTRY = "Software Development";
    private static final String TIME_ZONE = "Asia/Kolkata";
    private static final String CITY = "Bangalore";
    private static final String STATE = "Karnataka";
    private static final String COUNTRY = "India";
    private static final String POSTAL_CODE = "560001";
    private static final String ADDRESS = "123, MG Road, Bangalore, Karnataka, India";

    public static ClientDTO createClientDTO(UUID clientId){
        return  ClientDTO.builder()
                .clientId(clientId)
                .phoneNumber(PHONE_NUMBER)
                .bio(BIO)
                .profilePhotoURL(PROFILE_PHOTO_URL)
                .companyName(COMPANY_NAME)
                .companySize(COMPANY_SIZE)
                .industry(INDUSTRY)
                .timeZone(TIME_ZONE)
                .city(CITY)
                .state(STATE)
                .country(COUNTRY)
                .postalCode(POSTAL_CODE)
                .address(ADDRESS)
                .build();
    }

    public static Client createClientEntity(UUID clientId){
        return Client.builder()
                .clientId(clientId)
                .phoneNumber(PHONE_NUMBER)
                .bio(BIO)
                .profilePhotoURL(PROFILE_PHOTO_URL)
                .companyName(COMPANY_NAME)
                .companySize(COMPANY_SIZE)
                .industry(INDUSTRY)
                .timeZone(TIME_ZONE)
                .city(CITY)
                .state(STATE)
                .country(COUNTRY)
                .postalCode(POSTAL_CODE)
                .address(ADDRESS)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static ClientUpdateRequest createClientUpdateRequest(UUID clientId){
        return ClientUpdateRequest.builder()
                .phoneNumber(PHONE_NUMBER)
                .bio(BIO)
                .profilePhotoURL(PROFILE_PHOTO_URL)
                .companyName(COMPANY_NAME)
                .companySize(COMPANY_SIZE)
                .industry(INDUSTRY)
                .timeZone(TIME_ZONE)
                .city(CITY)
                .state(STATE)
                .country(COUNTRY)
                .postalCode(POSTAL_CODE)
                .address(ADDRESS)
                .build();


    }
}