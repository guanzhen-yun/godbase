package com.ziroom.godbase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ziroom.godbase.routers.RouterConstants;
import com.ziroom.godbase.routers.RouterUtils;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //很简单，一句话完成，可携带参数
        RouterUtils.jump(RouterConstants.App.Main);
    }
}
