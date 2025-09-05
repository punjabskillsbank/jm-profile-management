package com.jobmatrix.service;

import com.common.dto.PresignedUrlResponseDTO;

public interface FileService {

    /**
     * Generates presigned URLs for uploading and downloading a profile photo
     *
     * @param userId      the ID of the user
     * @param contentType the MIME type of the file
     * @return an array containing [uploadUrl, downloadUrl]
     */
    PresignedUrlResponseDTO generateProfilePhotoUrl(String userId, String contentType);

}