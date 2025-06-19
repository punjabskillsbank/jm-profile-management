package com.jobmatrix.controller;

import com.jobmatrix.dto.PresignedUrlResponse;
import com.jobmatrix.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PresignedUrlController.class)
public class PresignedUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FileService fileService;

    private PresignedUrlResponse presignedUrlResponse;
    private final UUID FREELANCER_ID = UUID.randomUUID();

    @BeforeEach
    void setup() {

        try {
            URL uploadUrl = new URL("https://s3-upload-url");
            presignedUrlResponse = new PresignedUrlResponse();
            presignedUrlResponse.setS3Key("profile-photos/" + FREELANCER_ID + ".jpg");
            presignedUrlResponse.setUploadUrl(uploadUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to construct upload URL", e);
        }
    }

    @Test
    public void testGenerateProfilePhotoUrl() throws Exception {
        // Mock the service call
        when(fileService.generateProfilePhotoUrl(anyString(), anyString())).thenReturn(presignedUrlResponse);

        // Perform the request and verify the response
        mockMvc.perform(get("/api/presigned_url/upload")
                .param("userId", FREELANCER_ID.toString())
                .param("contentType", "image/jpeg"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.s3Key").value(presignedUrlResponse.getS3Key()))
                .andExpect(jsonPath("$.uploadUrl").value(presignedUrlResponse.getUploadUrl().toString()));
    }

}
