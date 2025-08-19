package com.jobmatrix.repository;

import com.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserAccountRepository extends JpaRepository<User, UUID> {
}
