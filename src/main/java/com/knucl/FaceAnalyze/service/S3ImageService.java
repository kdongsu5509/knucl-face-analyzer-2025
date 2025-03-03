package com.knucl.FaceAnalyze.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.knucl.FaceAnalyze.myException.ErrorCode;
import com.knucl.FaceAnalyze.myException.S3Exception;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class S3ImageService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    public S3ImageService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }


    /**
     * Method1. upload image to S3
     *
     * @param image(MultipartFile)
     * @return image address
     * @throws S3Exception
     */
    public String upload(MultipartFile image) throws S3Exception, IOException {
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new S3Exception(ErrorCode.EMPTY_FILE_EXCEPTION);
        }

        String fileName = "/" + UUID.randomUUID().toString() + image.getName();
        //메타 데이터 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(image.getContentType());
        objectMetadata.setContentLength(image.getSize());

        //파일 업로드 요청
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, image.getInputStream(),
                objectMetadata);

        amazonS3.putObject(putObjectRequest);

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, amazonS3.getRegionName(),
                fileName);
    }


    public void deleteImageFromS3(String imageAddress) throws S3Exception {
        String key = getKeyFromImageAddress(imageAddress);
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception e) {
            throw new S3Exception(ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
        }
    }

    private String getKeyFromImageAddress(String imageAddress) throws S3Exception {
        try {
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            throw new S3Exception(ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
        }
    }
}