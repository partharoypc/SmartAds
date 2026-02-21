package com.partharoypc.smartads.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility to prevent spamming AdMob servers when inventory is unavailable
 * (NO_FILL).
 * This protects the application's AdMob "Match Rate" analytics by enforcing a
 * global
 * cooldown per Ad Unit ID before identical requests can be retried.
 */
public class AdMobRateLimiter {
    private static final Map<String, Long> lastNoFillTimes = new ConcurrentHashMap<>();
    private static final long NO_FILL_COOLDOWN_MS = 60_000L; // 60 seconds

    /**
     * Records a NO_FILL event for a given Ad Unit ID.
     */
    public static void recordNoFill(String adUnitId) {
        if (adUnitId != null && !adUnitId.isEmpty()) {
            lastNoFillTimes.put(adUnitId, System.currentTimeMillis());
        }
    }

    /**
     * Checks whether an Ad Unit ID is currently in the NO_FILL cooldown period.
     */
    public static boolean isRateLimited(String adUnitId) {
        if (adUnitId == null || adUnitId.isEmpty())
            return false;

        Long lastTime = lastNoFillTimes.get(adUnitId);
        if (lastTime != null) {
            if (System.currentTimeMillis() - lastTime < NO_FILL_COOLDOWN_MS) {
                return true; // Still in cooldown
            } else {
                // Cooldown elapsed, clean up
                lastNoFillTimes.remove(adUnitId);
            }
        }
        return false;
    }
}
