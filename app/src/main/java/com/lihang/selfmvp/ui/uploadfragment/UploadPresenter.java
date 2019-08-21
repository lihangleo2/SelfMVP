package com.lihang.selfmvp.ui.uploadfragment;

import com.lihang.selfmvp.base.BasePresenter;
import com.lihang.selfmvp.common.SystemConst;
import com.lihang.selfmvp.cutomview.LoadingDialog;
import com.lihang.selfmvp.retrofitwithrxjava.uploadutils.FileUploadObserver;
import com.lihang.selfmvp.utils.LogUtils;
import java.io.File;
import java.util.ArrayList;
import io.reactivex.functions.Consumer;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by leo
 * on 2019/8/20.
 */
public class UploadPresenter extends BasePresenter<UploadContract.View> implements UploadContract.Presenter {
    @Override
    public void uploadPic(RequestBody sequence, MultipartBody.Part file) {
        if (!isViewAttached()) {
            return;
        }
        /**
         * 这里是正常上传图片，不监听进度
         * */
        observe(apiService().uploadPic(SystemConst.DIFFERT_URL, sequence, file))
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        getView().showUploadData(responseBody.string());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().showUploadError(throwable.toString());
                    }
                });
    }

    /**
     * 这里是监听单张进度
     */
    @Override
    public void uploadPic(RequestBody sequence, File file) {
        //上传2遍是因为日志拦截器又走了一遍，注意注释
        uploadWithListener(SystemConst.DIFFERT_URL, sequence, file, new FileUploadObserver<ResponseBody>() {
            @Override
            public void onUpLoadSuccess(ResponseBody responseBody) {
                LogUtils.i("检查进度", "成功了");
            }

            @Override
            public void onUpLoadFail(Throwable e) {
                LogUtils.i("检查进度", e.toString());
            }

            @Override
            public void onProgress(int progress) {
                LoadingDialog.getInstance().setProgress("已上传进度" + progress + "%");
                LogUtils.i("检查进度", progress + "");
            }
        });

    }


    /**
     * 这里是监听多张图片上传进度
     */
    @Override
    public void uploadPic(RequestBody sequence, ArrayList<File> files, String key) {
        //上传2遍是因为日志拦截器又走了一遍，注意注释
        uploadWithListener(SystemConst.DIFFERT_URL, sequence, files, key, new FileUploadObserver<ResponseBody>() {
            @Override
            public void onUpLoadSuccess(ResponseBody responseBody) {
                LogUtils.i("检查进度", "成功了");
            }

            @Override
            public void onUpLoadFail(Throwable e) {
                LogUtils.i("检查进度", e.toString());
            }

            @Override
            public void onProgress(int progress) {
                LoadingDialog.getInstance().setProgress("已上传进度" + progress + "%");
                LogUtils.i("检查进度", progress + "");
            }
        });
    }


}
// 1、图文上传
//@POST("url")
//@Multipart
//Observable<ResponseBody> uploadPic(@Part("sequence") RequestBody sequence, @Part MultipartBody.Part file);

//参数就是这样，
//RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
//RequestBody describe = RequestBody.create(MediaType.parse("multipart/form-data"), "1");

//2、图文上传表单  多图文，多图片上传
//@POST("url")
//@Multipart
//Observable<ResponseBody> uploadPicLeo(@FieldMap Map<String , String> usermaps, @PartMap Map<String, RequestBody> maps);

//参数的话知道retrofit都知道，后面的map是就是多对应不同图片对应不同key
//RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

//结语：这个参数写的太多了，我们可以把它封装起来.具体封装可以看common里的PARAMS

