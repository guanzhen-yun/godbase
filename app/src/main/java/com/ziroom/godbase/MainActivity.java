package com.ziroom.godbase;

import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ziroom.base.BaseActivity;
import com.ziroom.godbase.routers.RouterConstants;

import butterknife.OnClick;

/**
 * view
 */

@Route(path = RouterConstants.App.Main)
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.IView {

    @Override
    public void getMvpResult(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.tv)
    public void onViewClicked() {
        mPresenter.sendMvpRequest();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainPresenter getPresenter() {
        return new MainPresenter(this);
    }
}
