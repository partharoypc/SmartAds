package com.partharoy.smartads.listeners;

import android.view.View;

public interface NativeAdListener {
    void onAdLoaded(View nativeAdView);

    void onAdFailed(String errorMessage);

    default void onAdClicked() {
    }

    default void onAdImpression() {
    }
}
