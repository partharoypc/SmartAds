package com.partharoypc.smartads;

import android.util.Log;

public final class SmartAdsLogger {
    private static final String TAG = "SmartAds";

    private SmartAdsLogger() {
        // Utility class
    }

    public static void d(String message) {
        try {
            if (SmartAds.getInstance().getConfig().isLoggingEnabled()) {
                Log.d(TAG, message);
            }
        } catch (IllegalStateException ignored) {
        }
    }

    public static void e(String message) {
        Log.e(TAG, message);
    }

    public static void e(String message, Throwable tr) {
        Log.e(TAG, message, tr);
    }
}
