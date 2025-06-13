package com.jobmatrix.dto;


import com.common.dto.FreelancerServicesUpdateRequestDTO;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FreelancerServicesUpdateRequestDTOTest {

    @Test
    void testBuilderAndGetters() {
        Set<Long> categoryIds = Set.of(1L, 2L, 3L);

        FreelancerServicesUpdateRequestDTO dto = FreelancerServicesUpdateRequestDTO.builder()
                .categoryIds(categoryIds)
                .build();

        assertNotNull(dto);
        assertEquals(3, dto.getCategoryIds().size());
        assertTrue(dto.getCategoryIds().contains(1L));
    }

}
