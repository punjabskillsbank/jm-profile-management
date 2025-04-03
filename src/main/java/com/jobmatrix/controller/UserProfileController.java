package com.jobmatrix.controller;

import com.jobmatrix.entity.User;
import com.jobmatrix.serviceimpl.UserProfileServiceImpl;
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
public class UserProfileController {

    private final UserProfileServiceImpl userProfileService;

    @PatchMapping("/soft_delete/{userId}")
    public ResponseEntity<User> softDeleteUserProfile(
            @PathVariable UUID userId) {

        User user = userProfileService.updateUserProfile(userId);
        return ResponseEntity.ok(user);
    }
}
