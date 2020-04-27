package com.ziroom.godbase.base;

import com.ziroom.base.GenericsUtils;
import com.ziroom.mvp.IMvpView;
import com.ziroom.mvp.base.BaseMvpPresenter;

import ziroom.zo.mvp.MvpEmptyViewFactory;

/**
 * Author:关震
 * Date:2020/4/27 14:04
 * Description:BasePresenter presenter基类
 **/
public abstract class BasePresenter <T extends IMvpView> extends BaseMvpPresenter<T> {
    public BasePresenter(T view) {
        super(view);
    }

    @Override
    protected T getEmptyView() {
        T t = null;
        Class superClassGenricType = GenericsUtils.getSuperClassGenricType(this.getClass(), 0);
        try {
            t = (T) MvpEmptyViewFactory.create(superClassGenricType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

}
