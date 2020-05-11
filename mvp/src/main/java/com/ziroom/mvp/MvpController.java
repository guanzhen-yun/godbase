package com.ziroom.mvp;

import android.content.Intent;
import android.os.Bundle;

import java.util.HashSet;
import java.util.Set;

/**
 * Description:MvpController presenter的控制器 绑定生命周期
 **/
public class MvpController implements ILifeCircle {

    //存放的是 P层的实例
    private Set<ILifeCircle> mLifeCircles = new HashSet<>();

    public void savePresenter(ILifeCircle lifeCircle) {
        this.mLifeCircles.add(lifeCircle);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, Intent intent, Bundle getArguments) {
        for (ILifeCircle next : this.mLifeCircles) {
            if (intent == null) {
                intent = new Intent();
            }
            if (getArguments == null) {
                getArguments = new Bundle();
            }
            next.onCreate(savedInstanceState, intent, getArguments);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState, Intent intent, Bundle getArguments) {
        for (ILifeCircle next : this.mLifeCircles) {
            if (intent == null) {
                intent = new Intent();
            }
            if (getArguments == null) {
                getArguments = new Bundle();
            }
            next.onActivityCreated(savedInstanceState, intent, getArguments);
        }
    }

    @Override
    public void onStart() {
        for (ILifeCircle next : this.mLifeCircles) {
            next.onStart();
        }
    }

    @Override
    public void onResume() {
        for (ILifeCircle next : this.mLifeCircles) {
            next.onResume();
        }
    }

    @Override
    public void onPause() {
        for (ILifeCircle next : this.mLifeCircles) {
            next.onPause();
        }
    }

    @Override
    public void onStop() {
        for (ILifeCircle next : this.mLifeCircles) {
            next.onStop();
        }
    }

    @Override
    public void onDestroy() {
        for (ILifeCircle next : this.mLifeCircles) {
            next.onDestroy();
        }
    }

    @Override
    public void destroyView() {
        for (ILifeCircle next : this.mLifeCircles) {
            next.destroyView();
        }
    }

    @Override
    public void onViewDestroy() {
        for (ILifeCircle next : this.mLifeCircles) {
            next.onViewDestroy();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        for (ILifeCircle next : this.mLifeCircles) {
            next.onNewIntent(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (ILifeCircle next : this.mLifeCircles) {
            next.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        for (ILifeCircle next : this.mLifeCircles) {
            next.onSaveInstanceState(bundle);
        }
    }

    @Override
    public void attachView(IMvpView iMvpView) {
        for (ILifeCircle next : this.mLifeCircles) {
            next.attachView(iMvpView);
        }
    }
}
