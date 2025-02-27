package com.jobmatrix.jm_profile_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "work_experience")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkExperienceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancerEntity freelancer;

    @Column(name = "job_title")
    @JsonProperty("job_title")
    private String jobTitle;

    @Column(name = "company_name")
    @JsonProperty("company_name")
    private String companyName;

    @Column(name = "start_date")
    @JsonProperty("start_date")
    private String startDate;

    @Column(name = "end_date")
    @JsonProperty("end_date")
    private String endDate;

    @Column(name = "job_responsibilities")
    @JsonProperty("job_responsibilities")
    private String jobResponsibilities;

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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getJobResponsibilities() {
        return jobResponsibilities;
    }

    public void setJobResponsibilities(String jobResponsibilities) {
        this.jobResponsibilities = jobResponsibilities;
    }
}

