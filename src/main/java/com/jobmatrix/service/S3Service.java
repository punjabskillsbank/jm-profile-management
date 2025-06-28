package com.jobmatrix.service;

import java.io.InputStream;
import java.net.URL;

public interface S3Service {

    /**
     * Generates a presigned URL for uploading a file to S3
     *
     * @param fileName the name to give the file in S3
     * @param contentType the MIME type of the file
     * @return the presigned URL for uploading
     */
    URL generatePresignedUploadUrl(String fileName, String contentType);

}