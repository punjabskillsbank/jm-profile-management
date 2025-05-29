package com.jobmatrix.serviceimpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import java.lang.reflect.Field;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceImplTest {

    @Mock
    private S3Presigner mockS3Presigner;

    @InjectMocks
    private S3ServiceImpl s3ServiceImpl;

    @BeforeEach
    void setUp() throws Exception {
        // Set the private bucketName using reflection
        Field field = S3ServiceImpl.class.getDeclaredField("bucketName");
        field.setAccessible(true);
        field.set(s3ServiceImpl, "dummy-bucket");
    }

    @Test
    void testGeneratePresignedUploadUrl() {

        String fileName = "test.jpg";
        String contentType = "image/jpeg";
        URL expectedUrl = mock(URL.class);

        PresignedPutObjectRequest mockPresignedPutObjectRequest = mock(PresignedPutObjectRequest.class);

        when(mockPresignedPutObjectRequest.url()).thenReturn(expectedUrl);

        when(mockS3Presigner.presignPutObject(any(PutObjectPresignRequest.class)))
                .thenReturn(mockPresignedPutObjectRequest);

        URL result = s3ServiceImpl.generatePresignedUploadUrl(fileName, contentType);

        assertEquals(expectedUrl, result);
        verify(mockS3Presigner, times(1)).presignPutObject(any(PutObjectPresignRequest.class));
    }
}
