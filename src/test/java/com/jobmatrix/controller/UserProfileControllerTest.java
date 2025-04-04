package com.jobmatrix.controller;

import com.common.enums.AccountStatus;
import com.jobmatrix.entity.User;
import com.jobmatrix.serviceimpl.UserProfileServiceImpl;
import com.jobmatrix.test_utils.factory.UserTestDataFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@WebMvcTest(UserProfileController.class)
public class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserProfileServiceImpl userProfileService;

    private final UUID USER_ID = UUID.randomUUID();

    @Test
    public void testSoftDeleteUserProfile() throws Exception {

        User updatedUserEntity = UserTestDataFactory.createUserEntity(USER_ID);
        updatedUserEntity.setAccountStatus(AccountStatus.TO_BE_DELETED);

        Mockito.when(userProfileService.updateUserProfile(USER_ID)).thenReturn(updatedUserEntity);
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/soft_delete/{userId}", USER_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(USER_ID.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountStatus").value(AccountStatus.TO_BE_DELETED.toString()));

        Mockito.verify(userProfileService, Mockito.times(1)).updateUserProfile(USER_ID);
    }



}
