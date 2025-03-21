package com.knucl.FaceAnalyze.controller;

import com.knucl.FaceAnalyze.dto.AnalyzeResultDTO;
import com.knucl.FaceAnalyze.repository.UserCountRepository;
import com.knucl.FaceAnalyze.service.AnalyzeService;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@lombok.extern.slf4j.Slf4j
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/analyze")
public class AnalyzeController {

    AnalyzeService analyzeService;
    UserCountRepository userCountRepository;

    @Tag(name = "Response Estimate", description = "Response Estimate API")
    @PostMapping("/face")
    public AnalyzeResultDTO analyzeFace(@RequestParam("imgAddress") String imgAddress) {

        try {
            userCountRepository.increaseUserCount();
            log.info("User Count: " + userCountRepository.getUserCount());
            return analyzeService.analyzeImage(imgAddress);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
