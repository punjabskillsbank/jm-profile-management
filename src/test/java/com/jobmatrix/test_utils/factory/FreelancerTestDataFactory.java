package com.jobmatrix.test_utils.factory;

import com.common.dto.CertificateDTO;
import com.common.dto.EducationDTO;
import com.common.dto.CategoryDTO;
import com.common.dto.FreelancerDTO;
import com.common.dto.JobDTO;
import com.common.entity.Certificate;
import com.common.entity.Education;
import com.common.entity.Freelancer;
import com.common.entity.Job;
import com.common.entity.Category; // Added for Category entity
import com.common.enums.ProfileStatus;
import com.common.enums.ProfileVisibility;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet; // Added for HashSet
import java.util.List;
import java.util.Set; // Added for Set
import java.util.UUID;

public class FreelancerTestDataFactory {

    public static Freelancer createFreelancerEntity(UUID freelancerId) {
        return Freelancer.builder()
                .freelancerId(freelancerId)
                .title("Senior Software Engineer")
                .bio("Experienced Java and Spring Boot developer")
                .hourlyRate(50)
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
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                .categories(createCategoryEntitySet()) // Add categories for entity
                .build();
    }

    public static FreelancerDTO createFreelancerDTO(UUID freelancerId) {
        return FreelancerDTO.builder()
                .freelancerId(freelancerId)
                .title("Senior Software Engineer")
                .bio("Experienced Java and Spring Boot developer")
                .hourlyRate(50)
                .address("123, MG Road")
                .city("Bangalore")
                .state("Karnataka")
                .country("India")
                .postalCode("560001")
                .phoneNumber("+919876543210")
                .isAbcMember(true)
                .profilePhotoURL("https://example.com/profile.jpg")
                .educations(createEducationListDTO(freelancerId))
                .jobs(createJobListDTO(freelancerId))
                .certificates(createCertificateListDTO(freelancerId))
                .profileStatus(ProfileStatus.APPROVED)
                .categoriesDTO(createCategoryDTOSet()) // Add categories, now a Set
                .profileVisibility(ProfileVisibility.PUBLIC)
                .build();
    }

    private static Set<CategoryDTO> createCategoryDTOSet() {
        // Using actual field names from CategoryDTO: categoryId and category
        return new HashSet<>(Arrays.asList(
                CategoryDTO.builder().categoryId(1L).category("Software Development").build(),
                CategoryDTO.builder().categoryId(2L).category("Java Programming").build()
        ));
    }

    private static Set<Category> createCategoryEntitySet() {
        return new HashSet<>(Arrays.asList(
                Category.builder().categoryId(1L).category("Software Development").build(),
                Category.builder().categoryId(2L).category("Java Programming").build()
        ));
    }

    private static List<Education> createEducationList(UUID freelancerId) {
        return Arrays.asList(
                new Education(1L, "B.Tech", "Computer Science", "IIT Delhi", 2015, 2019, freelancerId,
                              Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()),null)
        );
    }

    private static List<EducationDTO> createEducationListDTO(UUID freelancerId) {
        return Arrays.asList(
                new EducationDTO("B.Tech", "Computer Science", "IIT Delhi", 2015, 2019, freelancerId)
        );
    }

    private static List<Job> createJobList(UUID freelancerId) {
        return Arrays.asList(
                new Job(1L, "Software Engineer", "Google", Date.valueOf("2020-01-01"), Date.valueOf("2023-06-01"),
                        "Developed scalable backend systems", freelancerId, Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()),null)
        );
    }

    private static List<JobDTO> createJobListDTO(UUID freelancerId) {
        return Arrays.asList(
                new JobDTO("Software Engineer", "Google", Date.valueOf("2020-01-01"), Date.valueOf("2023-06-01"),
                        "Developed scalable backend systems", freelancerId)
        );
    }

    private static List<Certificate> createCertificateList(UUID freelancerId) {
        return Arrays.asList(
                new Certificate(1L, "AWS Certified Developer", "AWS", Date.valueOf("2021-05-10"),
                        Date.valueOf("2024-05-10"), "https://aws.com/cert/12345", freelancerId, Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()),null)
        );
    }

    private static List<CertificateDTO> createCertificateListDTO(UUID freelancerId) {
        return Arrays.asList(
                new CertificateDTO("AWS Certified Developer", "AWS", Date.valueOf("2021-05-10"),
                        Date.valueOf("2024-05-10"), "https://aws.com/cert/12345", freelancerId)
        );
    }
}
