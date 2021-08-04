package com.ziroom.godbase.ui;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ziroom.base.BaseActivity;
import com.ziroom.godbase.BuildConfig;
import com.ziroom.godbase.R;
import com.ziroom.godbase.routers.RouterConstants;
import com.ziroom.godbase.service.Host;
import com.ziroom.godbase.util.SharedUtils;
import com.ziroom.net.LogUtils;

import butterknife.BindView;

/**
 * 配置页面
 */

@Route(path = RouterConstants.App.Config)
public class ConfigActivity extends BaseActivity {
    @BindView(R.id.tv_env)
    TextView tvEnv;
    @BindView(R.id.tv_log)
    TextView tvLog;

    public static int OPEN_LOG = 1;
    public static int CLOSE_LOG = 0;

    private int mIsOpenLog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_config;
    }

    @Override
    public void initViews() {
        mIsOpenLog = SharedUtils.getInstance().getInt(SharedUtils.IS_OPENLOG);
        if(mIsOpenLog == -1) {
            mIsOpenLog = BuildConfig.DEBUG ? OPEN_LOG : CLOSE_LOG;
        }
        setLog();

        String envType = SharedUtils.getInstance().getString(SharedUtils.ENV_TYPE);
        if(TextUtils.isEmpty(envType)) {
            tvEnv.setText(String.format("当前环境: %s", BuildConfig.DEBUG ? Host.DEV : Host.RELEASE));
        } else {
            tvEnv.setText(String.format("当前环境: %s", envType));
        }
    }

    public void textEnv(View view) {
        //切换测试环境
        setEnv(true);
    }

    public void releaseEnv(View view) {
        //切换正式环境
        setEnv(false);
    }

    public void clicklog(View view) {
        //切换log开关
        mIsOpenLog = 1 - mIsOpenLog;
        LogUtils.debug = mIsOpenLog == OPEN_LOG;
        setLog();
        SharedUtils.getInstance().saveInt(SharedUtils.IS_OPENLOG, mIsOpenLog);
    }

    private void setLog() {
        tvLog.setText(String.format("Log开关-%s", mIsOpenLog == OPEN_LOG ? "开" : "关"));
    }

    private void setEnv(boolean isDev) {
        SharedUtils.getInstance().saveString(SharedUtils.ENV_TYPE, isDev ? Host.DEV : Host.RELEASE);
        if(isDev) {
            SharedUtils.getInstance().saveString(SharedUtils.ENV, Host.commonHostDev);
        } else {
            SharedUtils.getInstance().saveString(SharedUtils.ENV, Host.commonHostRelease);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                killProcess();
            }
        }, 600);
    }

    private void killProcess() {
        int pid = android.os.Process.myPid(); //获得自己的pid
        android.os.Process.killProcess(pid);//通过pid自杀
    }
}
