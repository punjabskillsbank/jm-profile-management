package com.jobmatrix.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    
    /**
     * Uploads a profile photo and returns the URL to access the file
     * 
     * @param file the file to upload
     * @param userId the ID of the user
     * @param userType the type of user ('client' or 'freelancer')
     * @return the URL to access the uploaded file
     */
    String uploadProfilePhoto(MultipartFile file, String userId, String userType);
    
    /**
     * Deletes a profile photo
     * 
     * @param fileUrl the URL of the file to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteProfilePhoto(String fileUrl);
    
    /**
     * Extracts the S3 object key from a URL
     * 
     * @param fileUrl the URL to extract the key from
     * @return the S3 object key
     */
    String getObjectKeyFromUrl(String fileUrl);
    
    /**
     * Refreshes a pre-signed URL for a profile photo
     * 
     * @param objectKey the S3 object key
     * @return a new pre-signed URL for the object
     */
    String refreshProfilePhotoUrl(String objectKey);
} 