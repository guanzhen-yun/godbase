package com.ziroom.godbase;

import com.ziroom.mvp.ILifeCircle;
import com.ziroom.mvp.IMvpView;

/**
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
