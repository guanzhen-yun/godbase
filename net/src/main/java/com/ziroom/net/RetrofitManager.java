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

import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
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
    private final static String DFT_DOMAIN = "https://api.abc.com";//域名
    public final static String DOMAIN_ABC_KEY = "abc";
    public final static String DOMAIN_ABC_HEADR = "Domain-Name: abc";
    /**
     * api.def.com
     */
    public final static String DOMAIN_DEF_HEADR = "Domain-Name: def";
    public final static String DOMAIN_DEF_KEY = "def";

    private RetrofitManager(OkHttpClient okHttpClient, String baseUrl) {
        OkHttpClient client;
        if (okHttpClient == null) {
            client = getOkhttpClient();
        } else {
            client = okHttpClient;
        }
        if (TextUtils.isEmpty(baseUrl)) {
            baseUrl = DFT_DOMAIN;
        }
        retrofit = new Retrofit.Builder().baseUrl(baseUrl).client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
    }

    public static RetrofitManager initClient(OkHttpClient client, String baseUrl) {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager(client, baseUrl);
                }
            }
        }
        return instance;
    }

    public <T> T create(final Class<T> service) {
        if (retrofit != null) {
            return retrofit.create(service);
        }
        return null;
    }

    public static RetrofitManager getInstance() {
        return initClient(null, null);
    }

    /**
     * 创建OkHttpClient
     */
    private static OkHttpClient getOkhttpClient() {
        return RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)).build();
    }

}

