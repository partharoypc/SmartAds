package com.partharoypc.smartads.listeners;

public interface AppOpenAdListener {
    void onAdDismissed();

    void onAdFailedToShow(String errorMessage);

    default void onAdLoaded() {
    }

    default void onAdClicked() {
    }

    default void onAdImpression() {
    }
}
