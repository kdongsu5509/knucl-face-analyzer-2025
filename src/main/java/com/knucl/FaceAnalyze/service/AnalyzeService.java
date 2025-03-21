package com.knucl.FaceAnalyze.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.knucl.FaceAnalyze.dto.AnalyzeResultDTO;
import com.knucl.FaceAnalyze.repository.ResultRepository;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

@Service
@AllArgsConstructor
public class AnalyzeService {

    private final AmazonS3 amazonS3;
    private final ChatClient chatClient;
    private final ResultRepository resultRepository;

    public AnalyzeResultDTO analyzeImage(String imageUrl) throws URISyntaxException, MalformedURLException {
        MimeType mimeType = resolveMimeTypeFromS3Url(imageUrl);

        URL url = new URI(imageUrl).toURL(); // Validate URL format here
        String result = chatClient.prompt()
                .user(userSpec -> userSpec
                        .text("스스로 관상가라고 생각하고 관상가 처럼 답변해줘. 처음에는 총괄적인 평가를 1~3줄로 요약해서 알려줘. 그 다음에 얼굴의 각 요소에 대해 설명해줘. 양식은 \"총평: ${너가 만들 총평} \n\n\n ${얼굴 각 요소에 대한 평가}\" 식으로 해줘. 마크다운 형식은 사용하지 말아줘.")
                        .media(MimeType.valueOf(mimeType.toString()),
                                url)) // Convert MimeType to String for media method
                .call()
                .content();
        assert result != null;
        AnalyzeResultDTO analyzeResultDTO = new AnalyzeResultDTO(result,
                resultRepository.saveResult(result).toString());
        return analyzeResultDTO;
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


