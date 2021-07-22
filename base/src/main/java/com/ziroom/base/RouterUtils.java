package com.ziroom.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Author:关震
 * Date:2020/5/29 11:07
 * Description:RouterUtils 路由跳转工具类
 **/
public class RouterUtils {
    public static void jumpWithFinish(Activity activity, String routerUrl) {
        jumpWithFinish(activity, routerUrl, null);
    }
    public static void jumpWithFinish(final Activity activity, String routerUrl, Bundle bundle) {
        jump(routerUrl, bundle);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.finish();
            }
        }, 300);
    }
    public static void jump(String routerUrl) {
        jump(routerUrl, null);
    }

    public static void jump(String routerUrl, Bundle bundle) {
        jump(routerUrl, bundle, -1);
    }

    public static void jump(String routerUrl, Bundle bundle, int requestCode) {
        ARouter.getInstance().build(routerUrl).with(bundle).navigation(null, requestCode);
    }
}
