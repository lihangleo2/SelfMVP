package com.lihang.selfmvp.retrofitwithrxjava.downloadutils;

import android.os.Handler;
import android.os.Looper;

import com.lihang.selfmvp.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DefaultObserver;
import okhttp3.ResponseBody;

/**
 * Created by leo
 * on 2019/8/21.
 */
public abstract class FileDownLoadObserver<T> implements Observer<T> {
    Handler mDelivery = new Handler(Looper.getMainLooper());

    @Override
    public void onNext(final T t) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                onDownLoadSuccess(t);
            }
        });
    }

    @Override
    public void onError(final Throwable e) {

        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                onDownLoadFail(e);
            }
        });
    }

    //可以重写，具体可由子类实现
    @Override
    public void onComplete() {
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    //下载成功的回调
    public abstract void onDownLoadSuccess(T t);

    //下载失败回调
    public abstract void onDownLoadFail(Throwable throwable);

    //下载进度监听
    public abstract void onProgress(int precent, long total);

    /**
     * 将文件写入本地
     *
     * @param responseBody 请求结果全体
     * @param destFileDir  目标文件夹
     * @param destFileName 目标文件名
     * @return 写入完成的文件
     * @throws IOException IO异常
     */
    //这里是非断点下载， 可以理解为正常下载
    public File saveFile(ResponseBody responseBody, String destFileDir, String destFileName) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = responseBody.byteStream();
            final long total = responseBody.contentLength();
            long sum = 0;

            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                //这里就是对进度的监听回调
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        onProgress((int) (finalSum * 100 / total), total);
                    }
                });

            }
            fos.flush();

            return file;

        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //这里为 断点下载
    public File saveFile(ResponseBody responseBody, String destFileDir, String destFileName, long currentLength) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = responseBody.byteStream();
            final long total = responseBody.contentLength() + currentLength;
            long sum = currentLength;

            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file, true);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                //这里就是对进度的监听回调
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        onProgress((int) (finalSum * 100 / total), total);
                    }
                });
            }
            fos.flush();

            return file;

        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
