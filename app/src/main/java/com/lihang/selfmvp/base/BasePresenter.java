package com.lihang.selfmvp.base;


import com.lihang.selfmvp.common.SystemConst;
import com.lihang.selfmvp.retrofitwithrxjava.Interceptor.NetCacheInterceptor;
import com.lihang.selfmvp.retrofitwithrxjava.Interceptor.OfflineCacheInterceptor;
import com.lihang.selfmvp.retrofitwithrxjava.RetrofitApiService;
import com.lihang.selfmvp.retrofitwithrxjava.RetrofitManager;
import com.lihang.selfmvp.retrofitwithrxjava.downloadutils.FileDownLoadObserver;
import com.lihang.selfmvp.retrofitwithrxjava.uploadutils.FileUploadObserver;
import com.lihang.selfmvp.retrofitwithrxjava.uploadutils.UploadFileRequestBody;
import com.lihang.selfmvp.utils.LogUtils;
import com.trello.rxlifecycle2.RxLifecycle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by leo
 * on 2019/8/16.
 * 当然是放的通用方法
 */
public abstract class BasePresenter<V extends BaseView> {
    //这个是为了退出页面，取消请求的
    public CompositeDisposable compositeDisposable;

    /**
     * 绑定的view
     */
    private V mvpView;

    /**
     * 绑定view，一般在初始化中调用该方法
     */
    public void attachView(V mvpView) {
        this.mvpView = mvpView;
        compositeDisposable = new CompositeDisposable();
    }

    /**
     * 断开view，一般在onDestroy中调用
     */
    public void detachView() {
        this.mvpView = null;
        //退出页面的时候移除所有网络请求
        removeDisposable();
    }

    /**
     * 是否与View建立连接
     * 每次调用业务请求的时候都要出先调用方法检查是否与View建立连接
     */
    public boolean isViewAttached() {
        return mvpView != null;
    }

    /**
     * 获取连接的view
     */
    public V getView() {
        return mvpView;
    }

    public RetrofitApiService apiService() {
        return RetrofitManager.getRetrofitManager().getApiService();
    }


    /**
     * 下面都是为了偷懒，方便封装的。，，
     */

    public <T> Observable<T> observe(Observable<T> observable) {
        return observe(observable, true, "");
    }

    public <T> Observable<T> observe(Observable<T> observable, boolean showDialog) {
        return observe(observable, showDialog, "");
    }

    public <T> Observable<T> observe(Observable<T> observable, final boolean showDialog, final String message) {

        return observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (showDialog) {
                            mvpView.showLoading(message);
                        }
                    }
                })
//                .subscribeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (showDialog) {
                            //这里真的用于实战，在方法里定义个tag = System.currentTimeMillis();
                            //在showloading的时候传入loadingDialog,取消显示的时候在hideloading(tag)里把tag取出来对比，一致则取消。
                            //以防异步取消掉loading，以至错乱。图片显示进度的时候最好带个图片tag.在设置进度的时候最好取出来比对
                            mvpView.hideLoading();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                //防止RxJava内存泄漏
                .compose(mvpView.bindLifecycle());
    }


    /**
     * 这是带断线重连的，封装在这里是为了偷懒
     */

    public <T> Observable<T> observeWithRetry(Observable<T> observable, int retryMaxCount) {
        return observeWithRetry(observable, true, "", retryMaxCount);
    }

    public <T> Observable<T> observeWithRetry(Observable<T> observable, boolean showDialog, int retryMaxCount) {
        return observeWithRetry(observable, showDialog, "", retryMaxCount);
    }


    public <T> Observable<T> observeWithRetry(Observable<T> observable, final boolean showDialog, final String message, int retryMaxCount) {
        final int maxCount = retryMaxCount;
        final int[] currentCount = {0};
        return observable.subscribeOn(Schedulers.io())
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {

                                //如果还没到次数，就延迟5秒发起重连
                                LogUtils.i("我看看是不是在重连", "当前的重连次数 == " + currentCount[0]);
                                if (currentCount[0] <= maxCount) {
                                    currentCount[0]++;
                                    return Observable.just(1).delay(5000, TimeUnit.MILLISECONDS);
                                } else {
                                    //到次数了跑出异常
                                    return Observable.error(new Throwable("重连次数已达最高,请求超时"));
                                }
                            }
                        });
                    }
                }).doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (showDialog) {
                            mvpView.showLoading(message);
                        }
                    }
                }).doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (showDialog) {
                            mvpView.hideLoading();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                //防止RxJava内存泄漏
                .compose(mvpView.bindLifecycle());
    }

    /**
     * 取消网络准备
     */

    public void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void removeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }


    //设置在线网络缓存
    public void setOnlineCacheTime(int time) {
        NetCacheInterceptor.getInstance().setOnlineTime(time);
    }

    //设置离线网络缓存
    public void setOfflineCacheTime(int time) {
        OfflineCacheInterceptor.getInstance().setOfflineCacheTime(time);
    }


    //在网络还在请求时，多次点击只请求一次
    public void addOneNet(String tag) {
        if (RetrofitManager.getRetrofitManager().getOneNetList().contains(tag)) {
            return;
        } else {
            RetrofitManager.getRetrofitManager().getOneNetList().add(tag);
        }
    }

    public void removeTag(String tag) {
        RetrofitManager.getRetrofitManager().getOneNetList().remove(tag);
    }


    //这里是监听图片下载进度的
    //单张图片
    public void uploadWithListener(String url, RequestBody requestBody, File file, FileUploadObserver fileUploadObserver) {
        UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(file, fileUploadObserver);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), uploadFileRequestBody);
        observe(apiService().uploadPic(url, requestBody, body))
                .subscribe(fileUploadObserver);
    }

    //这里是多张图片,不同key,不同图片
    public void uploadWithListener(String url, RequestBody requestBody, Map<String, File> fileMap, FileUploadObserver fileUploadObserver) {
        UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(fileMap, fileUploadObserver);
        MultipartBody.Part body = MultipartBody.Part.create(uploadFileRequestBody);
        observe(apiService().uploadPic(url, requestBody, body))
                .subscribe(fileUploadObserver);
    }

    //这里是多张图片,同一个key,不同图片
    public void uploadWithListener(String url, RequestBody requestBody, ArrayList<File> files, String key, FileUploadObserver fileUploadObserver) {
        UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(key, files, fileUploadObserver);
        MultipartBody.Part body = MultipartBody.Part.create(uploadFileRequestBody);
        observe(apiService().uploadPic(url, requestBody, body))
                .subscribe(fileUploadObserver);
    }

    /**
     * 这是下载文件
     */
    public void downLoadFile(String url, final String destDir, final String fileName, final FileDownLoadObserver<File> fileFileDownLoadObserver) {
        apiService().downloadFile(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        return fileFileDownLoadObserver.saveFile(responseBody, destDir, fileName);
                    }
                }).compose(mvpView.bindLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileFileDownLoadObserver);

    }


    public void downLoadFile(String url, final String destDir, final String fileName, final long currentLength, final FileDownLoadObserver<File> fileFileDownLoadObserver) {
        String range = "bytes=" + currentLength + "-";
        apiService().downloadFile(url,range)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        return fileFileDownLoadObserver.saveFile(responseBody, destDir, fileName,currentLength);
                    }
                })
                .compose(mvpView.bindLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileFileDownLoadObserver);
    }
}
