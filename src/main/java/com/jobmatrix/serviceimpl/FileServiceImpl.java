package com.jobmatrix.serviceimpl;

import com.jobmatrix.dto.PresignedUrlResponse;
import com.jobmatrix.repository.ClientProfileRepository;
import com.jobmatrix.service.FileService;
import com.jobmatrix.service.S3Service;
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
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/bmp", "image/webp","text/plain"
    );
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    private final S3Service s3Service;
    private final ClientProfileRepository clientProfileRepository;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public PresignedUrlResponse generateProfilePhotoUrls(String userId, String contentType) {
        validateContentType(contentType);
        
        // Generate a unique filename for the profile photo
        String s3Key = "profile_photos/" + userId + getFileExtension(contentType);
        
        // Generate presigned URLs
        URL uploadUrl = s3Service.generatePresignedUploadUrl(s3Key, contentType);
        
        return new PresignedUrlResponse(uploadUrl, s3Key);
    }

    private void validateContentType(String contentType) {
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Only image files (JPEG, JPG, PNG, GIF, BMP, WEBP) are allowed");
        }
    }

    private String getFileExtension(String contentType) {
        switch (contentType.toLowerCase()) {
            case "image/jpeg":
            case "image/jpg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "image/gif":
                return ".gif";
            case "image/bmp":
                return ".bmp";
            case "image/webp":
                return ".webp";
            case "text/plain":
                return ".txt";
            default:
                throw new IllegalArgumentException("Unsupported image type: " + contentType);
        }
    }
}