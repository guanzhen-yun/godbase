package com.ziroom.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ziroom.mvp.view.LifeCircleMvpFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Description:BaseFragment fragment基类
 **/
public abstract class BaseFragment<T> extends LifeCircleMvpFragment {

    protected Context mContext;
    protected T mPresenter;
    private boolean mIsRegistEventbus;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = null;
        mIsRegistEventbus = registEventBus();
        if (mIsRegistEventbus) {
            EventBus.getDefault().register(this);
        }
        int mainlayoutid = getLayoutId();
        if (mainlayoutid > 0) {
            mView = initFragmentView(inflater, mainlayoutid);
            bindView(mView);
            mPresenter = getPresenter();
            fetchIntents(getArguments());
            initViews(mView);
        } else {
            throw new RuntimeException("mainlayoutid < 0");
        }

        return mView;
    }

    /**
     * 抓取参数
     */

    public void fetchIntents(Bundle bundle) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDatas();
    }

    /**
     * 初始化数据
     */
    public void initDatas() {

    }

    /**
     * 初始化布局
     */
    public void initViews(View mView) {

    }

    private View initFragmentView(LayoutInflater inflater, int mainlayoutid) {
        return inflater.inflate(mainlayoutid, null);
    }

    //View 的依赖注入绑定
    private void bindView(View mView) {
        ButterKnife.bind(this, mView);
    }

    public T getPresenter() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mIsRegistEventbus) {
            EventBus.getDefault().unregister(this);
        }
    }

    public abstract int getLayoutId();

    public boolean registEventBus() {
        return false;
    }
}
