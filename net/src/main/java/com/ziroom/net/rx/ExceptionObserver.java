package com.ziroom.net.rx;

import com.ziroom.net.exception.ApiException;
import com.ziroom.net.exception.ERROR;

import io.reactivex.Observer;


/**
 * 异常
 */
public abstract class ExceptionObserver<T> implements Observer<T> {

    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            onError((ApiException) e);
        } else {
            ApiException ex = new ApiException(e, ERROR.UNKNOWN);
            ex.setDisplayMessage("未知错误");
            onError(ex);
        }
    }

    @Override
    public void onComplete() {

    }

    /**
     * 错误回调
     */
    protected abstract void onError(ApiException ex);
}

