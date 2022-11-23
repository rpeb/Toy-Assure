package com.increff.assure.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.profiles.ProfileFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;

@Service
public class S3Service {

    @Value("${profile-name}")
    String PROFILE;

    Region region = Region.AP_SOUTH_1;

    AwsCredentialsProvider credentialsProvider = ProfileCredentialsProvider
            .builder()
            .profileFile(ProfileFile.defaultProfileFile())
            .profileName(PROFILE)
            .build();

    S3Client s3Client = S3Client.builder().region(region)
            .credentialsProvider(credentialsProvider).build();

    public void setup(String bucketName) throws InvoiceUploadException {
        if (checkBucketExists(bucketName)) {
            return;
        }
        try {
            s3Client.createBucket(CreateBucketRequest
                    .builder()
                    .bucket(bucketName)
                    .createBucketConfiguration(
                            CreateBucketConfiguration.builder()
                                    .locationConstraint(region.id())
                                    .build())
                    .build());
            s3Client.waiter().waitUntilBucketExists(HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build());
        } catch (S3Exception e) {
            throw new InvoiceUploadException(e.awsErrorDetails().errorMessage());
        }

    }

    public String upload(String bucketName, File file, String key) {
        s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(key).acl(ObjectCannedACL.PUBLIC_READ)
                        .build(),
                RequestBody.fromFile(file));
        return String.format("https://%s.s3-ap-south-1.amazonaws.com/%s", bucketName, key);
    }

    private boolean checkBucketExists(String bucket) {
        HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                .bucket(bucket)
                .build();
        try {
            s3Client.headBucket(headBucketRequest);
            return true;
        } catch (NoSuchBucketException e) {
            return false;
        }
    }

}
