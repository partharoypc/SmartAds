package com.partharoypc.smartads.listeners;

/**
 * Listener for Rewarded Ad lifecycle events.
 */
public interface RewardedAdListener {
    /** Called when the user completes the ad and earns a reward. */
    void onUserEarnedReward();

    /** Called when the rewarded ad is closed/dismissed by the user. */
    void onAdDismissed();

    /** Called when the rewarded ad fails to show. */
    void onAdFailedToShow(String errorMessage);

    /** Called when the rewarded ad is successfully loaded. */
    default void onAdLoaded() {
    }

    /** Called when the user clicks on the rewarded ad. */
    default void onAdClicked() {
    }

    /** Called when the rewarded ad is displayed and an impression is recorded. */
    default void onAdImpression() {
    }
}
