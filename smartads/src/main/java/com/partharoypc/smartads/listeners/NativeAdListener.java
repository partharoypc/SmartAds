package com.partharoypc.smartads.listeners;

import android.view.View;

/**
 * Listener for Native Ad lifecycle events.
 */
public interface NativeAdListener {
    /**
     * Called when the native ad is successfully loaded.
     * 
     * @param nativeAdView The view containing the loaded native ad.
     */
    void onAdLoaded(View nativeAdView);

    /** Called when the native ad fails to load. */
    void onAdFailed(String errorMessage);

    /** Called when the user clicks on the native ad. */
    default void onAdClicked() {
    }

    /** Called when the native ad is displayed and an impression is recorded. */
    default void onAdImpression() {
    }
}
