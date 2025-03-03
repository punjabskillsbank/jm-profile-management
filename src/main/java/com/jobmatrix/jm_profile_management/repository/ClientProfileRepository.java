package com.jobmatrix.jm_profile_management.repository;

import com.jobmatrix.jm_profile_management.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientProfileRepository extends JpaRepository<ClientEntity, UUID> {


}
