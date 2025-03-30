package com.jobmatrix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "certificates")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id")
    private Long certificateId;

    @Column(name = "certificate_name")
    private String certificateName;

    @Column(name = "issued_by")
    private String issuedBy;

    @Column(name = "issue_date")
    private Date issueDate;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "credential_url")
    private String credentialUrl;

    @Column(name = "freelancer_id")
    private UUID freelancerId;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", referencedColumnName = "freelancer_id", insertable = false, updatable = false)
    private Freelancer freelancer;
}
