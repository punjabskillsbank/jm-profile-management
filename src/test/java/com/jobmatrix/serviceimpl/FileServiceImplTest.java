package com.jobmatrix.serviceimpl;

import com.common.util.S3PresignedURLUtil;
import com.common.dto.PresignedUrlResponseDTO;
import com.jobmatrix.repository.ClientProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileServiceImplTest {

    @Mock
    private S3PresignedURLUtil s3Service;

    @Mock
    private ClientProfileRepository clientProfileRepository;

    @InjectMocks
    private FileServiceImpl fileService;

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/webp"
    );
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;// 10MB
    private PresignedUrlResponseDTO presignedUrlResponse;
    private final UUID FREELANCER_ID = UUID.randomUUID();

    @BeforeEach
    void setup() {

        try {
            URL uploadUrl = new URL("https://s3-upload-url");
            presignedUrlResponse = new PresignedUrlResponseDTO();
            presignedUrlResponse.setS3Key("profile_photos/" + FREELANCER_ID + ".jpg");
            presignedUrlResponse.setUploadUrl(uploadUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to construct upload URL", e);
        }
    }

    @Test
    void generateProfilePhotoUrl_givenValidInput_returnsPresignedUrl() {
        String contentType = "image/jpg";

        when(s3Service.generatePresignedUploadUrl(any(String.class), eq(contentType)))
                .thenReturn(presignedUrlResponse.getUploadUrl());

        // Call the method under test
        PresignedUrlResponseDTO response = fileService.generateProfilePhotoUrl(FREELANCER_ID.toString(), contentType);

        // Validate the response
        assertNotNull(response);
        assertEquals(presignedUrlResponse.getS3Key(), response.getS3Key());
        assertEquals(presignedUrlResponse.getUploadUrl(), response.getUploadUrl());
    }

    @Test
    void generateProfilePhotoUrl_givenInvalidContentType_throwsIllegalArgumentException() {
        String invalidContentType = "application/pdf";

        try {
            fileService.generateProfilePhotoUrl(FREELANCER_ID.toString(), invalidContentType);
        } catch (IllegalArgumentException e) {
            assertEquals("Only image files (JPEG, JPG, PNG, WEBP) are allowed", e.getMessage());
        }
    }

}