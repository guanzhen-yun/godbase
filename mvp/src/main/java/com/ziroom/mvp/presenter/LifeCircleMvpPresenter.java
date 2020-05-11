package com.ziroom.mvp.presenter;

import com.ziroom.mvp.ILifeCircle;
import com.ziroom.mvp.IMvpView;
import com.ziroom.mvp.MvpController;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Description:LifeCircleMvpPresenter 带有生命周期的presenter
 **/
public abstract class LifeCircleMvpPresenter <T extends IMvpView> implements ILifeCircle {

    private WeakReference<T> mWeakReference;
    protected T mView;
    private T mMvpView;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    protected LifeCircleMvpPresenter() {
        super();
    }

    public LifeCircleMvpPresenter(T iMvpView) {
        super();
        attachView(iMvpView);
        MvpController mvpControler = iMvpView.getMvpController();
        mvpControler.savePresenter(this);
    }

    @Override
    public void attachView(IMvpView iMvpView) {
        if (mWeakReference == null) {
            mWeakReference = new WeakReference(iMvpView);
        } else {
            mMvpView = mWeakReference.get();
            if (mMvpView != iMvpView) {
                mWeakReference = new WeakReference(iMvpView);
            }
        }

        mView = (T) Proxy.newProxyInstance(iMvpView.getClass().getClassLoader(), iMvpView.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                if(mWeakReference == null || mWeakReference.get() == null) {
                    return null;
                }
                return method.invoke(mWeakReference.get(), objects);
            }
        });
    }

    @Override
    public void onDestroy() {
        mMvpView = null;
        mWeakReference = null;
        mDisposable.clear();
    }

    protected T getView() {
        return mView;
    }

    protected void addDisposable(Disposable disposable) {
        mDisposable.add(disposable);
    }
}
