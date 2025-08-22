package com.jobmatrix.serviceimpl;

import com.common.enums.AccountStatus;
import com.common.entity.User;
import com.jobmatrix.exceptionHandling.UserNotFoundException;
import com.jobmatrix.repository.UserAccountRepository;
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
public class UserAccountServiceImplTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserAccountServiceImpl userAccountService;

    private final UUID USER_ID = UUID.randomUUID();
    private User userEntity;

    @BeforeEach
    void setUp(){
        userEntity = UserTestDataFactory.createUserEntity(USER_ID);
    }

    @Test
    void updateUserAccount_shouldSoftDeleteAndReturnUser() {

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

        when(userAccountRepository.findById(USER_ID)).thenReturn(Optional.of(existingUser));

        when(userAccountRepository.save(existingUser)).thenReturn(updatedUser);
        User result = userAccountService.updateUserAccount(USER_ID);

        assertNotNull(result);
        assertNotNull(result.getUpdatedAt());
        assertEquals(AccountStatus.TO_BE_DELETED, result.getAccountStatus());

        verify(userAccountRepository, times(1)).findById(USER_ID);
        verify(userAccountRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUserAccount_userNotFound(){

        when(userAccountRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userAccountService.updateUserAccount(USER_ID) );

        verify(userAccountRepository, times(1)).findById(USER_ID);
        verify(userAccountRepository, never()).save(any(User.class));
    }
}
