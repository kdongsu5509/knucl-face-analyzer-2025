package com.knucl.FaceAnalyze.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.knucl.FaceAnalyze.myException.ErrorCode;
import com.knucl.FaceAnalyze.myException.S3Exception;
import java.net.URI;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

class S3ImageServiceTest {

    @InjectMocks
    private S3ImageService s3ImageService;

    @Mock
    private AmazonS3 mockAmazonS3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        try {
            URL mockUrl = new URI("https://example.com/test.jpg").toURL();
            when(mockAmazonS3.getUrl(anyString(), anyString())).thenReturn(mockUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testUploadValidImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpeg",
                "test image content".getBytes());
        when(mockAmazonS3.putObject(any(PutObjectRequest.class))).thenReturn(null);

        String result = s3ImageService.upload(file);

        assertNotNull(result);
        assertTrue(result.contains("amazonaws.com"));
    }

    @Test
    void testUploadEmptyFile() {
        MockMultipartFile emptyFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[0]);

        Exception exception = assertThrows(S3Exception.class, () -> {
            s3ImageService.upload(emptyFile);
        });

        assertEquals(ErrorCode.EMPTY_FILE_EXCEPTION.getMessage(), exception.getMessage());
    }
}
