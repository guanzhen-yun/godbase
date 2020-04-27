package com.ziroom.mvp.presenter;

import com.ziroom.mvp.ILifeCircle;
import com.ziroom.mvp.IMvpView;
import com.ziroom.mvp.MvpController;

import java.lang.ref.WeakReference;

/**
 * Author:关震
 * Date:2020/4/27 14:22
 * Description:LifeCircleMvpPresenter 带有生命周期的presenter
 **/
public abstract class LifeCircleMvpPresenter <T extends IMvpView> implements ILifeCircle {

    private WeakReference<T> mWeakReference;

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
            T view = mWeakReference.get();
            if (view != iMvpView) {
                mWeakReference = new WeakReference(iMvpView);
            }
        }
    }

    @Override
    public void onDestroy() {
        mWeakReference = null;
    }

    protected T getView() {
        T view = mWeakReference != null ? mWeakReference.get() : null;
        if (view == null) {
            return getEmptyView();
        }
        return view;
    }

    protected abstract T getEmptyView();
}
