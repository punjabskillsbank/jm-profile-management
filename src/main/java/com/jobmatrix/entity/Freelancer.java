package com.jobmatrix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "freelancers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Freelancer {

    @Id
<<<<<<< HEAD
    @Column(nullable = false, name = "freelancer_id", updatable = false)
=======
    @Column(nullable = false, name = "user_id", updatable = false)
>>>>>>> 4081ec6b1190e2aed7942a0e690182794161589b
    private UUID freelancerId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(nullable = false)
    private Double hourlyRate;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String phoneNumber;

    private String profilePhotoURL;

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL)
    private List<Education> educations;

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL)
    private List<Job> jobs;

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL)
    private List<Certificate> certificates;

    @Column(nullable = false)
    private String profileStatus;

    @Column(nullable = false)
    private Boolean isAbcMember;

}
