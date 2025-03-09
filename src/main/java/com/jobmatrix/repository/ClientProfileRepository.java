package com.jobmatrix.repository;

import com.jobmatrix.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientProfileRepository extends JpaRepository<ClientEntity, UUID> {


}
