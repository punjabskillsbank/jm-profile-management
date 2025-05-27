package com.jobmatrix.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FreelancerServicesDTO {

    @NotNull(message = "freelancer_id cannot be null.")
    private UUID freelancerId;

    @NotNull(message = "category_id cannot be null.")
    private Long categoryId;

}
