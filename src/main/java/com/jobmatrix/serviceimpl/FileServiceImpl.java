package com.jobmatrix.serviceimpl;

import com.common.util.S3PresignedURLUtil;
import com.common.dto.PresignedUrlResponseDTO;
import com.jobmatrix.repository.ClientProfileRepository;
import com.jobmatrix.service.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private static final Logger logger = Logger.getLogger(FileServiceImpl.class);
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/webp"
    );
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    private final S3PresignedURLUtil s3Service;
    private final ClientProfileRepository clientProfileRepository;

    @Override
    public PresignedUrlResponseDTO generateProfilePhotoUrl(String userId, String contentType) {
        validateContentType(contentType);

        // creates the S3 key for the profile photo
        String s3Key = "profile_photos/" + userId + getFileExtension(contentType);

        // Generate presigned URL for uploading the profile photo
        URL uploadUrl = s3Service.generatePresignedUploadUrl(s3Key, contentType);

        return new PresignedUrlResponseDTO(uploadUrl, s3Key);
    }

    private void validateContentType(String contentType) {
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Only image files (JPEG, JPG, PNG, WEBP) are allowed");
        }
    }

    private String getFileExtension(String contentType) {
        switch (contentType.toLowerCase()) {
            case "image/jpeg":
            case "image/jpg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "image/webp":
                return ".webp";
            default:
                throw new IllegalArgumentException("Unsupported image type: " + contentType);
        }
    }
}