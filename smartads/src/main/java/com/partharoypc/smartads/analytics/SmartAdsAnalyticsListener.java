package com.partharoypc.smartads.analytics;

import android.os.Bundle;

/**
 * Listener for tracking impression-level revenue data (ILAR).
 * Implement this interface to send ad revenue data to your analytics provider
 * (Google Analytics, Firebase, AppsFlyer, Mixpanel, etc.).
 */
public interface SmartAdsAnalyticsListener {
    /**
     * Called when an ad registers a paid event (impression).
     *
     * @param adUnitId     The Ad Unit ID that generated the revenue.
     * @param adFormat     The format of the ad (Banner, Interstitial, Reward,
     *                     etc.).
     * @param adNetwork    The name of the ad network that showed the ad (e.g.,
     *                     "Google AdMob", "Facebook", "AppLovin").
     * @param valueMicros  The value of the ad in micros (millionths of a currency
     *                     unit).
     * @param currencyCode The ISO 4217 currency code (e.g., "USD").
     * @param precision    The precision of the ad value (0 = Unknown, 1 =
     *                     Estimated, 2 = Publisher Provided, 3 = Precise).
     * @param extras       Additional metadata if available.
     */
    void onAdRevenuePaid(String adUnitId, String adFormat, String adNetwork, long valueMicros, String currencyCode,
            int precision, Bundle extras);
}
