package com.ziroom.godbase.ui.main;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ziroom.base.BaseActivity;
import com.ziroom.base.RouterUtils;
import com.ziroom.godbase.R;
import com.ziroom.godbase.model.FileDo;
import com.ziroom.godbase.routers.RouterConstants;
import com.ziroom.godbase.util.ToastUtils;
import com.ziroom.net.LogUtils;

import java.io.File;

import butterknife.OnClick;

/**
 * view
 */

@Route(path = RouterConstants.App.Main)
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.IView {

    @OnClick({R.id.tv, R.id.tv_config, R.id.tv_create})
    public void onViewClicked(View v) {
        if (v.getId() == R.id.tv) {
            mPresenter.sendMvpRequest();
        } else if (v.getId() == R.id.tv_config) {
            RouterUtils.jump(RouterConstants.App.Config);
        } else if (v.getId() == R.id.tv_create) {
            mPresenter.createFileRequest("godbase", "app.src.main", "java.com.ziroom.godbase.ui", "TestActivity");
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

    @Override
    public void getMvpResult(String result) {
        ToastUtils.showShortToast(result);
    }

    @Override
    public void createFileResult(FileDo fileDo) {
        ToastUtils.showShortToast(fileDo.getFileResult());
    }
}
