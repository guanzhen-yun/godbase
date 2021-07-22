package com.ziroom.godbase;

import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ziroom.base.BaseActivity;
import com.ziroom.godbase.routers.RouterConstants;
import com.ziroom.godbase.service.ChangeEnvironmentManager;

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

    @OnClick({R.id.tv, R.id.tv_exchange})
    public void onViewClicked(View v) {
        if(v.getId() == R.id.tv) {
            mPresenter.sendMvpRequest();
        } else {
            ChangeEnvironmentManager.getInstance().saveCurrentEnviroment((1 - ChangeEnvironmentManager.getInstance().getIsDebug()));
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
}
