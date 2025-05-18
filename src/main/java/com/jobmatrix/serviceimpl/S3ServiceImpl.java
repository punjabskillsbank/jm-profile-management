package com.jobmatrix.serviceimpl;

import com.jobmatrix.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private static final Logger logger = Logger.getLogger(S3ServiceImpl.class);
    private static final Duration UPLOAD_URL_EXPIRATION = Duration.ofMinutes(60);

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    @Override
    public URL generatePresignedUploadUrl(String fileName, String contentType) {
        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .build()) { // No credentialsProvider here

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(UPLOAD_URL_EXPIRATION)
                    .putObjectRequest(objectRequest)
                    .build();

            URL presignedUrl = presigner.presignPutObject(presignRequest).url();
            logger.info("Generated presigned upload URL for file: " + fileName);
            return presignedUrl;
        }
    }
}
