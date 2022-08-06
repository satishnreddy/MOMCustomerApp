package com.mom.momcustomerapp.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author shailesh.lobo
 * @version -
 * @link -
 * @created - 13-04-2020 10:57
 * @modified by -
 * @updated on -
 * @since -
 */
public class UIUtils {
    public static void closeKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
