package com.ziroom.net;

import com.ziroom.net.bean.Result;
import com.ziroom.net.exception.ApiException;
import com.ziroom.net.rx.ExceptionObserver;
import com.ziroom.net.rx.TransformUtils;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 *api请求工具类
 */
public class ApiUtil {

    public static <T> T getService(Class<T> clazz) {
        return RetrofitManager.getInstance().create(clazz);
    }

    public static <T> void getResponse(Observable<Result<T>> observable, OnResponseListener<T> listener) {
        Observable<T> observable1 = observable.compose(TransformUtils.defaultSchedulers());
        observable1.subscribe(new ExceptionObserver<T>() {
            @Override
            protected void onError(ApiException e) {
                if (listener != null) {
                    listener.onError(e);
                }
            }

            @Override
            public void onSubscribe(Disposable d) {
                if (listener != null) {
                    listener.onSubscribe(d);
                }
            }

            @Override
            public void onNext(T entity) {
                if (listener != null) {
                    listener.onNext(entity);
                }
            }
        });
    }

    public static void getResponseNoBody(Observable<Result> observable, OnResponseListener<Result> listener) {
        Observable<Result> observable1 = observable.compose(TransformUtils.noBodySchedulers());
        observable1.subscribe(new ExceptionObserver<Result>() {
            @Override
            protected void onError(ApiException e) {
                if (listener != null) {
                    listener.onError(e);
                }
            }

            @Override
            public void onSubscribe(Disposable d) {
                if (listener != null) {
                    listener.onSubscribe(d);
                }
            }

            @Override
            public void onNext(Result entity) {
                if (listener != null) {
                    listener.onNext(entity);
                }
            }
        });
    }
}
