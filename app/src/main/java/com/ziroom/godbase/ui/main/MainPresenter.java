package com.ziroom.godbase.ui.main;

import com.ziroom.godbase.model.FileDo;
import com.ziroom.godbase.model.InkeListDo;
import com.ziroom.godbase.service.AppService;
import com.ziroom.mvp.base.BaseMvpPresenter;
import com.ziroom.net.ApiUtil;
import com.ziroom.net.OnResponseListener;

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
                if(entity != null && entity.size() > 0) {
                    mView.getMvpResult("第一个用户为" + entity.get(0).getName());
                } else {
                    mView.getMvpResult("用户列表为空");
                }
            }
        });
    }

    @Override
    public void createFileRequest(String classPath, String className) {
        Map<String, String> map = new HashMap<>();
        map.put("classPath", classPath); //class相对路径 Copy -> Absolute Path 最后不带/  eq: /Users/guanzhen/godbase/app/src/main/java/com/ziroom/godbase/ui
        map.put("className", className); //class名字 SplashActivity
        ApiUtil.getResponse(ApiUtil.getService(AppService.class).createFileRequest(map), new OnResponseListener<FileDo>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(FileDo entity) {
                if(entity != null) {
                    mView.createFileResult(entity);
                }
            }
        });
    }

}
