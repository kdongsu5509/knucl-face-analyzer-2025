package com.knucl.FaceAnalyze.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.knucl.FaceAnalyze.service.S3ImageService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/analyze")
public class AnalyzeController {

    private final AmazonS3 amazonS3;
    private final S3ImageService imgService;
    private final ChatClient chatClient;

    @PostMapping("/face")
    public String analyzeFace(String imgAddress) throws IOException {
        MimeType mimeType = resolveMimeTypeFromS3Url(imgAddress);

        try {
            URL url = new URL(imgAddress); // Validate URL format here
            return chatClient.prompt()
                    .user(userSpec -> userSpec
                            .text("")
                            .media(MimeType.valueOf(mimeType.toString()),
                                    url)) // Convert MimeType to String for media method
                    .call()
                    .content();
        } catch (MalformedURLException e) {
            return "Invalid URL: " + e.getMessage(); // Return or log an appropriate error message
        }
    }

    private MimeType resolveMimeTypeFromS3Url(String url) {
        try {
            String bucketName = url.split("/")[2]; // Simplistic extraction, assumes URL is well-formed
            String key = url.substring(url.indexOf(bucketName) + bucketName.length() + 1);

            ObjectMetadata metadata = amazonS3.getObjectMetadata(new GetObjectMetadataRequest(bucketName, key));
            return MimeTypeUtils.parseMimeType(metadata.getContentType());
        } catch (Exception e) {
            // Log the error or handle it appropriately
            return MimeTypeUtils.APPLICATION_OCTET_STREAM; // Default MIME type if there's an error
        }
    }
}
