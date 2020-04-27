package com.ziroom.godbase;

import com.ziroom.mvp.ILifeCircle;
import com.ziroom.mvp.IMvpView;

/**
 * Author:关震
 * Date:2020/4/27 15:03
 * Description:MainContract contract
 **/
public interface MainContract {
    interface IView extends IMvpView {
        void getMvpResult(String result);
    }

    interface IPresenter extends ILifeCircle {
        void sendMvpRequest();
    }
}
