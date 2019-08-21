package com.lihang.selfmvp.utils.permissions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * Created by leo
 * on 2019/8/2.
 */
public class ModelPermissionImpl {

    public static ModelPermissionImpl newPermission() {
        return new ModelPermissionImpl();
    }

    //有可能一个页面进行多个网络请求，加上一个指令type区分
    public void requestPermission(final int permissionCommand, final FragmentActivity activity, final PermissionListener permissionListener, final String... permission) {
        //如果权限都被申请了，那么不再继续
        if (checkPermission(activity, permission)) {
            permissionListener.permissionSuccess(permissionCommand);
            return;
        }

        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(permission).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    //有些手机保持禁止以后会出现aBoolean为true的情况，则加上下一句
                    if (!checkPermission(activity, permission)) {
                        permissionListener.permissionFail(permissionCommand);
                        return;
                    }
                    permissionListener.permissionSuccess(permissionCommand);
                } else {
                    permissionListener.permissionFail(permissionCommand);
                }
            }
        });
    }


    public boolean checkPermission(Context context, String... permission) {
        for (int i = 0; i < permission.length; i++) {
            if (ActivityCompat.checkSelfPermission(context, permission[i]) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    public void requestPermission(final int permissionCommand, final Fragment fragment, final PermissionListener permissionListener, final String... permission) {
        //如果权限都被申请了，那么不再继续
        if (checkPermission(fragment.getActivity(), permission)) {
            permissionListener.permissionSuccess(permissionCommand);
            return;
        }

        RxPermissions rxPermissions = new RxPermissions(fragment);
        rxPermissions.request(permission).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    //有些手机保持禁止以后会出现aBoolean为true的情况，则加上下一句
                    if (!checkPermission(fragment.getActivity(), permission)) {
                        permissionListener.permissionFail(permissionCommand);
                        return;
                    }
                    permissionListener.permissionSuccess(permissionCommand);
                } else {
                    permissionListener.permissionFail(permissionCommand);
                }
            }
        });
    }


    //如果页面只有一个网络请求，那么方法可以不带，默认一个999
    public void requestPermission(Fragment fragment, PermissionListener permissionListener, String... permission) {
        requestPermission(999, fragment, permissionListener, permission);
    }

    public void requestPermission(FragmentActivity activity, PermissionListener permissionListener, String... permission) {
        requestPermission(999, activity, permissionListener, permission);
    }

    //如果是activity或fragment时 不需要带入实例，listener就是实例
    public void requestPermission(PermissionListener permissionListener, String... permission) {
        if (permissionListener instanceof FragmentActivity) {
            requestPermission(999, (FragmentActivity) permissionListener, permissionListener, permission);
        } else if (permissionListener instanceof Fragment) {
            requestPermission(999, (Fragment) permissionListener, permissionListener, permission);
        } else {
            throw new NullPointerException("Activity or Fragment can not be null");
        }
    }


    //如果是activity或fragment时 不需要带入实例，listener就是实例
    public void requestPermission(int command, PermissionListener permissionListener, String... permission) {
        if (permissionListener instanceof FragmentActivity) {
            requestPermission(command, (FragmentActivity) permissionListener, permissionListener, permission);
        } else if (permissionListener instanceof Fragment) {
            requestPermission(command, (Fragment) permissionListener, permissionListener, permission);
        } else {
            throw new NullPointerException("Activity or Fragment can not be null");
        }
    }

}
