package com.ziroom.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ziroom.mvp.view.LifeCircleMvpFragment;

import butterknife.ButterKnife;

/**
 * Author:关震
 * Date:2020/4/27 14:03
 * Description:BaseFragment fragment基类
 **/
public abstract class BaseFragment<T> extends LifeCircleMvpFragment {

    protected Context mContext;
    protected T mPresenter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = null;
        ViewInject annotation = this.getClass().getAnnotation(ViewInject.class);
        if (annotation != null) {
            int mainlayoutid = annotation.layoutId();
            if (mainlayoutid > 0) {
                mView = initFragmentView(inflater, mainlayoutid);
                bindView(mView);
                afterBindView();
            } else {
                throw new RuntimeException("mainlayoutid < 0");
            }
        } else {
            throw new RuntimeException("annotation = null");
        }
        mPresenter = getPresenter();
        return mView;
    }

    private View initFragmentView(LayoutInflater inflater, int mainlayoutid) {
        return inflater.inflate(mainlayoutid, null);
    }

    //模板方法 设计模式
    public abstract void afterBindView();

    //View 的依赖注入绑定
    private void bindView(View mView) {
        ButterKnife.bind(this, mView);
    }

    public T getPresenter() {
        return null;
    }
}
