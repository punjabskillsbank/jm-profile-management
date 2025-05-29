package com.jobmatrix.dto;

import com.common.dto.FreelancerDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.net.URL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FreelancerProfileCreationResponse {
    private FreelancerDTO freelancerDTO;
    private URL presignedUrl;
}
