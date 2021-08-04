package com.ziroom.godbase;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ziroom.godbase.service.CommonParam;
import com.ziroom.godbase.service.HeaderParam;
import com.ziroom.godbase.service.Host;
import com.ziroom.godbase.ui.ConfigActivity;
import com.ziroom.godbase.util.SharedUtils;
import com.ziroom.net.LogUtils;
import com.ziroom.net.RetrofitManager;

/**
 * Description:App
 **/
public class App extends Application {
    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        SharedUtils.getInstance().init();
        ARouter.init(this);
        initNetInfo();
    }

    public static App getApp() {
        return app;
    }

    /**
     * 初始化网络信息
     */
    public static void initNetInfo() {
        int isOpenLog = SharedUtils.getInstance().getInt(SharedUtils.IS_OPENLOG);
        if(isOpenLog == -1) {
            isOpenLog = BuildConfig.DEBUG ? ConfigActivity.OPEN_LOG : ConfigActivity.CLOSE_LOG;
        }
        LogUtils.debug = isOpenLog == ConfigActivity.OPEN_LOG;
        RetrofitManager.getInstance().setBaseUrl(Host.getCommonHost())
                .setParams(HeaderParam.getHeaderParams(), CommonParam.getCommonParams()).init(Host.getHosts());
    }
}
