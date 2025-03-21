package com.knucl.FaceAnalyze.repository;

import org.springframework.stereotype.Repository;

@Repository
public class UserCountRepository {
    private static int userCount = 0;

    public static int getUserCount() {
        return UserCountRepository.userCount;
    }

    public static void increaseUserCount() {
        UserCountRepository.userCount++;
    }

    public static void decreaseUserCount() {
        UserCountRepository.userCount--;
    }
}
