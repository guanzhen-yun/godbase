package com.ziroom.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ziroom.mvp.view.LifeCircleMvpActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Description:BaseActivity Activity 基类
 **/
public abstract class BaseActivity<T> extends LifeCircleMvpActivity {

    private Unbinder mUnbinder;

    protected T mPresenter;

    private boolean mIsRegistEventbus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isNotFitStatus()) {
            StatusBarUtil.with(this).init();
        }
        mIsRegistEventbus = registEventBus();
        if (mIsRegistEventbus) {
            EventBus.getDefault().register(this);
        }
        int mainlayoutid = getLayoutId();
        if (mainlayoutid > 0) {
            setContentView(mainlayoutid);
            bindView();
            mPresenter = getPresenter();
            fetchIntents();
            initViews();
            initDatas();
        } else {
            throw new RuntimeException("mainlayoutid < 0");
        }
    }

    public boolean isNotFitStatus() {
        return false;
    }

    /**
     * 初始化数据
     */
    public void initDatas() {
    }

    /**
     * 初始化页面
     */
    public void initViews() {
    }

    /**
     * 抓取参数
     */
    public void fetchIntents() {

    }

    //View 的依赖注入绑定
    private void bindView() {
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mIsRegistEventbus) {
            EventBus.getDefault().unregister(this);
        }
    }

    public abstract int getLayoutId();

    public T getPresenter() {
        return null;
    }

    public boolean registEventBus() {
        return false;
    }
}
