package com.jobmatrix.controller;

import com.jobmatrix.entity.User;
import com.jobmatrix.serviceimpl.UserAccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountServiceImpl userAccountService;

    @PatchMapping("/soft_delete/{userId}")
    public ResponseEntity<User> softDeleteUserProfile(
            @PathVariable UUID userId) {

        User user = userAccountService.updateUserProfile(userId);
        return ResponseEntity.ok(user);
    }
}
