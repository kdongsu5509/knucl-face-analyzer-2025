package com.knucl.FaceAnalyze.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalyzerResultDTO {
    private String result;
    private String uuid;
}
