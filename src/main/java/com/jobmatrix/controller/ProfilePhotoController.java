package com.jobmatrix.controller;

import com.jobmatrix.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class ProfilePhotoController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadProfilePhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId,
            @RequestParam("userType") String userType
    ) {
        String fileUrl = fileService.uploadProfilePhoto(file, userId, userType);
        return ResponseEntity.ok(fileUrl);
    }
}