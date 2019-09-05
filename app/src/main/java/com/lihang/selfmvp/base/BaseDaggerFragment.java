package com.lihang.selfmvp.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lihang.selfmvp.cutomview.LoadingDialog;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import dagger.android.support.AndroidSupportInjection;

/**
 * Created by leo
 * on 2019/8/19.
 */
public abstract class BaseDaggerFragment<T extends BasePresenter> extends RxFragment implements BaseView, HasAndroidInjector {
    @Inject
    public T mPresenter;


    //获取当前fragment布局文件
    public abstract int getContentViewId();

    //设置监听事件
    protected abstract void setListener();

    //处理逻辑业务
    protected abstract void processLogic(Bundle savedInstanceState);

    protected View mContentView;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 避免多次从xml中加载布局文件
        if (mContentView == null) {
            setContentView(getContentViewId());
            setListener();
            processLogic(savedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (parent != null) {
                parent.removeView(mContentView);
            }
        }
        return mContentView;
    }

    protected void setContentView(@LayoutRes int layoutResID) {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        mContentView = LayoutInflater.from(getActivity()).inflate(layoutResID, null);
        mUnbinder = ButterKnife.bind(this, mContentView);
    }


    //简单跳转
    public void transfer(Class<?> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }


    //快速获取textView 或 EditText上文字内容
    public String getStringByUI(View view) {
        if (view instanceof EditText) {
            return ((EditText) view).getText().toString().trim();
        } else if (view instanceof TextView) {
            return ((TextView) view).getText().toString().trim();
        }
        return "";
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    @Override
    public void showLoading(String message) {
        LoadingDialog.getInstance().show(getActivity(), message);
    }

    @Override
    public void hideLoading() {
        LoadingDialog.getInstance().dismiss();
    }

    //防止Rx内存泄漏
    @Override
    public LifecycleTransformer bindLifecycle() {
        LifecycleTransformer objectLifecycleTransformer = bindToLifecycle();
        return objectLifecycleTransformer;
    }

    //添加Dagger2代码


    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Inject
    DispatchingAndroidInjector<Object> androidInjector;
    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }
}
