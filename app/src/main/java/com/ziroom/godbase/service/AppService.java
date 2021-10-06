package com.ziroom.godbase.service;

import com.ziroom.godbase.model.FileDo;
import com.ziroom.godbase.model.InkeListDo;
import com.ziroom.net.bean.Result;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

/**
 * Description:AppService
 **/
public interface AppService {
    @Headers({Host.commonHostDomain})
    @GET("inke/personList")
    Observable<Result<ArrayList<InkeListDo>>> getRequest(@QueryMap Map<String, String> map);

    @Headers({Host.commonHostDomain})
    @GET("inke/createFileRequest")
    Observable<Result<FileDo>> createFileRequest(@QueryMap Map<String, String> map);

    @Headers({Host.commonHostDomain})
    @Multipart
    @POST("inke/upload")
    Observable<Result<FileDo>> uploadFile(@Part MultipartBody.Part part);
}
