package com.knucl.FaceAnalyze.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class ResultRepository {
    private static Map<UUID, String> result = new HashMap<>();

    public static UUID saveResult(String result) {
        UUID uuid = UUID.randomUUID();
        ResultRepository.result.put(uuid, result);
        return uuid;
    }

    public static String getResult(UUID uuid) {
        return ResultRepository.result.get(uuid);
    }

    public static void deleteResult(UUID uuid) {
        ResultRepository.result.remove(uuid);
    }

    public static void clearResults() {
        ResultRepository.result.clear();
    }
}
