package com.lihang.selfmvp.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lihang.selfmvp.cutomview.LoadingDialog;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by leo
 * on 2019/8/16.
 */
public abstract class BaseActivity<T extends BasePresenter> extends RxFragmentActivity implements BaseView {

    public T mPresenter;

    public abstract T cretaePresenter();

    //获取当前activity布局文件
    public abstract int getContentViewId();

    //设置监听事件
    public abstract void setListener();

    //处理逻辑业务
    protected abstract void processLogic();

    private Unbinder mUnbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mUnbinder = ButterKnife.bind(this);
        mPresenter = cretaePresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        setListener();
        processLogic();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        mUnbinder.unbind();
    }

    @Override
    public void showLoading(String message) {
        LoadingDialog.getInstance().show(BaseActivity.this, message);
    }

    @Override
    public void hideLoading() {
        LoadingDialog.getInstance().dismiss();
    }


    public void transfer(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
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
    public LifecycleTransformer bindLifecycle() {
        LifecycleTransformer objectLifecycleTransformer = bindToLifecycle();
        return objectLifecycleTransformer;
    }
}
