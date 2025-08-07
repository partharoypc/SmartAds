package com.partharoy.smartads.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.partharoy.smartads.R;

public class LoadingAdDialog {
    private final Dialog dialog;

    public LoadingAdDialog(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loading_ad);
        dialog.setCancelable(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    public void show(String status) {
        if (dialog != null && !dialog.isShowing()) {
            TextView statusText = dialog.findViewById(R.id.tv_status);
            if (statusText != null) {
                statusText.setText(status);
            }
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
