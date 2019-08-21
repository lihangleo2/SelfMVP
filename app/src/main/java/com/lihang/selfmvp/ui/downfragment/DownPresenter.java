package com.lihang.selfmvp.ui.downfragment;

import com.lihang.selfmvp.base.BasePresenter;
import com.lihang.selfmvp.common.SystemConst;
import com.lihang.selfmvp.retrofitwithrxjava.downloadutils.FileDownLoadObserver;
import com.lihang.selfmvp.utils.LogUtils;
import java.io.File;
import io.reactivex.disposables.Disposable;

/**
 * Created by leo
 * on 2019/8/21.
 */
public class DownPresenter extends BasePresenter<DownContract.View> implements DownContract.Presenter {


    Disposable downDisposable;

    @Override
    public void downFile(String destDir, String fileName) {
        if (!isViewAttached()) {
            return;
        }
        downLoadFile(SystemConst.QQ_APK, destDir, fileName, new FileDownLoadObserver<File>() {
            @Override
            public void onDownLoadSuccess(File file) {
                getView().showDownSuccess(file);
            }

            @Override
            public void onDownLoadFail(Throwable throwable) {
                getView().showDownFail(throwable.toString());
            }

            @Override
            public void onProgress(int precent, long total) {
                getView().showDownProgress(precent,total);
                LogUtils.i("下载文件哦", "下载进度 ==> " + precent + "                 " + "文件大小 ==> " + total);
            }

            @Override
            public void onSubscribe(Disposable d) {
                super.onSubscribe(d);
                downDisposable = d;
                addDisposable(d);
            }
        });
    }

    @Override
    public void downResumeFile(String destDir, String fileName) {
        File exFile = new File(destDir, fileName);
        long currentLength = 0;
        if (exFile.exists()) {
            currentLength = exFile.length();
        }
        downLoadFile(SystemConst.QQ_APK, destDir, fileName, currentLength, new FileDownLoadObserver<File>() {
            @Override
            public void onDownLoadSuccess(File file) {
                getView().showDownSuccess(file);
            }

            @Override
            public void onDownLoadFail(Throwable throwable) {
                getView().showDownFail(throwable.toString());
            }

            @Override
            public void onProgress(int precent, long total) {
                getView().showDownProgress(precent,total);
                LogUtils.i("下载文件哦", "下载进度 ==> " + precent + "                 " + "文件大小 ==> " + total);
            }

            @Override
            public void onSubscribe(Disposable d) {
                super.onSubscribe(d);
                downDisposable = d;
                addDisposable(d);
            }
        });
    }

    @Override
    public void pauseDown() {
        if (!downDisposable.isDisposed()) {
            downDisposable.dispose();
        }
    }
}
