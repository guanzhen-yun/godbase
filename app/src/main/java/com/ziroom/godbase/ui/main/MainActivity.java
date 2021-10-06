package com.ziroom.godbase.ui.main;

import android.Manifest;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ziroom.base.BaseActivity;
import com.ziroom.base.RouterUtils;
import com.ziroom.godbase.R;
import com.ziroom.godbase.model.FileDo;
import com.ziroom.godbase.routers.RouterConstants;
import com.ziroom.godbase.ui.SplashActivity;
import com.ziroom.godbase.util.ToastUtils;
import com.ziroom.net.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * view
 */

@Route(path = RouterConstants.App.Main)
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.IView {
    @BindView(R.id.et_address)
    EditText mEtAddress;
    @BindView(R.id.et_classname)
    EditText mEtClassname;
    @BindView(R.id.btn_createfile)
    Button mBtnCreatefile;

    @Override
    public void initViews() {
        //运行时权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    @OnClick({R.id.tv, R.id.tv_config, R.id.tv_create, R.id.btn_createfile, R.id.btn_download, R.id.btn_upload})
    public void onViewClicked(View v) {
        if (v.getId() == R.id.tv) {
            mPresenter.sendMvpRequest();
        } else if (v.getId() == R.id.tv_config) {
            RouterUtils.jump(RouterConstants.App.Config);
        } else if (v.getId() == R.id.tv_create) {
            setTestData();
            mEtAddress.setVisibility(View.VISIBLE);
            mEtClassname.setVisibility(View.VISIBLE);
            mBtnCreatefile.setVisibility(View.VISIBLE);
        } else if(v.getId() == R.id.btn_createfile) {
            String address = mEtAddress.getText().toString();
            String className = mEtClassname.getText().toString();
            if (TextUtils.isEmpty(address) || TextUtils.isEmpty(className)) {
                ToastUtils.showShortToast("类绝对地址和类名不能为空");
            } else {
                mPresenter.createFileRequest(address, className);
            }
        } else if(v.getId() == R.id.btn_download) {
            //http://192.168.2.12:8080/file/classes2.dex
            downLoadFile("http://192.168.2.12:8080/file/classes2.dex",
                    getExternalCacheDir().getAbsolutePath());//需要配置tomcat的配置文件 映射关系 tomcat/conf/server.xml
        } else if(v.getId() == R.id.btn_upload) {
            File file = new File(Environment.getExternalStorageDirectory(), "bigpng.jpg");
            mPresenter.uploadFile(file);
        }
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            ToastUtils.showShortToast("文件下载成功");
        }
    };

    /**
     * 下载文件
     * @param fileUrl 文件url
     * @param destFileDir 存储目标目录
     */
    public void downLoadFile(String fileUrl, final String destFileDir) {
        final File file = new File(destFileDir, "classes2.dex");
        if (file.exists()) {
            ToastUtils.showShortToast("文件已下载");
            return;
        }

        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(fileUrl).build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    handler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 测试数据
     */
    private void setTestData() {
        mEtAddress.setText("/Users/guanzhen/godbase/app/src/main/java/com/ziroom/godbase/ui");
        mEtClassname.setText("TestActivity");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainPresenter getPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public boolean isSetDarkStatusFrontColor() {
        return true;
    }

    @Override
    public void getMvpResult(String result) {
        ToastUtils.showShortToast(result);
    }

    @Override
    public void createFileResult(FileDo fileDo) {
        ToastUtils.showShortToast(fileDo.getFileResult());
        mEtAddress.setVisibility(View.GONE);
        mEtClassname.setVisibility(View.GONE);
        mEtAddress.setText("");
        mEtClassname.setText("");
        mBtnCreatefile.setVisibility(View.GONE);
    }
}
