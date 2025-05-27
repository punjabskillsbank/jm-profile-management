package com.jobmatrix.repository;

import com.jobmatrix.entity.FreelancerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FreelancerServicesRepository extends JpaRepository<FreelancerServices, Long> {
    List<FreelancerServices> findByFreelancerId(UUID freelancerId);
}
