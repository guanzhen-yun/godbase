package com.ziroom.net.rx;

import com.google.gson.JsonObject;
import com.ziroom.net.bean.Result;
import com.ziroom.net.exception.ServerException;

import org.json.JSONObject;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * 接口返回值装换类
 */
public class TransformUtils {
    /**
     * 返回的body为空
     */
    public static ObservableTransformer<Result, Result> noBodySchedulers() {
        return (resultObservable) ->
                resultObservable.map(new ServerNoBodyFunc()).onErrorResumeNext(new HttpNoBodyFunc()).unsubscribeOn(Schedulers.io()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 执行线程为IO线程，关注线程为主线程，增加异常处理和数据转换
     */
    public static <T> ObservableTransformer<Result<T>, T> defaultSchedulers() {
        return resultObservable -> resultObservable.map(new ServerResultFunc<T>()).
                onErrorResumeNext(new HttpResultFunc<T>())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static ObservableTransformer<JsonObject, JsonObject> jsonSchedulers() {
        return resultObservable -> resultObservable.map(jsonObject -> {
            JSONObject object = null;
            object = new JSONObject(jsonObject.toString());
            if (object.has("errcode")) {
                int errcode = object.getInt("errcode");
                if (0 != errcode) {
                    throw new ServerException(errcode, errcode, object.optString("errmsg"));
                }
            } else {
                if (object.optInt("error") != 0 || object.optInt("code") != 200) {
                    throw new ServerException(object.optInt("code"), object.optInt("error"), object.optString("message"));
                }
            }
            return jsonObject;
        }).onErrorResumeNext(new HttpResultFunc<JsonObject>()).unsubscribeOn(Schedulers.io()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     *
     */
    public static ObservableTransformer<String, String> strSchedulers() {
        return resultObservable -> resultObservable.map(str -> {
            JSONObject object = null;
            object = new JSONObject(str);
            if (object.has("errcode")) {
                int errcode = object.getInt("errcode");
                if (0 != errcode) {
                    throw new ServerException(errcode, errcode, object.optString("errmsg"));
                }
            } else {
                if (object.optInt("error") != 0 || object.optInt("code") != 200) {
                    throw new ServerException(object.optInt("code"), object.optInt("error"), object.optString("message"));
                }
            }
            return str;
        }).onErrorResumeNext(new HttpResultFunc<String>()).unsubscribeOn(Schedulers.io()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
