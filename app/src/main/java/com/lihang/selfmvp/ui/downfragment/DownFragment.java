package com.lihang.selfmvp.ui.downfragment;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lihang.selfmvp.R;
import com.lihang.selfmvp.base.BaseFragment;
import com.lihang.selfmvp.utils.MathUtils;
import com.lihang.selfmvp.utils.ToastUtils;
import com.lihang.selfmvp.utils.permissions.ModelPermissionImpl;
import com.lihang.selfmvp.utils.permissions.PermissionListener;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by leo
 * on 2019/8/21.
 */
public class DownFragment extends BaseFragment<DownPresenter> implements DownContract.View, PermissionListener {
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ok_down/demo/";
    private String fileName = "easyOk.apk";
    @BindView(R.id.txt_down)
    TextView txt_down;//正常下载(重新下载)
    @BindView(R.id.txt_resumdown)
    TextView txt_resumdown;//断点下载
    @BindView(R.id.txt_cancledown)
    TextView txt_cancledown;//取消下载
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.txt_current)
    TextView txt_current;
    @BindView(R.id.txt_total)
    TextView txt_total;

    public static DownFragment newFragment() {
        DownFragment fragment = new DownFragment();
        return fragment;
    }

    @Override
    public DownPresenter cretaePresenter() {
        return new DownPresenter();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_downfile;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    final int NORMAL_DOWN=99;
    final int RESUME_DOWN = 100;
    @OnClick({R.id.txt_down, R.id.txt_resumdown, R.id.txt_cancledown})
    public void fileAboutClick(View view) {
        switch (view.getId()) {
            case R.id.txt_down:
                ModelPermissionImpl.newPermission().requestPermission(NORMAL_DOWN,this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                break;

            case R.id.txt_resumdown:
                ModelPermissionImpl.newPermission().requestPermission(RESUME_DOWN,this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                break;

            case R.id.txt_cancledown:
                setCancle(false);
                mPresenter.pauseDown();
                break;
        }
    }


    @Override
    public void showDownSuccess(File file) {
        setCancle(false);
    }

    @Override
    public void showDownFail(String errorMsg) {
        ToastUtils.showToast(errorMsg);
    }

    @Override
    public void showDownProgress(int progress, long total) {
        progressBar.setProgress(progress);
        txt_current.setText(progress + "%");
        txt_total.setText(MathUtils.round((double) total / 1024 / 1024, 2) + "M");
    }

    public void setCancle(boolean cancle) {
        txt_down.setEnabled(!cancle);
        txt_resumdown.setEnabled(!cancle);
        txt_cancledown.setEnabled(cancle);
    }

    @Override
    public void permissionSuccess(int command) {
        switch (command){
            case NORMAL_DOWN:
                setCancle(true);
                mPresenter.downFile(path, fileName);
                break;

            case RESUME_DOWN:
                setCancle(true);
                //Retrofit下载文件只能把进度存起来，
                // 100%的时候再点断点下载，最好判断下，不然因为它封装的问题会直接报错HTTP 416 Request Range Not Satisfiable
                //而且这个错误捕捉不了，因为他封装在RxJava2 + Retrofit2里
                mPresenter.downResumeFile(path, fileName);
                break;
        }
    }

    @Override
    public void permissionFail(int command) {
       ToastUtils.showToast("下载文件需要此权限！！");
    }
}
