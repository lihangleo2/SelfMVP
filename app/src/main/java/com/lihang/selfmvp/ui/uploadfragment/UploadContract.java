package com.lihang.selfmvp.ui.uploadfragment;

import com.lihang.selfmvp.base.BaseView;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;

/**
 * Created by leo
 * on 2019/8/20.
 */
public interface UploadContract {
    interface View extends BaseView {
        void showUploadData(String data);

        void showUploadError(String error);
    }

    interface Presenter {
        //这里是上传图文，一个图文和一个文件
        void uploadPic(RequestBody sequence, MultipartBody.Part file);
        void uploadPic(RequestBody sequence, File file);
        void uploadPic(RequestBody sequence, ArrayList<File> files,String key);
    }
}
