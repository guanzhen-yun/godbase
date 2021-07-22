package com.ziroom.godbase.service;

import android.os.Build;

import java.util.HashMap;
import java.util.Map;

//公共参数
public class CommonParam {
    private static final Map<String, String> mCommonParams = new HashMap<>();
    public static Map<String, String> getCommonParams() {
        mCommonParams.put("platformType", "4");
        mCommonParams.put("appName", "zgzf");
        mCommonParams.put("app_name", "zgzf");
        mCommonParams.put("s_os", "Android");
        mCommonParams.put("operaVersion", Build.VERSION.RELEASE);
        mCommonParams.put("platform_type", "4");
        mCommonParams.put("device_from", "4");
        return mCommonParams;
    }
}
