package com.jobmatrix.controller;

import com.jobmatrix.dto.PresignedUrlResponse;
import com.jobmatrix.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/presigned_url")
@RequiredArgsConstructor
public class PresignedUrlController {

    private final FileService fileService;

    @GetMapping("/upload")
     public ResponseEntity<PresignedUrlResponse> generateUploadUrl(@RequestParam String userId, @RequestParam String contentType) {
         PresignedUrlResponse presignedUrlResponse = fileService.generateProfilePhotoUrl(userId, contentType);
         return ResponseEntity.ok(presignedUrlResponse);
     }
}
