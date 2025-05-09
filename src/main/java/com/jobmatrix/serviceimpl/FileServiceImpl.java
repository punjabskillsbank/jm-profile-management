package com.jobmatrix.serviceimpl;

import com.jobmatrix.repository.ClientProfileRepository;
import com.jobmatrix.service.FileService;
import com.jobmatrix.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private static final Logger logger = Logger.getLogger(FileServiceImpl.class);
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp"
    );
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    private final S3Service s3Service;
    private final ClientProfileRepository clientProfileRepository;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public String uploadProfilePhoto(MultipartFile file, String userId, String userType) {

        validateFile(file);

        try {
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String filename = userType + "/profiles/" + userId + "/" + originalFilename;

            String fileUrl =  s3Service.uploadFile(
                    filename,
                    file.getInputStream(),
                    file.getContentType()
            );

            clientProfileRepository.findById(UUID.fromString(userId)).ifPresent(user -> {
                user.setProfilePhotoURL(fileUrl);
                clientProfileRepository.save(user);
            });
            return fileUrl;

        } catch (IOException e) {
            logger.error("Error reading file: " + e.getMessage(), e);
            throw new RuntimeException("Failed to read uploaded file", e);
        }
    }


    /**
     * Validates if the file is a valid image and within size limits
     *
     * @param file the file to validate
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size of 5MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Only image files (JPEG, PNG, GIF, BMP, WEBP) are allowed");
        }
    }
}