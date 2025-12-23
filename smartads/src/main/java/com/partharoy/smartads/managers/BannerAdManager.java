package com.partharoy.smartads.managers;

import android.Manifest;
import android.app.Activity;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.partharoy.smartads.SmartAdsConfig;
import com.partharoy.smartads.TestAdIds;
import com.partharoy.smartads.listeners.BannerAdListener;

public class BannerAdManager {
    private final java.util.Map<FrameLayout, Boolean> listenerAdded = new java.util.WeakHashMap<>();

    @RequiresPermission(Manifest.permission.INTERNET)
    public void loadAndShowAd(Activity activity, FrameLayout adContainer, SmartAdsConfig config,
            BannerAdListener listener) {
        loadAdMob(activity, adContainer, config, listener);
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    private void loadAdMob(Activity activity, FrameLayout adContainer, SmartAdsConfig config,
            BannerAdListener listener) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            if (listener != null)
                listener.onAdFailed("Activity is not valid.");
            return;
        }

        String adUnitId = config.isTestMode() ? TestAdIds.ADMOB_BANNER_ID : config.getAdMobBannerId();
        if (adUnitId == null || adUnitId.isEmpty()) {
            if (listener != null)
                listener.onAdFailed("No AdMob ID provided.");
            return;
        }

        // Clean up existing ads before loading new one
        destroy(adContainer);

        int containerWidth = adContainer.getWidth();
        if (containerWidth == 0) {
            adContainer.post(() -> {
                if (!activity.isFinishing() && !activity.isDestroyed()) {
                    loadAdMob(activity, adContainer, config, listener);
                }
            });
            return;
        }

        AdView admobBanner = new AdView(activity);
        float density = activity.getResources().getDisplayMetrics().density;
        int adWidthInDp = Math.max(0, (int) (containerWidth / density));
        AdSize adaptiveSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidthInDp);
        admobBanner.setAdSize(adaptiveSize);
        admobBanner.setAdUnitId(adUnitId);
        admobBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (!isContainerActive(adContainer) || activity.isFinishing() || activity.isDestroyed()) {
                    try {
                        admobBanner.destroy();
                    } catch (Exception ignored) {
                    }
                    return;
                }
                adContainer.removeAllViews();
                adContainer.addView(admobBanner);
                if (listener != null)
                    listener.onAdLoaded(admobBanner);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                if (listener != null)
                    listener.onAdFailed(loadAdError.getMessage());
            }
        });
        AdRequest.Builder builder = new AdRequest.Builder();
        if (config.isCollapsibleBannerEnabled()) {
            android.os.Bundle extras = new android.os.Bundle();
            extras.putString("collapsible", "bottom");
            builder.addNetworkExtrasBundle(com.google.ads.mediation.admob.AdMobAdapter.class, extras);
        }
        admobBanner.loadAd(builder.build());
    }

    private boolean isContainerActive(FrameLayout adContainer) {
        if (adContainer.getWindowToken() == null)
            return false;
        if (adContainer.getVisibility() != android.view.View.VISIBLE)
            return false;
        return adContainer.isShown();
    }

    public void destroy(FrameLayout adContainer) {
        try {
            int childCount = adContainer.getChildCount();
            for (int i = 0; i < childCount; i++) {
                android.view.View child = adContainer.getChildAt(i);
                if (child instanceof AdView) {
                    ((AdView) child).destroy();
                }
            }
        } catch (Exception ignored) {
        }
        try {
            adContainer.removeAllViews();
        } catch (Exception ignored) {
        }
    }
}
