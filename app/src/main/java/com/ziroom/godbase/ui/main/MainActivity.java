package com.ziroom.godbase.ui.main;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ziroom.base.BaseActivity;
import com.ziroom.base.RouterUtils;
import com.ziroom.godbase.R;
import com.ziroom.godbase.model.FileDo;
import com.ziroom.godbase.routers.RouterConstants;
import com.ziroom.godbase.ui.SplashActivity;
import com.ziroom.godbase.util.ToastUtils;
import com.ziroom.net.LogUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * view
 */

@Route(path = RouterConstants.App.Main)
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.IView {
    @BindView(R.id.et_address)
    EditText mEtAddress;
    @BindView(R.id.et_classname)
    EditText mEtClassname;
    @BindView(R.id.btn_createfile)
    Button mBtnCreatefile;

    @OnClick({R.id.tv, R.id.tv_config, R.id.tv_create, R.id.btn_createfile})
    public void onViewClicked(View v) {
        if (v.getId() == R.id.tv) {
            mPresenter.sendMvpRequest();
        } else if (v.getId() == R.id.tv_config) {
            RouterUtils.jump(RouterConstants.App.Config);
        } else if (v.getId() == R.id.tv_create) {
            setTestData();
            mEtAddress.setVisibility(View.VISIBLE);
            mEtClassname.setVisibility(View.VISIBLE);
            mBtnCreatefile.setVisibility(View.VISIBLE);
        } else if(v.getId() == R.id.btn_createfile) {
            String address = mEtAddress.getText().toString();
            String className = mEtClassname.getText().toString();
            if (TextUtils.isEmpty(address) || TextUtils.isEmpty(className)) {
                ToastUtils.showShortToast("类绝对地址和类名不能为空");
            } else {
                mPresenter.createFileRequest(address, className);
            }
        }
    }

    /**
     * 测试数据
     */
    private void setTestData() {
        mEtAddress.setText("/Users/guanzhen/godbase/app/src/main/java/com/ziroom/godbase/ui");
        mEtClassname.setText("TestActivity");
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
        mEtAddress.setVisibility(View.GONE);
        mEtClassname.setVisibility(View.GONE);
        mEtAddress.setText("");
        mEtClassname.setText("");
        mBtnCreatefile.setVisibility(View.GONE);
    }
}
