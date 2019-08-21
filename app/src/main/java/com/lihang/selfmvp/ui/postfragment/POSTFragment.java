package com.lihang.selfmvp.ui.postfragment;

import android.os.Bundle;
import android.widget.TextView;

import com.lihang.selfmvp.R;
import com.lihang.selfmvp.base.BaseFragment;
import com.lihang.selfmvp.common.PARAMS;
import com.lihang.selfmvp.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by leo
 * on 2019/8/19.
 */
public class POSTFragment extends BaseFragment<PostPresenter> implements PostContract.View {
    @BindView(R.id.txt_content)
    TextView txt_content;


    public static POSTFragment newFragment() {
        POSTFragment fragment = new POSTFragment();
        return fragment;
    }

    @Override
    public PostPresenter cretaePresenter() {
        return new PostPresenter();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_post;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @OnClick(R.id.txt_post)
    public void postClick() {
        mPresenter.getPostDataWithRetryTime(PARAMS.gankPost("https://github.com/lihangleo2/ShadowLayout",
                "阴影布局，不管你是什么控件，放进阴影布局即刻享受你想要的阴影", "110", "Android", "true"), 3);
    }


    @Override
    public void showPostData(String data) {
        txt_content.setText(data);
    }

    @Override
    public void showPostError(String msg) {
        ToastUtils.showToast(msg);
    }
}
