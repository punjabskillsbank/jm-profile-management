package com.jobmatrix.service;

import com.jobmatrix.entity.User;

import java.util.UUID;

public interface UserAccountService {

    User updateUserProfile(UUID userId);
}
