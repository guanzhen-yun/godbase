/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.ziroom.net;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.ziroom.net.config.ParamsInterceptor;
import com.ziroom.net.config.SSLSocketClient;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Retrofit管理
 */
public class RetrofitManager {
    private volatile static RetrofitManager instance = null;
    private Retrofit retrofit;

    private Map<String, String> mHeaders;
    private Map<String, String> mCommonParams;
    private String mBaseUrl;
    public static final String TAG = "OkHttp_Log";

    private RetrofitManager(){}

    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }

    public RetrofitManager setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
        return this;
    }

    public RetrofitManager setParams(Map<String, String> headers, Map<String, String> commonParams) {
        mHeaders = headers;
        mCommonParams = commonParams;
        return this;
    }

    public void init(Map<String, String> mHost) {
        retrofit = new Retrofit.Builder().baseUrl(mBaseUrl).client(getOkhttpClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        initOkhttpUtils();
        if(mHost != null) {
            for (String key : mHost.keySet()) {
                RetrofitUrlManager.getInstance().putDomain(key, mHost.get(key));
            }
        }
    }

    /**
     * 创建OkHttpClient
     */
    private OkHttpClient getOkhttpClient() {
        HttpLoggingInterceptor.Logger logger =
                (String message) -> LogUtils.d(TAG, message);
        OkHttpClient.Builder builder = RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(
                        new HttpLoggingInterceptor(logger).setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new ParamsInterceptor(mHeaders, mCommonParams)));

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

    public <T> T create(final Class<T> service) {
        if (retrofit != null) {
            return retrofit.create(service);
        }
        return null;
    }
}

