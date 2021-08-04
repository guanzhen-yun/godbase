package com.ziroom.godbase.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ziroom.godbase.App;

/**
 * sp工具类
 */
public class SharedUtils {
    private static final String SP = "sp";
    public static final String IS_OPENLOG = "is_openlog";
    public static final String ENV_TYPE = "env_type";
    public static final String ENV = "env";
    public static final String ENV_OTHER = "env_other";

    private SharedPreferences mSharedPrefrence;

    private SharedUtils() {
    }

    private static volatile SharedUtils mInstance;

    public static SharedUtils getInstance() {
        if (mInstance == null) {
            synchronized (SharedUtils.class) {
                if (mInstance == null) {
                    mInstance = new SharedUtils();
                }
            }
        }
        return mInstance;
    }

    public void init() {
        mSharedPrefrence = App.getApp().getSharedPreferences(SP, Context.MODE_PRIVATE);
    }

    public void saveString(String key, String value) {
        mSharedPrefrence.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return mSharedPrefrence.getString(key, "");
    }

    public void saveBoolean(String key, boolean value) {
        mSharedPrefrence.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return mSharedPrefrence.getBoolean(key, false);
    }

    public void saveInt(String key, int value) {
        mSharedPrefrence.edit().putInt(key, value).apply();
    }

    public int getInt(String key) {
        return mSharedPrefrence.getInt(key, -1);
    }

}
