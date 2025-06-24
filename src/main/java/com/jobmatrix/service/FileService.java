package com.jobmatrix.service;

import com.jobmatrix.dto.PresignedUrlResponse;

public interface FileService {

    /**
     * Generates presigned URLs for uploading and downloading a profile photo
     *
     * @param userId      the ID of the user
     * @param contentType the MIME type of the file
     * @return an array containing [uploadUrl, downloadUrl]
     */
    PresignedUrlResponse generateProfilePhotoUrl(String userId, String contentType);

}