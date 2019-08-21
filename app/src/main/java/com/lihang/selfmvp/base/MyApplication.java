package com.lihang.selfmvp.base;

import android.app.Application;
import android.content.Context;


/**
 * Created by leo
 * on 2019/7/19.
 * 主要是针对自己封装OkHttp
 */
public class MyApplication extends Application {
    private static MyApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }

}
