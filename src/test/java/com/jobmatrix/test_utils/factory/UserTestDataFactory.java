package com.jobmatrix.test_utils.factory;

import com.common.enums.AccountStatus;
import com.common.enums.UserRole;
import com.common.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserTestDataFactory {

    public static User createUserEntity(UUID userId){
        return User.builder()
                .userId(userId)
                .email("testuser@gmail.com")
                .firstName("Test")
                .lastName("User")
                .userRole(UserRole.CLIENT)
                .accountStatus(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
