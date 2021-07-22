package com.ziroom.net.rx;


import android.text.TextUtils;


import com.ziroom.net.bean.Result;
import com.ziroom.net.exception.ServerException;

import io.reactivex.functions.Function;


/**
 *
 */

class ServerResultFunc<T> implements Function<Result<T>, T> {
    @Override
    public T apply(Result<T> httpResult) throws Exception {
        if (httpResult.getCode() == 200) {
            return httpResult.getData();
        }
        throw new ServerException(httpResult.getCode(), httpResult.getError(), httpResult.getMessage());
    }
}
