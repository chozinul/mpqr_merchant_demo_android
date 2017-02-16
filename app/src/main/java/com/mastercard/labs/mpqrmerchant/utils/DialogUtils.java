package com.mastercard.labs.mpqrmerchant.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.mastercard.labs.mpqrmerchant.R;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/2/17
 */
public class DialogUtils {
    public static void showDialog(Context context, @StringRes int title, @StringRes int message) {
        customAlertDialogBuilder(context, message)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    private static AlertDialog.Builder customAlertDialogBuilder(Context context) {
        TextView titleEditTextView = new TextView(context);
        titleEditTextView.setBackgroundResource(R.drawable.alert_title_header);

        return new AlertDialog.Builder(context)
                .setCustomTitle(titleEditTextView);
    }

    public static AlertDialog.Builder customAlertDialogBuilder(Context context, @StringRes int message) {
        return customAlertDialogBuilder(context, context.getString(message));
    }

    public static AlertDialog.Builder customAlertDialogBuilder(Context context, String message) {
        TextView messageTextView = new TextView(context);
        messageTextView.setText(message);
        messageTextView.setGravity(Gravity.CENTER_HORIZONTAL);

        int padding = context.getResources().getDimensionPixelSize(R.dimen.size_10);
        float textSize = context.getResources().getDimension(R.dimen.size_x);
        messageTextView.setPadding(padding, padding, padding, padding);
        messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        messageTextView.setTextColor(ContextCompat.getColor(context, R.color.colorTextMainColor));

        return customAlertDialogBuilder(context).setView(messageTextView);
    }
}
