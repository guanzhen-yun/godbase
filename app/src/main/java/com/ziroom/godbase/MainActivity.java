package com.ziroom.godbase;

import android.widget.Toast;

import com.ziroom.base.BaseActivity;
import com.ziroom.base.ViewInject;

import butterknife.OnClick;

/**
 * view
 */

@ViewInject(layoutId = R.layout.activity_main)
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
    public MainPresenter getPresenter() {
        return new MainPresenter(this);
    }
}
