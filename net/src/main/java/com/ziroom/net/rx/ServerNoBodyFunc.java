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
        if (httpResult.getCode() == 200) {
            return httpResult;
        }
        throw new ServerException(httpResult.getCode(), httpResult.getError(), httpResult.getMessage());
    }
}
