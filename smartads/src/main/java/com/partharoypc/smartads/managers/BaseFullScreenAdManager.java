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

    protected void scheduleRetry(Context context, SmartAdsConfig config, Runnable loadAction) {
        long delay = (long) Math.min(60_000L, Math.pow(2, Math.max(0, retryAttempt)) * 1000L);
        retryAttempt = Math.min(retryAttempt + 1, 10);
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
}
