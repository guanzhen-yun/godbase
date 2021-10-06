package com.ziroom.godbase.ui.main;

import com.ziroom.godbase.model.FileDo;
import com.ziroom.mvp.ILifeCircle;
import com.ziroom.mvp.IMvpView;

import java.io.File;
import java.io.FileFilter;

/**
 * Description:MainContract contract
 **/
public interface MainContract {
    interface IView extends IMvpView {
        void getMvpResult(String result);
        void createFileResult(FileDo fileDo);
    }

    interface IPresenter extends ILifeCircle {
        void sendMvpRequest();
        void createFileRequest(String classPath, String className);
        void uploadFile(File file);
    }
}
