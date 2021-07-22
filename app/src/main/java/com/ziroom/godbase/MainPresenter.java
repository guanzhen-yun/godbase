package com.ziroom.godbase;

import com.ziroom.godbase.model.InkeListDo;
import com.ziroom.godbase.service.AppService;
import com.ziroom.mvp.base.BaseMvpPresenter;
import com.ziroom.net.ApiUtil;
import com.ziroom.net.OnResponseListener;
import com.ziroom.net.exception.ApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Description:MainPresenter presenter
 **/
public class MainPresenter extends BaseMvpPresenter<MainContract.IView> implements MainContract.IPresenter {
    MainPresenter(MainContract.IView view) {
        super(view);
    }


    @Override
    public void sendMvpRequest() {
        Map<String, String> map = new HashMap<>();
        ApiUtil.getResponse(ApiUtil.getService(AppService.class).getRequest(map), new OnResponseListener<ArrayList<InkeListDo>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(ArrayList<InkeListDo> entity) {
                mView.getMvpResult("获取列表成功");
            }
        });
    }

}
