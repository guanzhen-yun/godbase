package com.ziroom.godbase.util;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.ziroom.godbase.App;

/**
 * 吐司工具类
 */
public class ToastUtils {
    private static Toast mToast;

    @SuppressLint("ShowToast")
    public static void showShortToast(String msg) {
        if(App.getApp() != null){
            if (mToast == null) {
                mToast = Toast.makeText(App.getApp(), msg, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(msg);
            }
            mToast.show();
        }
    }
}
