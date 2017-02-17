package com.mastercard.labs.mpqrmerchant.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/10/17
 */
public class KeyboardUtils {
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void restartInput(Context context, EditText editText) {
        InputMethodManager input= (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        input.restartInput(editText);
    }
}
