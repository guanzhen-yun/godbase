package com.ziroom.godbase;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.ziroom.godbase.config.ConfigManager;
import com.ziroom.godbase.config.LogUtils;
import com.ziroom.godbase.config.ParamsInterceptor;
import com.ziroom.godbase.config.SSLSocketClient;
import com.ziroom.net.RetrofitManager;

import java.util.concurrent.TimeUnit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Description:App
 **/
public class App extends Application {
    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        ARouter.init(this);
        RetrofitManager.initClient(getOkhttpClient(), ConfigManager.getInstance().getHost());
        initNetInfo();
        initOkhttpUtils();
    }

    public static App getApp() {
        return app;
    }

    /**
     * 创建OkHttpClient
     */
    private static OkHttpClient getOkhttpClient() {
        HttpLoggingInterceptor.Logger logger =
                (String message) -> LogUtils.d("OkHttp_Log", message);
        OkHttpClient.Builder builder = RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(
                        new HttpLoggingInterceptor(logger).setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new ParamsInterceptor()));

        if (BuildConfig.DEBUG) {
            builder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory());
            builder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
        }
        return builder.build();
    }

    private void initOkhttpUtils() {
        //okhttp
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager).build();
        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 初始化域名信息
     */
    public static void initNetInfo() {
        RetrofitUrlManager.getInstance().putDomain(RetrofitManager.DOMAIN_ABC_KEY, ConfigManager.getInstance().getHost());
    }
}
