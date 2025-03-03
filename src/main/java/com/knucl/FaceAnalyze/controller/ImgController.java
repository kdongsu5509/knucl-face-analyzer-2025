package com.knucl.FaceAnalyze.controller;

import com.knucl.FaceAnalyze.myException.S3Exception;
import com.knucl.FaceAnalyze.service.S3ImageService;
import groovy.util.logging.Slf4j;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@lombok.extern.slf4j.Slf4j
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/")
public class ImgController {

    private S3ImageService imgService;

    @PostMapping("image")
    public String upload(@RequestParam("img") MultipartFile img) throws S3Exception, IOException {
        String s3Url = imgService.upload(img);
        log.info("Image uploaded to: " + s3Url);
        return s3Url;
    }

    @DeleteMapping("image")
    public String delete(@RequestParam("imgAddress") String imgAddress) {
        try {
            imgService.deleteImageFromS3(imgAddress);
            return "success";
        } catch (S3Exception e) {
            return e.getErrorCode().getMessage();
        }
    }
}
