package com.ziroom.godbase.ui.main;

import com.ziroom.godbase.model.FileDo;
import com.ziroom.godbase.model.InkeListDo;
import com.ziroom.godbase.service.AppService;
import com.ziroom.godbase.util.ToastUtils;
import com.ziroom.mvp.base.BaseMvpPresenter;
import com.ziroom.net.ApiUtil;
import com.ziroom.net.OnResponseListener;
import com.ziroom.net.exception.ApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    @Override
    public void uploadFile(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.get("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        ApiUtil.getResponse(ApiUtil.getService(AppService.class).uploadFile(filePart), new OnResponseListener<FileDo>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(FileDo entity) {
                ToastUtils.showShortToast("上传成功" + entity.getFileResult());
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ToastUtils.showShortToast("上传失败" + e.getDisplayMessage());
            }
        });
    }

}
