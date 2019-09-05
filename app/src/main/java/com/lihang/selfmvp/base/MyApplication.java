package com.lihang.selfmvp.base;

import android.content.Context;

import com.lihang.selfmvp.daggerforandroid.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;


/**
 * Created by leo
 * on 2019/7/19.
 * 主要是针对自己封装OkHttp
 */
public class MyApplication extends DaggerApplication {
    private static MyApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    public static Context getContext() {
        return context;
    }

}
