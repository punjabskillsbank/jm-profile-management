package com.jobmatrix.serviceimpl;

import com.common.enums.AccountStatus;
import com.jobmatrix.entity.User;
import com.jobmatrix.exceptionHandling.UserNotFoundException;
import com.jobmatrix.repository.UserRepository;
import com.jobmatrix.test_utils.factory.UserTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAccountServiceImpl userAccountService;

    private final UUID USER_ID = UUID.randomUUID();
    private User userEntity;

    @BeforeEach
    void setUp(){
        userEntity = UserTestDataFactory.createUserEntity(USER_ID);
    }

    @Test
    void updateUserProfile_shouldSoftDeleteAndReturnUser() {

        User existingUser = userEntity;

        User updatedUser = User.builder()
                .userId(USER_ID)
                .email(existingUser.getEmail())
                .firstName(existingUser.getFirstName())
                .lastName(existingUser.getLastName())
                .userRole(existingUser.getUserRole())
                .accountStatus(AccountStatus.TO_BE_DELETED)
                .createdAt(existingUser.getCreatedAt())
                .updatedAt(java.time.LocalDateTime.now())
                .build();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(existingUser));

        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        User result = userAccountService.updateUserProfile(USER_ID);

        assertNotNull(result);
        assertEquals(AccountStatus.TO_BE_DELETED, result.getAccountStatus());

        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUserProfile_userNotFound(){

        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userAccountService.updateUserProfile(USER_ID) );

        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository, never()).save(any(User.class));
    }
}
