package com.lihang.selfmvp.ui.getfragment;

import com.lihang.selfmvp.base.BasePresenter;
import com.lihang.selfmvp.bean.GankFatherBean;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by leo
 * on 2019/8/16.
 */
public class GetPresenter extends BasePresenter<GetContract.View> implements GetContract.Prensenter {
    @Inject
    public GetPresenter() {

    }

    //模拟联网， 要不要考虑封装在model里
    @Override
    public void getGetData(String params) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }

        //这里是避免重复请求(用方法名做参数)
        addOneNet("getGetData");


        //这里是加缓存
        setOnlineCacheTime(3600);
        setOfflineCacheTime(3600);
        // 调用Model请求数据,可以通过disponsable主动取消网络请求
        Disposable disposable = observe(apiService().getGank(params))
                .subscribe(new Consumer<GankFatherBean>() {
                    @Override
                    public void accept(GankFatherBean gankFatherBean) throws Exception {
                        getView().showGetData(gankFatherBean.getResults().get(0).getTitle());
                        //成功或失败的时候都要移除tag，不然请求不了
                        removeTag("getGetData");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().showGetError(throwable.toString());
                        removeTag("getGetData");
                    }
                });

        //取消网络的请求
//        if (!disposable.isDisposed()){
//            disposable.dispose();
//        }

        //如果是退出页面统一取消网络的话可以通过
        //当然这里都不需要应用指向disposable
//        addDisposable(disposable);

    }
}
