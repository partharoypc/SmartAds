package com.partharoy.smartads.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.partharoy.smartads.R;

/**
 * Professional loading dialog used while fetching ads.
 * Now uses an XML-based layout for a premium look and feel.
 */
public class LoadingAdDialog {
    private final Context context;
    private Dialog dialog;

    public LoadingAdDialog(Context context) {
        this.context = context;
    }

    public void show(String message, Integer backgroundColor, Integer textColor) {
        if (!(context instanceof Activity))
            return;
        Activity activity = (Activity) context;
        if (activity.isFinishing() || activity.isDestroyed())
            return;

        dismiss();

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // Add fade animation
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        }

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_loading_ad, null);

        View container = view.findViewById(R.id.dialog_container);
        ProgressBar progressBar = view.findViewById(R.id.loading_progress);
        TextView textView = view.findViewById(R.id.loading_message);

        // Set Message
        if (message != null) {
            textView.setText(message);
        }

        // Apply Custom Background Color if provided
        if (backgroundColor != null) {
            container.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
        }

        // Apply Custom Text Color if provided
        if (textColor != null) {
            textView.setTextColor(textColor);
            progressBar.setIndeterminateTintList(ColorStateList.valueOf(textColor));
        }

        dialog.setContentView(view);

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
