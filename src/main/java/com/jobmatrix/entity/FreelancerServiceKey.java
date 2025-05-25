package com.jobmatrix.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Embeddable
public class FreelancerServiceKey {

    private UUID freelancerId;
    private Long categoryId;

}
