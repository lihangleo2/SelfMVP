package com.lihang.selfmvp.cutomview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.DrmInitData;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lihang.selfmvp.R;

/**
 * Created by leo
 * on 2019/7/23.
 * dialog要在activity context上，mainActivity上初始化
 */
public class LoadingDialog {

    public static LoadingDialog loadingDialog;
    private Dialog dialog;
    private TextView txt_message;
    private ImageView imageView;
    private AnimationDrawable spinner;
    private View view;

    private LoadingDialog() {

    }


    public static LoadingDialog getInstance() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog();
        }
        return loadingDialog;
    }


    public void setCancleable(boolean cancleable) {
        if (dialog != null) {
            dialog.setCancelable(cancleable);
        }
    }


    public void show(Context mContext) {
        show(mContext, "");
    }


    public void show(Context mContext, String message) {
        tagUrl = "";
        if (dialog != null) {
            // 开始动画
            try {
                //loading圈是否要显示文字
                if (TextUtils.isEmpty(message)) {
                    txt_message.setText("");
                    txt_message.setVisibility(View.GONE);
                } else {
                    txt_message.setText(message);
                    txt_message.setVisibility(View.VISIBLE);
                }
                spinner.start();
                dialog.show();
            } catch (Exception e) {
                //说明之前的context消失，提高垃圾回收
                dialog = null;
                txt_message = null;
                imageView = null;
                initDialog(mContext, message);
            }
        } else {
            initDialog(mContext, message);
        }
    }

    private String tagUrl;

    public String getTagUrl() {
        return tagUrl;
    }

    //用于判断是否是图片上传才显示进度
    public void show(Context mContext, String message, String url) {
        this.tagUrl = url;
        if (dialog != null) {
            // 开始动画
            try {
                //loading圈是否要显示文字
                if (TextUtils.isEmpty(message)) {
                    txt_message.setText("");
                    txt_message.setVisibility(View.GONE);
                } else {
                    txt_message.setText(message);
                    txt_message.setVisibility(View.VISIBLE);
                }
                spinner.start();
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            } catch (Exception e) {
                //说明之前的context消失，提高垃圾回收
                dialog = null;
                txt_message = null;
                imageView = null;
                initDialog(mContext, message);
            }
        } else {
            initDialog(mContext, message);
        }
    }


    public void setProgress(String message) {
        if (dialog != null) {
            if (TextUtils.isEmpty(message)) {
                txt_message.setText("");
                txt_message.setVisibility(View.GONE);
            } else {
                txt_message.setText(message);
                txt_message.setVisibility(View.VISIBLE);
            }
        }
    }

    public void dismiss() {
        if (dialog != null) {
            spinner.stop();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    //因为是dialog，所以如果mContext deth产生的bug，将生产新的。
    public void initDialog(Context mContext, String message) {
        dialog = new Dialog(mContext, R.style.Custom_Progress);
        view = LayoutInflater.from(mContext).inflate(
                R.layout.progress_leo, null);
        txt_message = view.findViewById(R.id.txt_message);
        imageView = view.findViewById(R.id.spinnerImageView);

        // 获取ImageView上的动画背景
        spinner = (AnimationDrawable) imageView.getBackground();
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);

        //loading圈是否要显示文字
        if (TextUtils.isEmpty(message)) {
            txt_message.setText("");
            txt_message.setVisibility(View.GONE);
        } else {
            txt_message.setText(message);
            txt_message.setVisibility(View.VISIBLE);
        }

        spinner.start();
        if (!dialog.isShowing()) {
            dialog.show();
        }

    }

}
