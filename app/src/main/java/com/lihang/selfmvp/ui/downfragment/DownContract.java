package com.lihang.selfmvp.ui.downfragment;

import com.lihang.selfmvp.base.BaseView;

import java.io.File;

/**
 * Created by leo
 * on 2019/8/21.
 */
public interface DownContract {
    interface View extends BaseView {
        void showDownSuccess(File file);

        void showDownFail(String errorMsg);

        void showDownProgress(int current, long total);
    }

    interface Presenter {
        //正常下载
        void downFile(String destDir, String fileName);

        //断点下载
        void downResumeFile(String destDir,String fileName);

        //暂停下载
        void pauseDown();
    }
}
