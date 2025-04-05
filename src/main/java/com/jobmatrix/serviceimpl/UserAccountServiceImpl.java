package com.jobmatrix.serviceimpl;

import com.common.enums.AccountStatus;
import com.jobmatrix.entity.User;
import com.jobmatrix.exceptionHandling.UserNotFoundException;
import com.jobmatrix.repository.UserRepository;
import com.jobmatrix.service.UserAccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public User updateUserProfile(UUID userId) {

        User tempUser = userRepository.findById(userId)
                .orElseThrow( ()-> new UserNotFoundException(userId));

        tempUser.setAccountStatus(AccountStatus.TO_BE_DELETED);
        return userRepository.save(tempUser);
    }
}
