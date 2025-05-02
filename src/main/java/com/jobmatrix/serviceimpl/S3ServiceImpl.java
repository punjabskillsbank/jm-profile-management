package com.jobmatrix.serviceimpl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jobmatrix.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private static final Logger logger = Logger.getLogger(S3ServiceImpl.class);

    private final AmazonS3 amazonS3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    // Set pre-signed URL expiration to 7 days (in milliseconds)
    private static final long URL_EXPIRATION = 7 * 24 * 60 * 60 * 1000L;

    @Override
    public String uploadFile(String fileName, InputStream inputStream, String contentType) {
        try {
            // Generate a unique file name to avoid collisions
            String uniqueFileName = generateUniqueFileName(fileName);

            // Set the metadata for the file
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);

            // Create the upload request WITHOUT public read access
            PutObjectRequest uploadRequest = new PutObjectRequest(
                    bucketName,
                    uniqueFileName,
                    inputStream,
                    metadata
            );

            // Upload the file to S3
            amazonS3Client.putObject(uploadRequest);

            // Generate a pre-signed URL with expiration
            String presignedUrl = generatePresignedUrl(uniqueFileName);

            logger.info("File uploaded successfully to S3 with pre-signed URL: " + presignedUrl);

            return presignedUrl;
        } catch (Exception e) {
            logger.error("Error uploading file to S3: " + e.getMessage(), e);
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        try {
            // Extract the file key from the URL
            String fileKey = extractFileKeyFromUrl(fileUrl);

            // Delete the file from S3
            amazonS3Client.deleteObject(bucketName, fileKey);
            logger.info("File deleted successfully from S3: " + fileKey);

            return true;
        } catch (Exception e) {
            logger.error("Error deleting file from S3: " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public String refreshPresignedUrl(String objectKey) {
        // Verify the object exists
        if (!amazonS3Client.doesObjectExist(bucketName, objectKey)) {
            logger.error("Object does not exist: " + objectKey);
            throw new RuntimeException("Failed to refresh URL for non-existent object: " + objectKey);
        }

        // Generate a new pre-signed URL
        String presignedUrl = generatePresignedUrl(objectKey);
        logger.info("Pre-signed URL refreshed for: " + objectKey);

        return presignedUrl;
    }

    /**
     * Generates a pre-signed URL for the given object key
     *
     * @param objectKey the S3 object key
     * @return the pre-signed URL
     */
    private String generatePresignedUrl(String objectKey) {
        Date expiration = new Date(System.currentTimeMillis() + URL_EXPIRATION);
        return amazonS3Client.generatePresignedUrl(
                bucketName,
                objectKey,
                expiration,
                HttpMethod.GET
        ).toString();
    }

    /**
     * Generates a unique file name by adding a UUID to the original file name
     *
     * @param originalFileName the original file name
     * @return a unique file name
     */
    private String generateUniqueFileName(String originalFileName) {
        String extension = "";
        if (originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            originalFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        }
        return originalFileName + "-" + UUID.randomUUID() + extension;
    }

    /**
     * Extracts the file key from the S3 URL
     *
     * @param fileUrl the S3 URL
     * @return the file key
     */
    private String extractFileKeyFromUrl(String fileUrl) {
        // For pre-signed URLs, extraction is more complex
        // Extract the path after the bucket name
        String[] urlParts = fileUrl.split("\\?")[0].split("/");
        StringBuilder key = new StringBuilder();

        boolean bucketFound = false;
        for (String part : urlParts) {
            if (bucketFound) {
                if (key.length() > 0) {
                    key.append("/");
                }
                key.append(part);
            }
            if (part.equals(bucketName)) {
                bucketFound = true;
            }
        }

        return key.toString();
    }
}