package com.ziroom.godbase.routers;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Author:关震
 * Date:2020/5/29 11:07
 * Description:RouterUtils
 **/
public class RouterUtils {
    public static void jump(String routerUrl) {
        ARouter.getInstance().build(routerUrl).navigation();
    }
}
