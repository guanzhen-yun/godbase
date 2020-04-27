package com.ziroom.mvp;

import android.content.Intent;
import android.os.Bundle;

/**
 * Author:关震
 * Date:2020/4/27 14:09
 * Description:ILifeCircle 用到的生命周期
 **/
public interface ILifeCircle {
    void onCreate(Bundle savedInstanceState, Intent intent, Bundle getArguments);
    void onActivityCreated(Bundle savedInstanceState, Intent intent, Bundle getArguments);
    void onStart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();
    void destroyView();
    void onViewDestroy();
    void onNewIntent(Intent intent);
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void onSaveInstanceState(Bundle bundle);
    void attachView(IMvpView iMvpView);
}
