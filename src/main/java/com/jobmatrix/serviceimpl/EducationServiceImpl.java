package com.jobmatrix.serviceimpl;

import com.jobmatrix.dto.EducationDTO;
import com.jobmatrix.service.EducationService;

public class EducationServiceImpl implements EducationService {

    @Override
    public EducationDTO createEducation(EducationDTO educationDTO) {
        // Validate the education years
        validateEducationYears(educationDTO.getStartYear(), educationDTO.getEndYear());
        return educationDTO;
    }

    private void validateEducationYears(int startYear, int endYear) {
        if (endYear < startYear) {
            throw new IllegalArgumentException("End year must be greater than or equal to start year.");
        }
    }
}