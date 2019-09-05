package com.lihang.selfmvp.daggerforandroid;


import com.lihang.selfmvp.ui.downfragment.DownFragment;
import com.lihang.selfmvp.ui.getfragment.GETFragment;
import com.lihang.selfmvp.ui.getfragment.GetPresenter;
import com.lihang.selfmvp.ui.postfragment.POSTFragment;
import com.lihang.selfmvp.ui.uploadfragment.UploadFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by leo
 * on 2019/9/4.
 */
@Module
public abstract class NeedInjectModules {
    @ContributesAndroidInjector
    abstract POSTFragment injectPOSTFragment();
    @ContributesAndroidInjector
    abstract UploadFragment injectUploadFragment();
    @ContributesAndroidInjector
    abstract DownFragment injectDownFragment();
}
