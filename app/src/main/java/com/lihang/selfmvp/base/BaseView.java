package com.lihang.selfmvp.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * Created by leo
 * on 2019/8/16.
 *
 * BaseView放的是每个activity和fragment上的通用方法
 */
public interface BaseView {

    //如果不显示通过presenter调用方法，传参来判断是否显示
     //显示正在加载view
    void showLoading(String message);


   // 关闭正在加载view

    void hideLoading();

    //防止内存泄漏
    LifecycleTransformer bindLifecycle();

}
