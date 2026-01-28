package com.partharoypc.smartads.listeners;

import android.view.View;

/**
 * Listener for Banner Ad lifecycle events.
 */
public interface BannerAdListener {
    /**
     * Called when the banner ad is successfully loaded.
     * 
     * @param adView The view containing the loaded banner ad.
     */
    void onAdLoaded(View adView);

    /** Called when the banner ad fails to load. */
    void onAdFailed(String errorMessage);

    /** Called when the user clicks on the banner ad. */
    default void onAdClicked() {
    }

    /** Called when the banner ad is displayed and an impression is recorded. */
    default void onAdImpression() {
    }
}
