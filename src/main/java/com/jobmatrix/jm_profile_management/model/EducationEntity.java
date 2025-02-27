package com.jobmatrix.jm_profile_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "education")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EducationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "freelancer_id" )
    private FreelancerEntity freelancer;

    @Column(name = "degree")
    @JsonProperty("degree")
    private String degree;

    @Column(name = "specialization")
    @JsonProperty("specialization")
    private String specialization;

    @Column(name = "institution")
    @JsonProperty("institution")
    private String institution;

    @Column(name = "start_year")
    @JsonProperty("start_year")
    private String startYear;

    @Column(name = "end_year")
    @JsonProperty("end_year")
    private String endYear;

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

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }
}
