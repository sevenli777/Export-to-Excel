package com.quitter.quitter;

import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;


public class ShowToast {
    public static final int LONG_DURATION = 5000;
    public static final int SHORT_DURATION = 3000;
    private static Toast mToast;

    public static void showToast(String msgContent) {
        showToast(Gravity.CENTER, SHORT_DURATION, msgContent);
    }

    public static void showLongToast(String msgContent) {
        showToast(Gravity.CENTER, LONG_DURATION, msgContent);
    }

    public static void showToast(String msgContent, int period) {
        showToast(Gravity.CENTER, period, msgContent);
    }

    private static void showToast(int pos, int duration, CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(MyApplication.getAppContext(), text,
                        duration);
                mToast.setGravity(pos, 0, 0);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }

}
