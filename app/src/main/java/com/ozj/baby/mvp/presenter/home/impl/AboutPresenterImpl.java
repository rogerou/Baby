package com.ozj.baby.mvp.presenter.home.impl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.model.bean.UpdateVersion;
import com.ozj.baby.mvp.presenter.home.IAboutPresenter;
import com.ozj.baby.mvp.views.home.IAboutView;

import javax.inject.Inject;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

/**
 * Created by YX201603-6 on 2016/5/3.
 */
public class AboutPresenterImpl implements IAboutPresenter {


    private final Context mContext;
    private IAboutView mAboutView;

    @Inject
    public AboutPresenterImpl(@ContextLife("Activity") Context context) {
        mContext = context;
    }

    @Override
    public void checkVersion() {
        FIR.checkForUpdateInFIR("85d4be69c008c7b81331a0476728d459", new VersionCheckCallback() {
            @Override
            public void onSuccess(String versionJson) {
                mAboutView.showToast("正在获取更新");
                Log.i("fir", "check from fir.im success! " + "\n" + versionJson);
                UpdateVersion version = JSON.parseObject(versionJson, UpdateVersion.class);

                if (getAppVersionCode(mContext) < version.getVersion()) {
                    mAboutView.toWebView(version.getInstallUrl());
                } else {
                    mAboutView.showToast("目前是最新版本");
                }

            }

            @Override
            public void onFail(Exception exception) {
                Log.i("fir", "check fir.im fail! " + "\n" + exception.getMessage());
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onFinish() {
            }
        });
    }

    @Override
    public void downloadApk(final String url) {
//        dialog.setIndeterminate(false);
//        dialog.setCancelable(false);
//        dialog.setMessage("下载中...");
//        dialog.show();
//        Observable.create(new Observable.OnSubscribe<File>() {
//
//            @Override
//            public void call(Subscriber<? super File> subscriber) {
//                OkHttpClient client = new OkHttpClient();
//                Request request = new Request.Builder().url(url).get().build();
//                okhttp3.Response response = null;
//                InputStream inputStream = null;
//                FileOutputStream fileOutputStream = null;
//                int len = 0;
//                byte[] buf = new byte[2048];
//                try {
//                    response = client.newCall(request).execute();
//                    inputStream = response.body().byteStream();
//                    final int total = (int) (response.body().contentLength() / 1024);
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            dialog.setMax(total);
//                        }
//                    });
//                    long sum = 0;
//                    File dir = Environment.getExternalStorageDirectory();
//                    if (!dir.exists()) {
//                        dir.mkdir();
//                    }
//                    File file = new File(dir, "NewBaby");
//                    fileOutputStream = new FileOutputStream(file);
//                    while ((len = inputStream.read(buf)) != -1) {
//                        sum += len;
//                        fileOutputStream.write(buf, 0, len);
//                        final int finalSum = (int) (sum / 1024);
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                dialog.setProgress(finalSum);
//                            }
//                        });
//
//                    }
//                    fileOutputStream.flush();
//                    subscriber.onNext(file);
//                } catch (IOException e) {
//                    subscriber.onError(e);
//                    e.printStackTrace();
//                } finally {
//
//                    try {
//                        if (inputStream != null) {
//                            inputStream.close();
//                        }
//                        if (fileOutputStream != null) {
//                            fileOutputStream.close();
//                        }
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                subscriber.onCompleted();
//
//
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<File>() {
//                    @Override
//                    public void onCompleted() {
//                        dialog.dismiss();
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        dialog.dismiss();
//                        mAboutView.showToast(e.getMessage());
//
//                    }
//
//                    @Override
//                    public void onNext(File file) {
//                        openFile(file);
//                    }
//                });
    }

    @Override
    public void attachView(@NonNull BaseView view) {
        mAboutView = (IAboutView) view;
    }

    @Override
    public void detachView() {

    }

    public long getAppVersionCode(Context context) {
        String versionName = "";
        long versioncode = 0;
        try {
            // ---get the package info---  
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode;
    }

}
