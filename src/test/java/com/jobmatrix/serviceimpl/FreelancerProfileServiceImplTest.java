package com.jobmatrix.serviceimpl;
import com.common.dto.FreelancerDTO;
import com.common.dto.ProfileVisibilityDTO;
import com.common.entity.Freelancer;
import com.common.enums.ProfileVisibility;
import com.common.exceptionHandling.FreelancerNotFoundException;
import com.jobmatrix.repository.FreelancerRepository;
import com.jobmatrix.repository.CategoryRepository;
import com.jobmatrix.test_utils.factory.FreelancerTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import com.common.dto.CategoryDTO;
import com.common.entity.Category;
import com.jobmatrix.exceptionHandling.CategoryNotFound;
import com.jobmatrix.exceptionHandling.NullCategoriesOfferedException;
import com.jobmatrix.exceptionHandling.CategoriesOfferedLimitExceededException;

import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FreelancerProfileServiceImplTest {

    @Mock
    private FreelancerRepository freelancerRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private FreelancerProfileServiceImpl freelancerProfileService;
    private final UUID FREELANCER_ID = UUID.randomUUID();
    private FreelancerDTO inputFreelancerDTO;
    private Freelancer freelancerEntity;
    private FreelancerDTO freelancerDTO; // This seems unused, can be removed or clarified if needed later

    @BeforeEach
    void setup() {
        inputFreelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
        freelancerEntity = FreelancerTestDataFactory.createFreelancerEntity(FREELANCER_ID);
    }

    @Test
    void createFreelancerProfile_success_shouldMapAndSaveAndReturnDTOWithCategories() {
        // Arrange
        // inputFreelancerDTO and freelancerEntity are initialized with categories via FreelancerTestDataFactory

        // 1. Mock initial DTO to Entity mapping for Freelancer's main fields
        when(modelMapper.map(inputFreelancerDTO, Freelancer.class)).thenReturn(freelancerEntity);

        // 2. Mock CategoryRepository to return category entities when searched by ID
        // These are the Category entities associated with freelancerEntity from the factory
        Set<Category> expectedPersistedCategories = freelancerEntity.getCategories();
        for (Category catEntity : expectedPersistedCategories) {
            when(categoryRepository.findById(catEntity.getCategoryId())).thenReturn(Optional.of(catEntity));
        }

        // 3. Mock FreelancerRepository save operation
        // It should return the entity that was passed to it, which should have categories set by the service.
        when(freelancerRepository.save(any(Freelancer.class))).thenAnswer(invocation -> {
            Freelancer saved = invocation.getArgument(0);
            // Schnell-check: ensure categories were set on the entity passed to save
            assertNotNull(saved.getCategories(), "Categories should be set on Freelancer before saving");
            assertEquals(expectedPersistedCategories.size(), saved.getCategories().size(), "Mismatch in category count before saving");
            return saved; // Return the (mock) saved entity
        });

        // 4. Mock mapping of the saved Freelancer entity back to a FreelancerDTO (for main fields)
        // This DTO represents the state *before* the service manually sets categoriesDTO on it.
        FreelancerDTO baseMappedResponseDTO = FreelancerDTO.builder()
                .freelancerId(freelancerEntity.getFreelancerId())
                .title(freelancerEntity.getTitle())
                .bio(freelancerEntity.getBio())
                .hourlyRate(freelancerEntity.getHourlyRate())
                .address(freelancerEntity.getAddress())
                .city(freelancerEntity.getCity())
                .state(freelancerEntity.getState())
                .country(freelancerEntity.getCountry())
                .postalCode(freelancerEntity.getPostalCode())
                .phoneNumber(freelancerEntity.getPhoneNumber())
                .isAbcMember(freelancerEntity.getIsAbcMember())
                .profilePhotoURL(freelancerEntity.getProfilePhotoURL())
                .profileStatus(freelancerEntity.getProfileStatus())
                // CategoriesDTO is NOT set here, as the service does it in a subsequent step
                .build();
        when(modelMapper.map(any(Freelancer.class), eq(FreelancerDTO.class))).thenReturn(baseMappedResponseDTO);

        // 5. Mock mapping of individual Category entities (from saved Freelancer) to CategoryDTOs
        // These are the CategoryDTOs associated with inputFreelancerDTO from the factory
        Set<CategoryDTO> expectedResponseCategoryDTOs = inputFreelancerDTO.getCategoriesDTO();
        for (CategoryDTO catDTO : expectedResponseCategoryDTOs) {
            Category correspondingEntity = expectedPersistedCategories.stream()
                .filter(e -> e.getCategoryId().equals(catDTO.getCategoryId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Mismatch between test DTO and Entity categories for mocking"));
            when(modelMapper.map(correspondingEntity, CategoryDTO.class)).thenReturn(catDTO);
        }

        // Act
        FreelancerDTO resultDTO = freelancerProfileService.createFreelancerProfile(inputFreelancerDTO);

        // Assert
        assertNotNull(resultDTO, "Result DTO should not be null");
        assertEquals(freelancerEntity.getFreelancerId(), resultDTO.getFreelancerId(), "Freelancer ID mismatch");
        assertEquals(freelancerEntity.getTitle(), resultDTO.getTitle(), "Title mismatch");
        assertEquals(freelancerEntity.getBio(), resultDTO.getBio(), "Bio mismatch");
        // ... (add assertions for other main fields as needed)

        assertNotNull(resultDTO.getCategoriesDTO(), "CategoriesDTO in result should not be null");
        assertEquals(expectedResponseCategoryDTOs.size(), resultDTO.getCategoriesDTO().size(), "CategoryDTO count mismatch");
        assertTrue(resultDTO.getCategoriesDTO().containsAll(expectedResponseCategoryDTOs), "Result DTO missing expected categories");
        assertTrue(expectedResponseCategoryDTOs.containsAll(resultDTO.getCategoriesDTO()), "Result DTO has unexpected categories");

        // Verify interactions
        verify(modelMapper).map(inputFreelancerDTO, Freelancer.class); // Initial DTO -> Entity map
        for (CategoryDTO catDTO : inputFreelancerDTO.getCategoriesDTO()) {
            verify(categoryRepository).findById(catDTO.getCategoryId()); // Category fetches
        }
        verify(freelancerRepository).save(any(Freelancer.class)); // Save freelancer
        verify(modelMapper).map(any(Freelancer.class), eq(FreelancerDTO.class)); // Saved Entity -> DTO map (base)
        // Verify mapping for each category from entity to DTO
        for (Category catEntity : expectedPersistedCategories) { // Iterate over the entities that would have been in 'savedFreelancer.getCategories()'
            verify(modelMapper).map(catEntity, CategoryDTO.class);
        }
    }


    @Test
    void getFreelancerProfileById_shouldReturnFreelancerEntity() {
        UUID freelancerId = UUID.randomUUID();
        Freelancer freelancer = new Freelancer();
        FreelancerDTO expectedDTO = FreelancerTestDataFactory.createFreelancerDTO(freelancerId);
        when(freelancerRepository.findById(freelancerId)).thenReturn(Optional.of(freelancer));
        when(modelMapper.map(freelancer, FreelancerDTO.class)).thenReturn(expectedDTO);
        FreelancerDTO result = freelancerProfileService.getFreelancerProfileById(freelancerId);
        assertNotNull(result);
        assertEquals(expectedDTO, result);
        verify(freelancerRepository).findById(freelancerId);
        verify(modelMapper).map(freelancer, FreelancerDTO.class);

    }
    @Test
    void getFreelancerProfileById_shouldThrowFreelancerNotFoundException() {
        UUID freelancerId = UUID.randomUUID();
        when(freelancerRepository.findById(freelancerId)).thenReturn(Optional.empty());
        FreelancerNotFoundException exception = assertThrows(
                FreelancerNotFoundException.class,
                () -> freelancerProfileService.getFreelancerProfileById(freelancerId)
        );
        assertEquals("Freelancer not found with ID: " + freelancerId, exception.getMessage());
        verify(freelancerRepository).findById(freelancerId);

    }

    @Test
    void createFreelancerProfile_shouldThrowNullCategoriesOfferedException_whenCategoriesAreNull() {
        // Arrange
        inputFreelancerDTO.setCategoriesDTO(null);

        // Act & Assert
        NullCategoriesOfferedException exception = assertThrows(NullCategoriesOfferedException.class, () -> {
            freelancerProfileService.createFreelancerProfile(inputFreelancerDTO);
        });

        assertEquals("Categories cannot be null.", exception.getMessage());
        verifyNoInteractions(freelancerRepository, categoryRepository);
        // Verify modelMapper was not called for the main DTO to entity mapping beyond the initial check by the service
        verify(modelMapper, never()).map(any(FreelancerDTO.class), eq(Freelancer.class));
    }

    @Test
    void createFreelancerProfile_shouldThrowNullCategoriesOfferedException_whenCategoriesAreEmpty() {
        // Arrange
        inputFreelancerDTO.setCategoriesDTO(Collections.emptySet());

        // Act & Assert
        NullCategoriesOfferedException exception = assertThrows(NullCategoriesOfferedException.class, () -> {
            freelancerProfileService.createFreelancerProfile(inputFreelancerDTO);
        });

        assertEquals("Categories cannot be null.", exception.getMessage());
        verifyNoInteractions(freelancerRepository, categoryRepository);
        verify(modelMapper, never()).map(any(FreelancerDTO.class), eq(Freelancer.class));
    }

    @Test
    void createFreelancerProfile_shouldThrowCategoriesOfferedLimitExceededException_whenTooManyCategories() {
        // Arrange
        Set<CategoryDTO> tooManyCategories = new HashSet<>();
        for (long i = 1; i <= 11; i++) {
            tooManyCategories.add(CategoryDTO.builder().categoryId(i).category("Category " + i).build());
        }
        inputFreelancerDTO.setCategoriesDTO(tooManyCategories);

        // Act & Assert
        CategoriesOfferedLimitExceededException exception = assertThrows(CategoriesOfferedLimitExceededException.class, () -> {
            freelancerProfileService.createFreelancerProfile(inputFreelancerDTO);
        });

        assertEquals("Cannot assign more than 10 categories.", exception.getMessage());
        verifyNoInteractions(freelancerRepository, categoryRepository);
        verify(modelMapper, never()).map(any(FreelancerDTO.class), eq(Freelancer.class));
    }

    @Test
    void createFreelancerProfile_shouldThrowCategoryNotFoundException_whenCategoryDoesNotExist() {
        // Arrange
        // inputFreelancerDTO is already populated with categories by setup()
        // Let's assume the first category in the DTO's set is the one that won't be found.
        CategoryDTO nonExistentCategoryDTO = inputFreelancerDTO.getCategoriesDTO().iterator().next();
        Long nonExistentCategoryId = nonExistentCategoryDTO.getCategoryId();

        // Mock the initial DTO to Entity mapping for Freelancer
        when(modelMapper.map(inputFreelancerDTO, Freelancer.class)).thenReturn(freelancerEntity);

        // Mock CategoryRepository: the specific category identified as non-existent is not found.
        when(categoryRepository.findById(nonExistentCategoryId)).thenReturn(Optional.empty());

        // Act & Assert
        CategoryNotFound exception = assertThrows(CategoryNotFound.class, () -> {
            freelancerProfileService.createFreelancerProfile(inputFreelancerDTO);
        });

        assertEquals("Category not found with ID: " + nonExistentCategoryId, exception.getMessage());

        // Verify interactions
        verify(modelMapper).map(inputFreelancerDTO, Freelancer.class); // Initial DTO -> Entity map
        verify(categoryRepository).findById(nonExistentCategoryId); // Verified it was called for the non-existent one
        verify(freelancerRepository, never()).save(any(Freelancer.class)); // Save should not be called
    }

    @Test
    void updateProfileVisibility_shouldUpdateSuccessfully() {
        ProfileVisibilityDTO dto = new ProfileVisibilityDTO();
        dto.setFreelancerId(FREELANCER_ID);
        dto.setProfileVisibility(ProfileVisibility.PUBLIC);

        when(freelancerRepository.findById(FREELANCER_ID)).thenReturn(Optional.of(freelancerEntity));

        freelancerProfileService.updateProfileVisibility(dto);

        verify(freelancerRepository).findById(FREELANCER_ID);
        verify(freelancerRepository).save(freelancerEntity);
        assertEquals(ProfileVisibility.PUBLIC, freelancerEntity.getProfileVisibility());
    }

    @Test
    void updateProfileVisibility_shouldThrowExceptionIfNotFound() {
        ProfileVisibilityDTO dto = new ProfileVisibilityDTO();
        dto.setFreelancerId(FREELANCER_ID);
        dto.setProfileVisibility(ProfileVisibility.PRIVATE);

        when(freelancerRepository.findById(FREELANCER_ID)).thenReturn(Optional.empty());

        assertThrows(FreelancerNotFoundException.class,
                () -> freelancerProfileService.updateProfileVisibility(dto));

        verify(freelancerRepository).findById(FREELANCER_ID);
        verify(freelancerRepository, never()).save(any());
    }

    @Test
    void updateCategories_success_shouldUpdateCategories() {
        // Arrange
        UUID freelancerId = FREELANCER_ID;
        Set<Long> categoryIds = Set.of(1L, 2L);
        
        // Mock existing freelancer
        Freelancer existingFreelancer = FreelancerTestDataFactory.createFreelancerEntity(freelancerId);
        when(freelancerRepository.findById(freelancerId)).thenReturn(Optional.of(existingFreelancer));
        
        // Mock categories
        Set<Category> newCategories = new HashSet<>();
        for (Long categoryId : categoryIds) {
            Category category = new Category();
            category.setCategoryId(categoryId);
            newCategories.add(category);
            when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        }
        
        // Mock save operation
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(existingFreelancer);
        
        // Mock DTO mapping
        FreelancerDTO expectedDTO = FreelancerTestDataFactory.createFreelancerDTO(freelancerId);
        
        // Mock category DTO mapping
        Set<CategoryDTO> expectedCategoryDTOs = new HashSet<>();
        for (Category category : newCategories) {
            CategoryDTO categoryDTO = CategoryDTO.builder()
                .categoryId(category.getCategoryId())
                .category("Category " + category.getCategoryId())
                .build();
            expectedCategoryDTOs.add(categoryDTO);
            when(modelMapper.map(category, CategoryDTO.class)).thenReturn(categoryDTO);
        }
        
        // Mock final DTO mapping
        when(modelMapper.map(existingFreelancer, FreelancerDTO.class)).thenReturn(expectedDTO);

        // Act
        FreelancerDTO result = freelancerProfileService.updateCategories(freelancerId, categoryIds);

        // Assert
        assertNotNull(result);
        assertEquals(freelancerId, result.getFreelancerId());
        assertNotNull(result.getCategoriesDTO());
        assertEquals(newCategories.size(), result.getCategoriesDTO().size());
        assertTrue(result.getCategoriesDTO().containsAll(expectedCategoryDTOs));
        
        // Verify interactions
        verify(freelancerRepository).findById(freelancerId);
        for (Long categoryId : categoryIds) {
            verify(categoryRepository).findById(categoryId);
        }
        verify(freelancerRepository).save(any(Freelancer.class));
        verify(modelMapper).map(existingFreelancer, FreelancerDTO.class);
        for (Category category : newCategories) {
            verify(modelMapper).map(category, CategoryDTO.class);
        }
    }

    @Test
    void updateCategories_shouldThrowFreelancerNotFoundException_whenFreelancerNotFound() {
        // Arrange
        UUID freelancerId = FREELANCER_ID;
        Set<Long> categoryIds = Set.of(1L, 2L);
        
        when(freelancerRepository.findById(freelancerId)).thenReturn(Optional.empty());

        // Act & Assert
        FreelancerNotFoundException exception = assertThrows(FreelancerNotFoundException.class, () -> {
            freelancerProfileService.updateCategories(freelancerId, categoryIds);
        });

        assertEquals("Freelancer not found with ID: " + freelancerId, exception.getMessage());
        verify(freelancerRepository).findById(freelancerId);
        verifyNoInteractions(categoryRepository);
    }

    @Test
    void updateCategories_shouldThrowCategoriesOfferedLimitExceededException_whenTooManyCategories() {
        // Arrange
        UUID freelancerId = FREELANCER_ID;
        Set<Long> tooManyCategoryIds = Set.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L);

        // Act & Assert
        CategoriesOfferedLimitExceededException exception = assertThrows(CategoriesOfferedLimitExceededException.class, () -> {
            freelancerProfileService.updateCategories(freelancerId, tooManyCategoryIds);
        });

        assertEquals("Cannot assign more than 10 categories.", exception.getMessage());
    }

    @Test
    void updateCategories_shouldThrowCategoryNotFound_whenCategoryNotFound() {
        // Arrange
        UUID freelancerId = FREELANCER_ID;
        Set<Long> categoryIds = Set.of(1L, 2L, 3L);
        
        Freelancer existingFreelancer = FreelancerTestDataFactory.createFreelancerEntity(freelancerId);
        when(freelancerRepository.findById(freelancerId)).thenReturn(Optional.of(existingFreelancer));
        
        // Only mock one category to be found
        Category category = new Category();
        category.setCategoryId(1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        
        // The other categories won't be found
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        CategoryNotFound exception = assertThrows(CategoryNotFound.class, () -> {
            freelancerProfileService.updateCategories(freelancerId, categoryIds);
        });

        assertEquals("Category not found with ID: 2", exception.getMessage());
        verify(freelancerRepository).findById(freelancerId);
    }
}
