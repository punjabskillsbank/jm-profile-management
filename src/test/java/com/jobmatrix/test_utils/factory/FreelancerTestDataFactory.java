package com.jobmatrix.test_utils.factory;

import com.jobmatrix.dto.CertificateDTO;
import com.jobmatrix.dto.EducationDTO;
import com.jobmatrix.dto.FreelancerDTO;
import com.jobmatrix.dto.JobDTO;
import com.common.enums.ProfileStatus;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FreelancerTestDataFactory {

    public static FreelancerDTO createFreelancerDTO(UUID freelancerId) {
        return FreelancerDTO.builder()
                .freelancerId(freelancerId)
                .title("Senior Software Engineer")
                .bio("Experienced Java and Spring Boot developer")
                .hourlyRate(50.0)
                .address("123, MG Road")
                .city("Bangalore")
                .state("Karnataka")
                .country("India")
                .postalCode("560001")
                .phoneNumber("+919876543210")
                .isAbcMember(true)
                .profilePhotoURL("https://example.com/profile.jpg")
                .educations(createEducationList(freelancerId))
                .jobs(createJobList(freelancerId))
                .certificates(createCertificateList(freelancerId))
                .profileStatus(ProfileStatus.APPROVED)
                .build();
    }

    private static List<EducationDTO> createEducationList(UUID freelancerId) {
        return Arrays.asList(
                new EducationDTO(1L, "B.Tech", "Computer Science", "IIT Delhi", 2015, 2019, freelancerId)
        );
    }

    private static List<JobDTO> createJobList(UUID freelancerId) {
        return Arrays.asList(
                new JobDTO(1L, "Software Engineer", "Google", Date.valueOf("2020-01-01"), Date.valueOf("2023-06-01"),
                        "Developed scalable backend systems", freelancerId)
        );
    }

    private static List<CertificateDTO> createCertificateList(UUID freelancerId) {
        return Arrays.asList(
                new CertificateDTO(1L, "AWS Certified Developer", "AWS", Date.valueOf("2021-05-10"),
                        Date.valueOf("2024-05-10"), "https://aws.com/cert/12345", freelancerId)
        );
    }
}
