package com.jobmatrix.serviceimpl;

import com.common.enums.AccountStatus;
import com.common.entity.User;
import com.jobmatrix.exceptionHandling.UserNotFoundException;
import com.jobmatrix.repository.UserAccountRepository;
import com.jobmatrix.service.UserAccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Transactional
    @Override
    public User updateUserAccount(UUID userId) {

        User tempUser = userAccountRepository.findById(userId)
                .orElseThrow( ()-> new UserNotFoundException(userId));

        tempUser.setAccountStatus(AccountStatus.TO_BE_DELETED);
        return userAccountRepository.save(tempUser);
    }
}
