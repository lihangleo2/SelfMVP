package com.lihang.selfmvp.ui.postfragment;


import com.lihang.selfmvp.base.BaseView;

import java.util.HashMap;

/**
 * Created by leo
 * on 2019/8/16.
 */

public interface PostContract {

    //view只有2个更新ui的方法
    interface View extends BaseView {
        // 1、获取post数据，更新ui
        void showPostData(String data);

        // 2、获取post数据失败，更新ui
        void showPostError(String msg);
    }

    //post的prensenter只有一个获取post数据的数据请求
    interface Prensenter {
        // 1、开启post网络请求
        public void getPostData(HashMap<String, String> postParm);
        // 2、开启post请求，带失败重连
        public void getPostDataWithRetryTime(HashMap<String, String> postParm,int retryMaxCount);
    }
}
