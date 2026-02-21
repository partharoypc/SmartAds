package com.partharoypc.smartads.managers;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.partharoypc.smartads.AdStatus;
import com.partharoypc.smartads.SmartAds;
import com.partharoypc.smartads.SmartAdsConfig;
import com.partharoypc.smartads.SmartAdsLogger;
import com.partharoypc.smartads.ui.LoadingAdDialog;

public abstract class BaseFullScreenAdManager {
    protected AdStatus adStatus = AdStatus.IDLE;
    protected LoadingAdDialog loadingDialog;
    protected boolean isShowPending = false;
    protected Activity pendingActivity;
    protected boolean isLoading = false;
    protected final Handler handler = new Handler(Looper.getMainLooper());
    protected int retryAttempt = 0;
    protected static long lastShownTime = 0; // Static to cap across types if needed, or non-static for per-type

    protected boolean isAutoReloadEnabled = true;

    protected boolean isFrequencyCapped(SmartAdsConfig config) {
        long currentTime = System.currentTimeMillis();
        long diffSeconds = (currentTime - lastShownTime) / 1000;
        return diffSeconds < config.getFrequencyCapSeconds();
    }

    public AdStatus getAdStatus() {
        return adStatus;
    }

    public void setAutoReloadEnabled(boolean enabled) {
        this.isAutoReloadEnabled = enabled;
    }

    protected void showLoadingDialog(Activity activity) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingAdDialog(activity);
            SmartAdsConfig config = SmartAds.getInstance().getConfig();
            String text = config != null ? config.getDialogText() : "Loading Ad...";
            Integer bgColor = config != null ? config.getDialogBackgroundColor() : null;
            Integer textColor = config != null ? config.getDialogTextColor() : null;
            loadingDialog.show(text, bgColor, textColor);
        }
    }

    protected void dismissLoadingDialog() {
        handler.post(() -> {
            if (loadingDialog != null) {
                try {
                    loadingDialog.dismiss();
                    SmartAdsLogger.d("Loading dialog dismissed.");
                } catch (Exception e) {
                    SmartAdsLogger.e("Error dismissing loading dialog: " + e.getMessage());
                }
                loadingDialog = null;
            }
        });
    }

    protected void scheduleRetry(Context context, SmartAdsConfig config,
            com.google.android.gms.ads.LoadAdError loadAdError, Runnable loadAction) {
        long delay;
        if (loadAdError != null && loadAdError.getCode() == com.google.android.gms.ads.AdRequest.ERROR_CODE_NO_FILL) {
            SmartAdsLogger.d("Throttling NO_FILL retry to protect Match Rate.");
            delay = 60_000L;
            retryAttempt = 0;
        } else {
            delay = (long) Math.min(30_000L, Math.pow(2, Math.max(0, retryAttempt)) * 1000L);
            retryAttempt = Math.min(retryAttempt + 1, 3);
        }

        handler.postDelayed(loadAction, delay);
    }

    protected void onAdLoadedBase() {
        adStatus = AdStatus.LOADED;
        isLoading = false;
        retryAttempt = 0;
        dismissLoadingDialog();
    }

    protected void onAdFailedToLoadBase() {
        adStatus = AdStatus.FAILED;
        isLoading = false;
        dismissLoadingDialog();
    }

    /**
     * Checks if network is available. If not, attempts to load a House Ad.
     * 
     * @param context       Context reference
     * @param config        SmartAdsConfig reference
     * @param houseAdLoader Functional interface to trigger House Ad loading
     * @return true if we should stop execution (either because no net & house ad
     *         loading started, or no net & no house ad available).
     */
    protected boolean checkNetworkAndFallback(Context context, SmartAdsConfig config, Runnable houseAdLoader) {
        if (!com.partharoypc.smartads.utils.NetworkUtils.isNetworkAvailable(context)) {
            SmartAdsLogger.d("No Internet Connection. Checking for House Ad fallback...");
            if (config.isHouseAdsEnabled()) {
                houseAdLoader.run();
                return true; // We delegated to house ad loader
            } else {
                SmartAdsLogger.d("No Internet and House Ads disabled. Ad Request failed.");
                adStatus = AdStatus.IDLE;
                isLoading = false;
                dismissLoadingDialog();
                return true; // Stop execution
            }
        }
        return false; // Network checks out, proceed with AdMob
    }
}
