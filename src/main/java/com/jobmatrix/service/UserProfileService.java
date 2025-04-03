package com.jobmatrix.service;

import com.jobmatrix.entity.User;

import java.util.UUID;

public interface UserProfileService {

    User updateUserProfile(UUID userId);
}
