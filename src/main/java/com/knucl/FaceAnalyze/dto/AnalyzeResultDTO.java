package com.knucl.FaceAnalyze.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalyzeResultDTO {
    private String result;
    private String uuid;
}
