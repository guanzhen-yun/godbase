package com.ziroom.net.rx;

import android.text.TextUtils;


import com.ziroom.net.bean.Result;
import com.ziroom.net.exception.ServerException;

import io.reactivex.functions.Function;

/**
 * 没有返回body
 */
public class ServerNoBodyFunc implements Function<Result, Result> {
    @Override
    public Result apply(Result httpResult) {
        if (!TextUtils.isEmpty(httpResult.getErrcode())) {
            if (!"0".equals(httpResult.getErrcode()) && !"0.0".equals(httpResult.getErrcode()) && !"1000".equals(httpResult.getErrcode())) {
                throw new ServerException(Integer.parseInt(httpResult.getErrcode()), Integer.parseInt(httpResult.getErrcode()), httpResult.getErrmsg());
            } else {
                return httpResult;
            }
        } else if (httpResult.getError() == 0 && (httpResult.getCode() == 1000 || httpResult.getCode() == 0 || httpResult.getCode() == 200)) {
            return httpResult;
        } else {
            throw new ServerException(httpResult.getCode(), httpResult.getError(), httpResult.getMessage());
        }
    }
}
