package com.partharoy.smartads.listeners;

import android.view.View;

public interface BannerAdListener {
    void onAdLoaded(View adView);
    void onAdFailed(String errorMessage);
}
