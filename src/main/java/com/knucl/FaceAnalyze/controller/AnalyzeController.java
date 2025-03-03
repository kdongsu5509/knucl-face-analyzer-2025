package com.knucl.FaceAnalyze.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.knucl.FaceAnalyze.service.S3ImageService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/analyze")
public class AnalyzeController {

    private final AmazonS3 amazonS3;
    private final S3ImageService imgService;
    private final ChatClient chatClient;

    @PostMapping("/face")
    public String analyzeFace(@RequestParam("imgAddress") String imgAddress) throws IOException, RuntimeException {
        MimeType mimeType = resolveMimeTypeFromS3Url(imgAddress);

        try {
            URL url = new URI(imgAddress).toURL(); // Validate URL format here
            return chatClient.prompt()
                    .user(userSpec -> userSpec
                            .text("스스로 관상가라고 생각해봐. 얼굴의 각 요소에 대해 설명한 다음 마지막에 총괄적인 내용도 말해줘. 마크다운 형식은 사용하지 말아줘.")
                            .media(MimeType.valueOf(mimeType.toString()),
                                    url)) // Convert MimeType to String for media method
                    .call()
                    .content();
        } catch (MalformedURLException e) {
            return "Invalid URL: " + e.getMessage(); // Return or log an appropriate error message
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
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
