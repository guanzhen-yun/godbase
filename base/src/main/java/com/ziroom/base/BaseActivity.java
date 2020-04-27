package com.ziroom.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ziroom.mvp.view.LifeCircleMvpActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author:关震
 * Date:2020/4/27 14:02
 * Description:BaseActivity Activity 基类
 **/
public abstract class BaseActivity<T> extends LifeCircleMvpActivity {

    private Unbinder mUnbinder;

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInject annotation = this.getClass().getAnnotation(ViewInject.class);
        if(annotation != null) {
            int mainlayoutid = annotation.layoutId();
            if(mainlayoutid > 0) {
                setContentView(mainlayoutid);
                bindView();
                afterBindView();
            } else {
                throw new RuntimeException("mainlayoutid < 0");
            }
        } else {
            throw new RuntimeException("annotation = null");
        }
        mPresenter = getPresenter();
    }

    //模板方法 设计模式
    public abstract void afterBindView();

    //View 的依赖注入绑定
    private void bindView() {
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    public T getPresenter() {
        return null;
    }
}
