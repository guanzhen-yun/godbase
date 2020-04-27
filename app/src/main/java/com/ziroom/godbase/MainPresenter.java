package com.ziroom.godbase;

import com.ziroom.godbase.base.BasePresenter;

/**
 * Author:关震
 * Date:2020/4/27 15:03
 * Description:MainPresenter presenter
 **/
public class MainPresenter extends BasePresenter<MainContract.IView> implements MainContract.IPresenter {
    MainPresenter(MainContract.IView view) {
        super(view);
    }


    @Override
    public void sendMvpRequest() {
        getView().getMvpResult("我是测试数据");
    }
}
