package com.fitmanager.app.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.fitmanager.app.R;



public final class FitProgressBar {

    private Dialog dialog;

    public void show(Context context) {
        show(context, null);
    }

    public void show(Context context, CharSequence title) {
        show(context, title, true);
    }

    public void show(Context context, CharSequence title, boolean cancelable) {
        show(context, title, cancelable, null);
    }

    public void show(Context context, CharSequence title, boolean cancelable,
                       DialogInterface.OnCancelListener cancelListener) {
        if (dialog == null) {
            LayoutInflater inflator = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflator.inflate(R.layout.progress_bar, null);

            dialog = new Dialog(context, R.style.NewDialog);
            dialog.setContentView(view);
            dialog.setCancelable(cancelable);
            dialog.setOnCancelListener(cancelListener);
        }

        dialog.show();
    }

    public void hide() {
        if (dialog != null && dialog.isShowing()) {
            dialog.hide();
        }
    }

}
