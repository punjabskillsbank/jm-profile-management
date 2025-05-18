package com.jobmatrix.service;

import java.io.InputStream;
import java.net.URL;

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
    //String uploadFile(String fileName, InputStream inputStream, String contentType);

    /**
     * Generates a presigned URL for uploading a file to S3
     *
     * @param fileName the name to give the file in S3
     * @param contentType the MIME type of the file
     * @return the presigned URL for uploading
     */
    URL generatePresignedUploadUrl(String fileName, String contentType);


}
