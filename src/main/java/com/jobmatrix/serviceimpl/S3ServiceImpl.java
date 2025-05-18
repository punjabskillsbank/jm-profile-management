package com.jobmatrix.serviceimpl;

import com.jobmatrix.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private static final Logger logger = Logger.getLogger(S3ServiceImpl.class);
    private static final Duration UPLOAD_URL_EXPIRATION = Duration.ofMinutes(60);

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Override
    public URL generatePresignedUploadUrl(String fileName, String contentType) {


        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build()) {

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