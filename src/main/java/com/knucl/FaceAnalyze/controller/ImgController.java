package com.knucl.FaceAnalyze.controller;

import com.knucl.FaceAnalyze.myException.S3Exception;
import com.knucl.FaceAnalyze.service.S3ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController("/img")
public class ImgController {

    @Autowired
    private S3ImageService imgService;

    @PostMapping()
    public String upload(MultipartFile img) throws S3Exception {
        return imgService.upload(img);
    }

    @DeleteMapping()
    public String delete(String imgAddress) {
        try {
            imgService.deleteImageFromS3(imgAddress);
            return "success";
        } catch (S3Exception e) {
            return e.getErrorCode().getMessage();
        }
    }
}
