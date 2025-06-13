package com.jobmatrix.dto;


import com.common.dto.FreelancerServicesUpdateRequestDTO;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FreelancerServicesUpdateRequestDTOTest {

    @Test
    void testBuilderAndGetters() {
        // Test with non-empty set
        Set<Long> categoryIds = Set.of(1L, 2L, 3L);
        
        FreelancerServicesUpdateRequestDTO dto = FreelancerServicesUpdateRequestDTO.builder()
                .categoryIds(categoryIds)
                .build();

        assertNotNull(dto);
        assertNotNull(dto.getCategoryIds());
        assertEquals(3, dto.getCategoryIds().size());
        assertTrue(dto.getCategoryIds().containsAll(categoryIds));
        assertTrue(dto.getCategoryIds().contains(1L));
        assertTrue(dto.getCategoryIds().contains(2L));
        assertTrue(dto.getCategoryIds().contains(3L));
    }

    @Test
    void testBuilderWithEmptySet() {
        Set<Long> emptySet = Set.of();
        
        FreelancerServicesUpdateRequestDTO dto = FreelancerServicesUpdateRequestDTO.builder()
                .categoryIds(emptySet)
                .build();

        assertNotNull(dto);
        assertNotNull(dto.getCategoryIds());
        assertTrue(dto.getCategoryIds().isEmpty());
    }

    @Test
    void testBuilderChaining() {
        Set<Long> categoryIds = Set.of(1L, 2L, 3L);
        
        FreelancerServicesUpdateRequestDTO dto = FreelancerServicesUpdateRequestDTO.builder()
                .categoryIds(categoryIds)
                .categoryIds(categoryIds)
                .build();

        assertNotNull(dto);
        assertNotNull(dto.getCategoryIds());
        assertEquals(3, dto.getCategoryIds().size());
        assertTrue(dto.getCategoryIds().containsAll(categoryIds));
    }

}
