package com.jobmatrix.repository;


import com.common.entity.Client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientProfileRepository extends JpaRepository<Client, UUID> {
}