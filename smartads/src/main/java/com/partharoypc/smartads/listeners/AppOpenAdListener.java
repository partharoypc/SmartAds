package com.partharoypc.smartads.listeners;

/**
 * Listener for App Open Ad lifecycle events.
 */
public interface AppOpenAdListener {
    /** Called when the ad is closed/dismissed by the user. */
    void onAdDismissed();

    /** Called when the ad fails to show (e.g., failed to load or expired). */
    void onAdFailedToShow(String errorMessage);

    /** Called when the ad is successfully loaded. */
    default void onAdLoaded() {
    }

    /** Called when the user clicks on the ad. */
    default void onAdClicked() {
    }

    /** Called when the ad is displayed and an impression is recorded. */
    default void onAdImpression() {
    }
}
