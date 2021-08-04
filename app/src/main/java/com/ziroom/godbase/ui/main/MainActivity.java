package com.ziroom.godbase.ui.main;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ziroom.base.BaseActivity;
import com.ziroom.base.RouterUtils;
import com.ziroom.godbase.R;
import com.ziroom.godbase.routers.RouterConstants;
import com.ziroom.godbase.util.ToastUtils;

import butterknife.OnClick;

/**
 * view
 */

@Route(path = RouterConstants.App.Main)
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.IView {

    @Override
    public void getMvpResult(String result) {
        ToastUtils.showShortToast(result);
    }

    @OnClick({R.id.tv, R.id.tv_config})
    public void onViewClicked(View v) {
        if(v.getId() == R.id.tv) {
            mPresenter.sendMvpRequest();
        } else {
            RouterUtils.jump(RouterConstants.App.Config);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainPresenter getPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public boolean isSetDarkStatusFrontColor() {
        return true;
    }
}
