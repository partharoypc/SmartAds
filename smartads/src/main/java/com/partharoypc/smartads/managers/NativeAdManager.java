package com.partharoypc.smartads.managers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.partharoypc.smartads.R;
import com.partharoypc.smartads.SmartAdsConfig;
import com.partharoypc.smartads.TestAdIds;
import com.partharoypc.smartads.listeners.NativeAdListener;

public class NativeAdManager {
    private NativeAd currentAdMobNative;
    private final java.util.Map<FrameLayout, Boolean> listenerAdded = new java.util.WeakHashMap<>();
    private final android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
    private int retryAttempt = 0;

    public void loadAndShowAd(Activity activity, FrameLayout adContainer, @LayoutRes int layoutRes,
            SmartAdsConfig config, NativeAdListener listener) {
        loadAdMob(activity, adContainer, layoutRes, config, listener);
    }

    public void loadAndShowAd(Activity activity, FrameLayout adContainer, com.partharoypc.smartads.NativeAdSize size,
            SmartAdsConfig config, NativeAdListener listener) {
        int layoutRes;
        switch (size) {
            case SMALL:
                layoutRes = R.layout.native_ad_small;
                break;
            case MEDIUM:
                layoutRes = R.layout.native_ad_medium;
                break;
            case LARGE:
                layoutRes = R.layout.native_ad_large;
                break;
            default:
                layoutRes = R.layout.native_ad_medium;
                break;
        }
        loadAdMob(activity, adContainer, layoutRes, config, listener);
    }

    private void loadAdMob(Activity activity, FrameLayout adContainer, @LayoutRes int layoutRes, SmartAdsConfig config,
            NativeAdListener listener) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            if (listener != null)
                listener.onAdFailed("Activity is invalid.");
            return;
        }

        String adUnitId = config.isTestMode() ? TestAdIds.ADMOB_NATIVE_ID : config.getAdMobNativeId();
        if (adUnitId == null || adUnitId.isEmpty()) {
            if (listener != null)
                listener.onAdFailed("No AdMob ID provided.");
            return;
        }

        AdLoader.Builder builder = new AdLoader.Builder(activity, adUnitId);
        builder.forNativeAd(nativeAd -> {
            if (!isContainerActive(adContainer)) {
                try {
                    nativeAd.destroy();
                } catch (Exception ignored) {
                }
                return;
            }
            if (currentAdMobNative != null) {
                currentAdMobNative.destroy();
            }
            currentAdMobNative = nativeAd;
            NativeAdView adView = (NativeAdView) LayoutInflater.from(activity).inflate(layoutRes, adContainer, false);
            populateAdMobNativeAdView(nativeAd, adView);
            adContainer.removeAllViews();
            adContainer.addView(adView);
            if (listener != null)
                listener.onAdLoaded(adView);
            retryAttempt = 0;
        });

        AdLoader adLoader = builder.withAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                if (listener != null)
                    listener.onAdFailed(loadAdError.getMessage());
                scheduleRetry(activity, adContainer, layoutRes, config, listener);
            }

            @Override
            public void onAdClicked() {
                if (listener != null)
                    listener.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                if (listener != null)
                    listener.onAdImpression();
            }
        }).build();

        // Attach Paid Event Listener to the loaded native ad in forNativeAd callback
        // This is handled inside forNativeAd block above

        adLoader.loadAd(new AdRequest.Builder().build());
        if (listenerAdded.get(adContainer) == null) {
            adContainer.addOnAttachStateChangeListener(new android.view.View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    destroy(adContainer);
                }
            });
            listenerAdded.put(adContainer, Boolean.TRUE);
        }
    }

    private void populateAdMobNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        adView.setMediaView(adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // Headline
        if (adView.getHeadlineView() != null) {
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        }

        // Media
        if (adView.getMediaView() != null) {
            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        }

        // Body
        if (adView.getBodyView() != null) {
            if (nativeAd.getBody() == null) {
                adView.getBodyView().setVisibility(View.INVISIBLE);
            } else {
                adView.getBodyView().setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }
        }

        // Call to Action
        if (adView.getCallToActionView() != null) {
            if (nativeAd.getCallToAction() == null) {
                adView.getCallToActionView().setVisibility(View.INVISIBLE);
            } else {
                adView.getCallToActionView().setVisibility(View.VISIBLE);
                ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }
        }

        // Icon
        if (adView.getIconView() != null) {
            if (nativeAd.getIcon() == null) {
                adView.getIconView().setVisibility(View.GONE);
            } else {
                ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }
        }

        // Star Rating
        if (adView.getStarRatingView() != null) {
            if (nativeAd.getStarRating() == null) {
                adView.getStarRatingView().setVisibility(View.INVISIBLE);
            } else {
                ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }
        }

        // Advertiser
        if (adView.getAdvertiserView() != null) {
            if (nativeAd.getAdvertiser() == null) {
                adView.getAdvertiserView().setVisibility(View.INVISIBLE);
            } else {
                ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }
        }

        adView.setNativeAd(nativeAd);
    }

    public void destroy(FrameLayout adContainer) {
        try {
            if (currentAdMobNative != null) {
                currentAdMobNative.destroy();
                currentAdMobNative = null;
            }
        } catch (Exception ignored) {
        }
        try {
            adContainer.removeAllViews();
        } catch (Exception ignored) {
        }
    }

    private boolean isContainerActive(FrameLayout adContainer) {
        if (adContainer.getWindowToken() == null)
            return false;
        if (adContainer.getVisibility() != View.VISIBLE)
            return false;
        return adContainer.isShown();
    }

    private void scheduleRetry(Activity activity, FrameLayout adContainer, @LayoutRes int layoutRes,
            SmartAdsConfig config, NativeAdListener listener) {
        long delay = (long) Math.min(60_000L, Math.pow(2, Math.max(0, retryAttempt)) * 1000L);
        retryAttempt = Math.min(retryAttempt + 1, 10);
        handler.postDelayed(() -> loadAdMob(activity, adContainer, layoutRes, config, listener), delay);
    }
}