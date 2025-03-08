package com.jobmatrix.jm_profile_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name ="clients")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ClientEntity {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID user_id;

    @Column(name ="phone_number")
    private String phoneNumber;

    @Column(name ="bio")
    private String bio;

    @Column(name = "profile_photo_url")
    private String profilePhotoURL;

    @Column(name ="company_name")
    private String companyName;

    @Column(name = "company_size")
    private String companySize;

    @Column(name ="industry")
    private String industry;

    @Column(name = "timezone")
    private String timeZone;

    @Column(name = "city")
    private String city;

    @Column(name="state")
    private String state;

    @Column(name ="country")
    private String country;

    @Column(name ="postal_code")
    private String postalCode;

    @Column(name ="address")
    private String address;

    @Column(name="created_at")
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    public ClientEntity(String phoneNumber, String bio, String profilePhotoURL, String companyName, String companySize, String industry, String timeZone, String city, String state, String country, String postalCode, String address) {
        this.phoneNumber = phoneNumber;
        this.bio = bio;
        this.profilePhotoURL = profilePhotoURL;
        this.companyName = companyName;
        this.companySize = companySize;
        this.industry = industry;
        this.timeZone = timeZone;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.address = address;
    }
}
