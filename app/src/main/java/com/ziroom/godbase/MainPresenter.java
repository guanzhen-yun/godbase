package com.ziroom.godbase;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.ziroom.mvp.base.BaseMvpPresenter;

/**
 * Author:关震
 * Date:2020/4/27 15:03
 * Description:MainPresenter presenter
 **/
public class MainPresenter extends BaseMvpPresenter<MainContract.IView> implements MainContract.IPresenter {
    MainPresenter(MainContract.IView view) {
        super(view);
    }


    @Override
    public void sendMvpRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            getView().getMvpResult("我是测试数据");
        }
    };

}
