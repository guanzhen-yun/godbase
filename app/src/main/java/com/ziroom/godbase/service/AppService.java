package com.ziroom.godbase.service;

import com.ziroom.godbase.model.InkeListDo;
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
    @Headers({Host.commonHostDomain})
    @GET("inke/personList")
    Observable<Result<ArrayList<InkeListDo>>> getRequest(@QueryMap Map<String, String> map);
}
