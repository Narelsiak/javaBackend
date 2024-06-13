package com.example.jp.services;

public class CalculateGrade {
    public static double calculate(long score, long maxScore) {
        if (maxScore == 0) {
            throw new IllegalArgumentException("maxScore cannot be zero");
        }

        double percentage = ((double) score / maxScore) * 100;

        if (percentage < 50) {
            return 2.0;
        } else if (percentage < 60) {
            return 3.0;
        } else if (percentage < 70) {
            return 3.5;
        } else if (percentage < 80) {
            return 4.0;
        } else if (percentage < 90) {
            return 4.5;
        } else {
            return 5.0;
        }
    }
}
