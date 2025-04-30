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

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProfilePhoto(
            @RequestParam("fileUrl") String fileUrl
    ) {
        boolean deleted = fileService.deleteProfilePhoto(fileUrl);
        return deleted
                ? ResponseEntity.ok("File deleted successfully.")
                : ResponseEntity.badRequest().body("Invalid file URL or deletion failed.");
    }

    @GetMapping("/object-key")
    public ResponseEntity<String> getObjectKeyFromUrl(
            @RequestParam("fileUrl") String fileUrl
    ) {
        String objectKey = fileService.getObjectKeyFromUrl(fileUrl);
        return ResponseEntity.ok(objectKey);
    }

    @GetMapping("/refresh-url")
    public ResponseEntity<String> refreshProfilePhotoUrl(
            @RequestParam("objectKey") String objectKey
    ) {
        String refreshedUrl = fileService.refreshProfilePhotoUrl(objectKey);
        return ResponseEntity.ok(refreshedUrl);
    }
}
