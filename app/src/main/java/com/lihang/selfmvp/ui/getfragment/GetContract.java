package com.lihang.selfmvp.ui.getfragment;


import com.lihang.selfmvp.base.BaseView;

/**
 * Created by leo
 * on 2019/8/16.
 */
/*
 * get界面页面的契约类，
 * 从这个类中对get页面的有哪些工作一目了然，这也是mvp一大优点
 * view 层有2个更改UI的操作：showGetData    showGetError
 * P 层有一个网络请求的功能：这里因为用了RxJava结合Retrofit使用，有自己的观察者回调，所以这里有点把他们当成了Model了。
 * 这也是retrofit请求的一大落点，在注解里就定死了返回类型，导致有多少个网络请求，就要写几遍
 * */
public interface GetContract {

    //view只有2个更新ui的方法
    interface View extends BaseView {
        // 1、获取get数据，更新ui
        void showGetData(String data);

        // 2、获取get数据失败，更新ui
        void showGetError(String msg);
    }

    //get的prensenter只有一个获取get数据的数据请求
    interface Prensenter {
        // 1、开启get网络请求
        public void getGetData(String params);
    }
}
