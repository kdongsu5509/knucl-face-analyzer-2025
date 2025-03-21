package com.knucl.FaceAnalyze.controller;

import com.knucl.FaceAnalyze.repository.ResultRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@AllArgsConstructor
public class ResultController {
    private final ResultRepository resultRepository;

    /**
     * 사용자에게 결과 View 페이지를 제공합니다
     *
     * @param uuid
     * @param model
     * @return
     */
    @GetMapping("/face/result/{uuid}")
    public String showResult(@PathVariable String uuid, Model model) {
        String resultText = resultRepository.getResult(UUID.fromString(uuid));
        model.addAttribute("resultText", resultText);
        return "result";
    }
}
