package com.ziroom.net;

import android.util.Log;

import com.ziroom.net.exception.ApiException;

import io.reactivex.disposables.Disposable;

public abstract class OnResponseListener<T> {
    public void onError(ApiException e) {
        Log.d(RetrofitManager.TAG, e.getDisplayMessage());
    }
    public abstract void onSubscribe(Disposable d);
    public abstract void onNext(T entity);
}
