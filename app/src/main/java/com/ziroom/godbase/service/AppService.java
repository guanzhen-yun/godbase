package com.ziroom.godbase.service;

import com.ziroom.net.RetrofitManager;
import com.ziroom.net.bean.Result;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

/**
 * Description:AppService
 **/
public interface AppService {
    @Headers({RetrofitManager.DOMAIN_ABC_HEADR})
    @GET("/xx/abc")
    Observable<Result<ArrayList<Object>>> getRequest(@QueryMap Map<String, String> map);
}
