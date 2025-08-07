package com.partharoy.smartads.managers;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAdLayout;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.partharoy.smartads.R;
import com.partharoy.smartads.SmartAdsConfig;
import com.partharoy.smartads.TestAdIds;
import com.partharoy.smartads.listeners.NativeAdListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NativeAdManager {
    private static final String TAG = "NativeAdManager";

    public void loadAndShowAd(Activity activity, FrameLayout adContainer, @LayoutRes int layoutRes, SmartAdsConfig config, NativeAdListener listener) {
        loadAdMob(activity, adContainer, layoutRes, config, listener);
    }

    private void loadAdMob(Activity activity, FrameLayout adContainer, @LayoutRes int layoutRes, SmartAdsConfig config, NativeAdListener listener) {
        String adUnitId = config.isTestMode() ? TestAdIds.ADMOB_NATIVE_ID : config.getAdMobNativeId();
        if (adUnitId == null || adUnitId.isEmpty()) {
            if (config.isUseMetaBackup())
                loadMeta(activity, adContainer, layoutRes, config, listener);
            else if (listener != null) listener.onAdFailed("No AdMob ID provided.");
            return;
        }

        AdLoader.Builder builder = new AdLoader.Builder(activity, adUnitId);
        builder.forNativeAd(nativeAd -> {
            NativeAdView adView = (NativeAdView) LayoutInflater.from(activity).inflate(layoutRes, adContainer, false);
            populateAdMobNativeAdView(nativeAd, adView);
            adContainer.removeAllViews();
            adContainer.addView(adView);
            if (listener != null) listener.onAdLoaded(adView);
            Log.i(TAG, "AdMob Native Ad Loaded.");
        });

        AdLoader adLoader = builder.withAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.w(TAG, "AdMob Native failed: " + loadAdError.getMessage());
                if (config.isUseMetaBackup())
                    loadMeta(activity, adContainer, layoutRes, config, listener);
                else if (listener != null) listener.onAdFailed(loadAdError.getMessage());
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void loadMeta(Activity activity, FrameLayout adContainer, @LayoutRes int layoutRes, SmartAdsConfig config, NativeAdListener listener) {
        String placementId = config.isTestMode() ? TestAdIds.META_NATIVE_ID.replace("YOUR_PLACEMENT_ID", config.getMetaNativeId()) : config.getMetaNativeId();
        if (placementId == null || placementId.isEmpty()) {
            if (listener != null) listener.onAdFailed("No Meta Placement ID provided.");
            return;
        }

        com.facebook.ads.NativeAd metaNativeAd = new com.facebook.ads.NativeAd(activity, placementId);
        metaNativeAd.loadAd(metaNativeAd.buildLoadAdConfig().withAdListener(new com.facebook.ads.NativeAdListener() {
            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {
                if (metaNativeAd != ad) return;

                // FIX: Inflate the ad view and then wrap it in the NativeAdLayout
                NativeAdLayout nativeAdLayout = new NativeAdLayout(activity);
                LayoutInflater inflater = LayoutInflater.from(activity);
                View adView = inflater.inflate(layoutRes, nativeAdLayout, false);
                nativeAdLayout.addView(adView);

                populateMetaNativeAdView(activity, metaNativeAd, adView, nativeAdLayout);

                adContainer.removeAllViews();
                adContainer.addView(nativeAdLayout);
                if (listener != null) listener.onAdLoaded(nativeAdLayout);
                Log.i(TAG, "Meta Native Ad Loaded.");
            }

            @Override
            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {
                Log.e(TAG, "Meta Native failed: " + adError.getErrorMessage());
                if (listener != null) listener.onAdFailed(adError.getErrorMessage());
            }

            @Override
            public void onMediaDownloaded(com.facebook.ads.Ad ad) {
            }

            @Override
            public void onAdClicked(com.facebook.ads.Ad ad) {
            }

            @Override
            public void onLoggingImpression(com.facebook.ads.Ad ad) {
            }
        }).build());
    }

    private void populateAdMobNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        adView.setMediaView(adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        ((TextView) Objects.requireNonNull(adView.getHeadlineView())).setText(nativeAd.getHeadline());
        if (adView.getMediaView() != null) {
            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        }

        if (nativeAd.getBody() == null) Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);
        else {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null)
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
        else {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
        else {
            ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getStarRating() == null)
            Objects.requireNonNull(adView.getStarRatingView()).setVisibility(View.INVISIBLE);
        else {
            ((RatingBar) Objects.requireNonNull(adView.getStarRatingView())).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null)
            Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.INVISIBLE);
        else {
            ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);
    }

    private void populateMetaNativeAdView(Activity activity, com.facebook.ads.NativeAd metaNativeAd, View adView, NativeAdLayout nativeAdLayout) {
        metaNativeAd.unregisterView();

        // Add the AdOptionsView. This is required for Meta ads.
        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(activity, metaNativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Find views
        com.facebook.ads.MediaView metaMediaView = adView.findViewById(R.id.meta_ad_media);
        ImageView iconView = adView.findViewById(R.id.ad_app_icon);
        TextView headlineView = adView.findViewById(R.id.ad_headline);
        TextView bodyView = adView.findViewById(R.id.ad_body);
        Button callToActionView = adView.findViewById(R.id.ad_call_to_action);

        headlineView.setText(metaNativeAd.getAdHeadline());
        bodyView.setText(metaNativeAd.getAdBodyText());
        callToActionView.setText(metaNativeAd.getAdCallToAction());
        callToActionView.setVisibility(metaNativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);

        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(headlineView);
        clickableViews.add(callToActionView);
        clickableViews.add(metaMediaView);
        clickableViews.add(iconView);

        metaNativeAd.registerViewForInteraction(adView, metaMediaView, iconView, clickableViews);
    }
}