package com.jobmatrix.repository;

import com.jobmatrix.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificatesRepository extends JpaRepository<Certificate, Long> {
}
