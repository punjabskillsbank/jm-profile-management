package com.jobmatrix.jm_profile_management.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "freelancer_profile")
public class FreelancerEntity {

    @Id
    @Column(nullable = false, name = "sub", updatable = false)
    private UUID sub;

    @Column(name = "title")
    private String title;

    @Column(name = "bio")
    private String bio;

    @Column(name = "hourly_rate")
    private BigDecimal hourlyRate;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_abc_member")
    private boolean isAbcMember;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "education")
    private List<EducationEntity> education;

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL, orphanRemoval = true )
    @Column(name = "work_experience")
    private List<WorkExperienceEntity> workExperience;

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL, orphanRemoval = true )
    @Column(name = "certificates")
    private List<CertificatesEntity> certificates;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile_status")
    private ProfileStatus profileStatus;

    public UUID getSub() {
        return sub;
    }

    public String getTitle() {
        return title;
    }

    public String getBio() {
        return bio;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isAbcMember() {
        return isAbcMember;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public List<EducationEntity> getEducation() {
        return education;
    }

    public List<WorkExperienceEntity> getWorkExperience() {
        return workExperience;
    }

    public List<CertificatesEntity> getCertificates() {
        return certificates;
    }

    public ProfileStatus getProfileStatus() {
        return profileStatus;
    }

    public void setSub(UUID sub) {
        this.sub = sub;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAbcMember(boolean abcMember) {
        isAbcMember = abcMember;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setEducation(List<EducationEntity> education) {
        this.education = education;
    }

    public void setWorkExperience(List<WorkExperienceEntity> workExperience) {
        this.workExperience = workExperience;
    }

    public void setCertificates(List<CertificatesEntity> certificates) {
        this.certificates = certificates;
    }

    public void setProfileStatus(ProfileStatus profileStatus) {
        this.profileStatus = profileStatus;
    }


}
