package com.jobmatrix.jm_profile_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "certificates")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CertificatesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancerEntity freelancer;

    @Column(name = "certificate_name")
    @JsonProperty("certificate_name")
    private String certificateName;

    @Column(name = "issued_by")
    @JsonProperty("issued_by")
    private String issuedBy;

    @Column(name = "issue_date")
    @JsonProperty("issue_date")
    private String issueDate;

    @Column(name = "expiry_date")
    @JsonProperty("expiry_date")
    private String expiryDate;

    @Column(name = "credential_url")
    @JsonProperty("credential_url")
    private String credentialUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FreelancerEntity getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(FreelancerEntity freelancer) {
        this.freelancer = freelancer;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCredentialUrl() {
        return credentialUrl;
    }

    public void setCredentialUrl(String credentialUrl) {
        this.credentialUrl = credentialUrl;
    }
}
