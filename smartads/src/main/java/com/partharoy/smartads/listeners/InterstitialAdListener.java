package com.partharoy.smartads.listeners;

public interface InterstitialAdListener {
    void onAdDismissed();

    void onAdFailedToShow(String errorMessage);

    default void onAdLoaded() {
    }

    default void onAdClicked() {
    }

    default void onAdImpression() {
    }
}
