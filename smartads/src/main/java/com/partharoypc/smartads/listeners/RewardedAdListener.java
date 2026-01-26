package com.partharoypc.smartads.listeners;

public interface RewardedAdListener {
    void onUserEarnedReward();

    void onAdDismissed();

    void onAdFailedToShow(String errorMessage);

    default void onAdLoaded() {
    }

    default void onAdClicked() {
    }

    default void onAdImpression() {
    }
}
