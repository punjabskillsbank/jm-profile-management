package com.jobmatrix.service;

import com.common.entity.User;

import java.util.UUID;

public interface UserAccountService {

    User updateUserAccount(UUID userId);
}
