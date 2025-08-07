package com.partharoy.smartads.helpers;

public class AdIntervalHelper {
    private static long lastAdShowTime = 0;

    public static void initialize() {
        lastAdShowTime = 0;
    }

    public static boolean canShowAd(long intervalMillis) {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastAdShowTime >= intervalMillis);
    }

    public static void onAdShown() {
        lastAdShowTime = System.currentTimeMillis();
    }
}
