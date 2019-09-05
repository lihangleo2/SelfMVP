package com.lihang.selfmvp.ui.postfragment;

import android.util.Log;

import com.lihang.selfmvp.base.BasePresenter;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Created by leo
 * on 2019/8/19.
 */
public class PostPresenter extends BasePresenter<PostContract.View> implements PostContract.Prensenter {

    @Inject
    public PostPresenter(){

    }
    @Override
    public void getPostData(HashMap<String, String> postParm) {
        if (!isViewAttached()) {
            return;
        }
        observe(apiService().postAddGank(postParm))
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        getView().showPostData(responseBody.string());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().showPostError(throwable.toString());
                    }
                });


    }

    @Override
    public void getPostDataWithRetryTime(HashMap<String, String> postParm, int retryMaxCount) {
        //重连部分封装到了basePresenter里，那么子页面要重连只要调用就好了
        // 不需要每次都要写一大推，偷懒万岁
        observeWithRetry(apiService().postAddGank(postParm),retryMaxCount)
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        getView().showPostData(responseBody.string());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().showPostError(throwable.toString());
                    }
                });

    }


}
