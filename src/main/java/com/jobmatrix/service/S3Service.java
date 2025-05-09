package com.jobmatrix.service;

import java.io.InputStream;

public interface S3Service {

    /**
     * Uploads a file to S3 bucket and returns the pre-signed URL to access the file
     * This URL will expire after a set period
     *
     * @param fileName the name to give the file in S3
     * @param inputStream the file content as an input stream
     * @param contentType the MIME type of the file
     * @return the pre-signed URL to access the uploaded file
     */
    String uploadFile(String fileName, InputStream inputStream, String contentType);

    /**
     * Deletes a file from S3 bucket
     *
     * @param fileUrl the URL of the file to delete
     * @return true if deletion was successful, false otherwise
     */
   // boolean deleteFile(String fileUrl);

    /**
     * Refreshes a pre-signed URL for an existing S3 object
     * Use this when the original pre-signed URL is about to expire
     *
     * @param objectKey the S3 object key
     * @return a new pre-signed URL for the object
     */
    //String refreshPresignedUrl(String objectKey);
}
