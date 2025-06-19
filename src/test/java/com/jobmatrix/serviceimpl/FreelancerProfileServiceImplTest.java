package com.jobmatrix.serviceimpl;
import com.common.dto.FreelancerDTO;
import com.common.dto.ProfileVisibilityDTO;
import com.common.entity.Freelancer;
import com.common.enums.ProfileVisibility;
import com.common.exceptionHandling.FreelancerNotFoundException;
import com.jobmatrix.repository.FreelancerRepository;
import com.jobmatrix.service.FileService;
import com.jobmatrix.repository.CategoryRepository;
import com.jobmatrix.test_utils.factory.FreelancerTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.net.MalformedURLException;
import java.util.Optional;
import java.util.UUID;
import java.util.*;
import com.common.dto.CategoryDTO;
import com.common.entity.Category;
import com.jobmatrix.exceptionHandling.CategoryNotFound;
import com.jobmatrix.exceptionHandling.NullCategoriesOfferedException;
import com.jobmatrix.exceptionHandling.CategoriesOfferedLimitExceededException;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class FreelancerProfileServiceImplTest {

    @Mock
    private FreelancerRepository freelancerRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private FileService fileService;


    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private FreelancerProfileServiceImpl freelancerProfileService;
    private final UUID FREELANCER_ID = UUID.randomUUID();
    private FreelancerDTO freelancerDTO;
    private Freelancer freelancerEntity;
    private FreelancerDTO inputFreelancerDTO;

    @BeforeEach
    void setup() {
        freelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
        inputFreelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
        freelancerEntity = FreelancerTestDataFactory.createFreelancerEntity(FREELANCER_ID);
    }

    @Test
    void createFreelancerProfile_success_shouldMapAndSaveAndReturnDTOWithCategories() {
        // Arrange
        // inputFreelancerDTO and freelancerEntity are initialized with categories via FreelancerTestDataFactory

        // 1. Mock initial DTO to Entity mapping for Freelancer's main fields
        doReturn(freelancerEntity).when(modelMapper).map(inputFreelancerDTO, Freelancer.class);

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
                .profilePhotoS3Key(freelancerEntity.getProfilePhotoS3Key())
                .profileStatus(freelancerEntity.getProfileStatus())
                // CategoriesDTO is NOT set here, as the service does it in a subsequent step
                .build();
        doReturn(baseMappedResponseDTO).when(modelMapper).map(any(Freelancer.class), eq(FreelancerDTO.class));

        // 5. Mock mapping of individual Category entities (from saved Freelancer) to CategoryDTOs
        // These are the CategoryDTOs associated with inputFreelancerDTO from the factory
        Set<CategoryDTO> expectedResponseCategoryDTOs = inputFreelancerDTO.getCategoriesDTO();
        for (CategoryDTO catDTO : expectedResponseCategoryDTOs) {
            Category correspondingEntity = expectedPersistedCategories.stream()
                .filter(e -> e.getCategoryId().equals(catDTO.getCategoryId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Mismatch between test DTO and Entity categories for mocking"));
            doReturn(catDTO).when(modelMapper).map(correspondingEntity, CategoryDTO.class);
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
        doReturn(expectedDTO).when(modelMapper).map(freelancer, FreelancerDTO.class);
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
        // Use existing freelancer from setup()
        when(freelancerRepository.findById(FREELANCER_ID)).thenReturn(Optional.of(freelancerEntity));

        // Mock categories
        Set<Category> categories = new HashSet<>();
        Set<Long> categoryIds = Set.of(1L, 2L); // Define categoryIds here
        for (Long categoryId : categoryIds) {
            Category category = new Category();
            category.setCategoryId(categoryId);
            categories.add(category);
            when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        }
        // Mock save operation
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(freelancerEntity);

        // Mock DTO mapping
        FreelancerDTO expectedDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);

        // Mock category DTO mapping
        Set<CategoryDTO> categoryDTOs = new HashSet<>();
        for (Category category : categories) {
            CategoryDTO categoryDTO = CategoryDTO.builder()
                .categoryId(category.getCategoryId())
                .category("Category " + category.getCategoryId())
                .build();
            categoryDTOs.add(categoryDTO);
            doReturn(categoryDTO).when(modelMapper).map(eq(category), eq(CategoryDTO.class));
        }

        // Set categories in the expected DTO
        expectedDTO.setCategoriesDTO(categoryDTOs);

        // Mock final DTO mapping
        doReturn(expectedDTO).when(modelMapper).map(freelancerEntity, FreelancerDTO.class);

        // Act
        FreelancerDTO result = freelancerProfileService.updateCategories(FREELANCER_ID, categoryIds);

        // Assert
        assertNotNull(result);
        assertEquals(FREELANCER_ID, result.getFreelancerId());
        assertNotNull(result.getCategoriesDTO());
        assertEquals(categories.size(), result.getCategoriesDTO().size());
        assertTrue(result.getCategoriesDTO().containsAll(categoryDTOs));

        // Verify interactions
        verify(freelancerRepository).findById(FREELANCER_ID);
        for (Long categoryId : categoryIds) {
            verify(categoryRepository).findById(categoryId);
        }
        verify(freelancerRepository).save(any(Freelancer.class));
        verify(modelMapper).map(freelancerEntity, FreelancerDTO.class);
        for (Category category : categories) {
            verify(modelMapper).map(eq(category), eq(CategoryDTO.class));
        }
    }

    @Test
    void completeFlow_createAndUpdateCategories() {
        // Arrange
        // Initial categories: 1, 3, 7
        Set<CategoryDTO> initialCategoryDTOs = Set.of(
                CategoryDTO.builder().categoryId(1L).category("Category 1").build(),
                CategoryDTO.builder().categoryId(3L).category("Category 3").build(),
                CategoryDTO.builder().categoryId(7L).category("Category 7").build()
        );

        // Create initial freelancer DTO with categories
        FreelancerDTO initialFreelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
        initialFreelancerDTO.setCategoriesDTO(initialCategoryDTOs);

        // Mock initial categories
        Set<Category> initialCategories = new HashSet<>();
        for (CategoryDTO dto : initialCategoryDTOs) {
            Category category = new Category();
            category.setCategoryId(dto.getCategoryId());
            category.setCategory(dto.getCategory());
            initialCategories.add(category);
            when(categoryRepository.findById(dto.getCategoryId())).thenReturn(Optional.of(category));
        }

        // Mock initial DTO to entity mapping
        Freelancer initialEntity = FreelancerTestDataFactory.createFreelancerEntity(FREELANCER_ID);
        when(modelMapper.map(initialFreelancerDTO, Freelancer.class)).thenReturn(initialEntity);

        // Mock initial save
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(initialEntity);

        // Mock DTO mapping for initial creation
        FreelancerDTO createdDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);

        // Set categories in the entity before mapping
        initialEntity.setCategories(initialCategories);

        // Mock the complete DTO mapping for creation
        when(modelMapper.map(initialEntity, FreelancerDTO.class)).thenReturn(createdDTO);

        // Mock category DTO mapping for creation
        for (Category category : initialCategories) {
            CategoryDTO categoryDTO = CategoryDTO.builder()
                    .categoryId(category.getCategoryId())
                    .category(category.getCategory())
                    .build();
            when(modelMapper.map(category, CategoryDTO.class)).thenReturn(categoryDTO);
        }

        // Act - Create freelancer
        FreelancerDTO actualCreatedDTO = freelancerProfileService.createFreelancerProfile(initialFreelancerDTO);

        // Assert - Verify creation
        assertNotNull(actualCreatedDTO);
        assertEquals(FREELANCER_ID, actualCreatedDTO.getFreelancerId());
        assertNotNull(actualCreatedDTO.getCategoriesDTO());
        assertEquals(3, actualCreatedDTO.getCategoriesDTO().size());
        assertTrue(actualCreatedDTO.getCategoriesDTO().stream()
                .map(CategoryDTO::getCategoryId)
                .allMatch(id -> id == 1L || id == 3L || id == 7L));

        // Arrange - Update categories: 2, 5, 7
        Set<Long> newCategoryIds = Set.of(2L, 5L, 7L);

        // Mock new categories
        Set<Category> newCategories = new HashSet<>();
        for (Long categoryId : newCategoryIds) {
            Category category = new Category();
            category.setCategoryId(categoryId);
            category.setCategory("Category " + categoryId);
            newCategories.add(category);
            when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        }

        // Mock save operation for update
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(initialEntity);

        // Mock the repository to return the entity when finding by ID
        when(freelancerRepository.findById(FREELANCER_ID)).thenReturn(Optional.of(initialEntity));

        // Mock DTO mapping for update
        FreelancerDTO updatedDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);

        // Mock category DTO mapping for update
        Set<CategoryDTO> expectedCategoryDTOs = new HashSet<>();
        for (Category category : newCategories) {
            CategoryDTO categoryDTO = CategoryDTO.builder()
                    .categoryId(category.getCategoryId())
                    .category(category.getCategory())
                    .build();
            expectedCategoryDTOs.add(categoryDTO);
            when(modelMapper.map(category, CategoryDTO.class)).thenReturn(categoryDTO);
        }

        // Set categories in the entity before mapping for update
        initialEntity.setCategories(newCategories);

        // Mock the complete DTO mapping for update
        when(modelMapper.map(initialEntity, FreelancerDTO.class)).thenReturn(updatedDTO);

        // Set the categories on the DTO directly
        updatedDTO.setCategoriesDTO(expectedCategoryDTOs);

        // Act - Update categories
        FreelancerDTO actualUpdatedDTO = freelancerProfileService.updateCategories(FREELANCER_ID, newCategoryIds);

        // Assert - Verify update
        assertNotNull(actualUpdatedDTO);
        assertEquals(FREELANCER_ID, actualUpdatedDTO.getFreelancerId());
        assertNotNull(actualUpdatedDTO.getCategoriesDTO());
        assertEquals(3, actualUpdatedDTO.getCategoriesDTO().size());
        assertTrue(actualUpdatedDTO.getCategoriesDTO().containsAll(expectedCategoryDTOs));

        // Verify that old categories are not present
        assertFalse(actualUpdatedDTO.getCategoriesDTO().stream()
                        .map(CategoryDTO::getCategoryId)
                        .anyMatch(id -> id == 1L || id == 3L),
                "Old categories (1 and 3) should not be present in the result");

        // Verify interactions
        verify(freelancerRepository, times(2)).save(any(Freelancer.class));
        verify(modelMapper, times(2)).map(any(Freelancer.class), eq(FreelancerDTO.class));
        verify(categoryRepository, times(6)).findById(anyLong());
        for (Category category : newCategories) {
            verify(modelMapper).map(category, CategoryDTO.class);
        }
    }
}
