package com.lihang.selfmvp.ui.getfragment;

import android.os.Bundle;
import android.widget.TextView;
import com.lihang.selfmvp.R;
import com.lihang.selfmvp.base.BaseFragment;
import com.lihang.selfmvp.utils.ToastUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by leo
 * on 2019/8/19.
 */
public class GETFragment extends BaseFragment<GetPresenter> implements GetContract.View {
    @BindView(R.id.txt_content)
    TextView txt_content;

    public static GETFragment newFragment() {
        GETFragment fragment = new GETFragment();
        return fragment;
    }

    @Override
    public GetPresenter cretaePresenter() {

        return new GetPresenter();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_get;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @OnClick(R.id.txt_get)
    public void getClick() {
        mPresenter.getGetData("Android");
    }

    @Override
    public void showGetData(String data) {
        txt_content.setText(data);
    }

    @Override
    public void showGetError(String msg) {
        ToastUtils.showToast(msg);
    }
}
