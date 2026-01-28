package com.partharoypc.smartads.listeners;

/**
 * Listener for Interstitial Ad lifecycle events.
 */
public interface InterstitialAdListener {
    /** Called when the interstitial ad is closed/dismissed by the user. */
    void onAdDismissed();

    /** Called when the interstitial ad fails to show. */
    void onAdFailedToShow(String errorMessage);

    /** Called when the interstitial ad is successfully loaded. */
    default void onAdLoaded() {
    }

    /** Called when the user clicks on the interstitial ad. */
    default void onAdClicked() {
    }

    /**
     * Called when the interstitial ad is displayed and an impression is recorded.
     */
    default void onAdImpression() {
    }
}
