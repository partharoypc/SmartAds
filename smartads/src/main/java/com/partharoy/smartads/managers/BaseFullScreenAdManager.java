package com.partharoy.smartads.managers;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.partharoy.smartads.AdStatus;
import com.partharoy.smartads.SmartAds;
import com.partharoy.smartads.SmartAdsConfig;
import com.partharoy.smartads.ui.LoadingAdDialog;

public abstract class BaseFullScreenAdManager {
    protected AdStatus adStatus = AdStatus.IDLE;
    protected LoadingAdDialog loadingDialog;
    protected boolean isShowPending = false;
    protected Activity pendingActivity;
    protected boolean isLoading = false;
    protected final Handler handler = new Handler(Looper.getMainLooper());
    protected int retryAttempt = 0;

    public AdStatus getAdStatus() {
        return adStatus;
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
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
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
