package com.ziroom.godbase.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.ziroom.godbase.App;

/**
 * 切换环境
 */
public class ChangeEnvironmentManager {
    private static final String mSp = "inkeE";
    private static final String mDebug = "isDebug";

    private ChangeEnvironmentManager() {
    }

    private static ChangeEnvironmentManager mInstance;

    public static ChangeEnvironmentManager getInstance() {
        if (mInstance == null) {
            synchronized (ChangeEnvironmentManager.class) {
                if (mInstance == null) {
                    mInstance = new ChangeEnvironmentManager();
                }
            }
        }
        return mInstance;
    }

    public void saveCurrentEnviroment(int isDebug) {
        SharedPreferences sharedPreferences = App.getApp().getSharedPreferences(mSp, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(mDebug, isDebug).apply();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                killProcess();
            }
        }, 600);
    }

    public int getIsDebug() {
        SharedPreferences sharedPreferences = App.getApp().getSharedPreferences(mSp, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(mDebug, -1);
    }

    private void killProcess() {
        int pid = android.os.Process.myPid(); //获得自己的pid
        android.os.Process.killProcess(pid);//通过pid自杀
    }
}
