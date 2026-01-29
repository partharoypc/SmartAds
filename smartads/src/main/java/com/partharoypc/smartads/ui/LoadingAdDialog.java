package com.partharoypc.smartads.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import android.widget.TextView;

import com.partharoypc.smartads.R;

public class LoadingAdDialog {
    private final Context context;
    private Dialog dialog;

    public LoadingAdDialog(Context context) {
        this.context = context;
    }
    private String headline;
    private String subHeadline;
    private Integer backgroundColor;
    private Integer headlineColor;
    private Integer subHeadlineColor;
    private Integer progressColor;

    public LoadingAdDialog setHeadline(String headline) {
        this.headline = headline;
        return this;
    }

    public LoadingAdDialog setSubHeadline(String subHeadline) {
        this.subHeadline = subHeadline;
        return this;
    }

    public LoadingAdDialog setBackgroundColor(int color) {
        this.backgroundColor = color;
        return this;
    }

    public LoadingAdDialog setHeadlineColor(int color) {
        this.headlineColor = color;
        return this;
    }

    public LoadingAdDialog setSubHeadlineColor(int color) {
        this.subHeadlineColor = color;
        return this;
    }

    public LoadingAdDialog setProgressColor(int color) {
        this.progressColor = color;
        return this;
    }

    public void show(String message, Integer bgColor, Integer txtColor) {
        if (!(context instanceof Activity activity))
            return;
        if (activity.isFinishing() || activity.isDestroyed())
            return;

        dismiss();

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        }

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_loading_ad, null);

        View cardView = view.findViewById(R.id.dialog_card);
        View progressBar = view.findViewById(R.id.loading_progress);
        TextView titleView = view.findViewById(R.id.loading_message);
        TextView subtitleView = view.findViewById(R.id.loading_submessage);

        String finalHeadline = (message != null) ? message : this.headline;
        if (finalHeadline != null)
            titleView.setText(finalHeadline);
        else
            titleView.setText(R.string.smartads_loading_ad);

        if (subHeadline != null) {
            subtitleView.setText(subHeadline);
            subtitleView.setVisibility(View.VISIBLE);
        }

        Integer finalBgColor = (bgColor != null) ? bgColor : this.backgroundColor;
        if (finalBgColor != null && cardView instanceof com.google.android.material.card.MaterialCardView) {
            ((com.google.android.material.card.MaterialCardView) cardView).setCardBackgroundColor(finalBgColor);
        }

        Integer finalTxtColor = (txtColor != null) ? txtColor : this.headlineColor;
        if (finalTxtColor != null) {
            titleView.setTextColor(finalTxtColor);
        }

        if (subHeadlineColor != null) {
            subtitleView.setTextColor(subHeadlineColor);
        }

        Integer finalProgressColor = (txtColor != null) ? txtColor : this.progressColor; // Fallback to txtColor if
        if (finalProgressColor != null
                && progressBar instanceof com.google.android.material.progressindicator.CircularProgressIndicator) {
            ((com.google.android.material.progressindicator.CircularProgressIndicator) progressBar)
                    .setIndicatorColor(finalProgressColor);
        }

        dialog.setContentView(view);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        }

        if (!activity.isFinishing() && !activity.isDestroyed()) {
            try {
                dialog.show();
            } catch (Exception ignored) {
            }
        }
    }

    public void dismiss() {
        if (dialog != null) {
            try {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            } catch (Exception ignored) {
            }
            dialog = null;
        }
    }
}
