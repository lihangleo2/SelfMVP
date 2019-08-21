package com.lihang.selfmvp.ui.uploadfragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lihang.selfmvp.R;
import com.lihang.selfmvp.base.BaseFragment;
import com.lihang.selfmvp.utils.LogUtils;
import com.lihang.selfmvp.utils.ToastUtils;
import com.lihang.selfmvp.utils.permissions.ModelPermissionImpl;
import com.lihang.selfmvp.utils.permissions.PermissionListener;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by leo
 * on 2019/8/20.
 */
public class UploadFragment extends BaseFragment<UploadPresenter> implements UploadContract.View, PermissionListener {

    @BindView(R.id.img_)
    ImageView img_;
    @BindView(R.id.txt_upload)
    TextView txt_upload;
    @BindView(R.id.txt_content)
    TextView txt_content;
    private ArrayList<ImageItem> images = null;

    public static Fragment newFragment() {
        return new UploadFragment();
    }

    @Override
    public UploadPresenter cretaePresenter() {
        return new UploadPresenter();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_upload;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }


    @OnClick(R.id.txt_select)
    public void selectClick() {
        ModelPermissionImpl.newPermission().requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnClick(R.id.txt_upload)
    public void uploadPicClick() {
        File file = new File(images.get(0).path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody describe = RequestBody.create(MediaType.parse("multipart/form-data"), "1");

//        mPresenter.uploadPic(describe, body);
//        mPresenter.uploadPic(describe,file);
        ArrayList<File> files = new ArrayList<>();
        files.add(new File(images.get(0).path));
        files.add(new File(images.get(0).path));
        files.add(new File(images.get(0).path));
        files.add(new File(images.get(0).path));
        files.add(new File(images.get(0).path));
        files.add(new File(images.get(0).path));
        mPresenter.uploadPic(describe, files, "file");
    }


    @Override
    public void showUploadData(String data) {
        txt_content.setText("上传图片请求成功 ==> " + data);
    }

    @Override
    public void showUploadError(String error) {
        ToastUtils.showToast(error);
    }

    @Override
    public void permissionSuccess(int command) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setStyle(CropImageView.Style.CIRCLE);
        imagePicker.setMultiMode(false);
        Intent intent = new Intent(getActivity(), ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
        startActivityForResult(intent, 100);
    }

    @Override
    public void permissionFail(int command) {
        ToastUtils.showToast("请打开权限");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images.size() > 0) {
                txt_upload.setEnabled(true);
                Glide.with(UploadFragment.this).load(new File(images.get(0).path)).into(img_);
            }
        }
    }
}
