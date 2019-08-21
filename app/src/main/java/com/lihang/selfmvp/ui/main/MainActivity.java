package com.lihang.selfmvp.ui.main;


import android.Manifest;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.gyf.barlibrary.ImmersionBar;
import com.lihang.selfmvp.R;
import com.lihang.selfmvp.base.BaseActivity;
import com.lihang.selfmvp.base.BasePresenter;
import com.lihang.selfmvp.utils.ToastUtils;
import com.lihang.selfmvp.utils.networks.NetStateChangeObserver;
import com.lihang.selfmvp.utils.networks.NetStateChangeReceiver;
import com.lihang.selfmvp.utils.networks.NetworkType;
import com.lihang.selfmvp.utils.permissions.ModelPermissionImpl;
import com.lihang.selfmvp.utils.permissions.PermissionListener;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;

import java.util.Observable;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


public class MainActivity extends BaseActivity implements NetStateChangeObserver, PermissionListener {
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    String[] titles = {"GET请求", "下载文件", "POST请求", "上传文件"};
    private CommonNavigator commonNavigator;
    //沉浸式状态栏
    protected ImmersionBar mImmersionBar;
    private ViewPagerFragmentMoreAdapter adapter;

    @Override
    public BasePresenter cretaePresenter() {
        return null;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void setListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //绑定观察者
        NetStateChangeReceiver.registerObserver(this);
    }

    @Override
    protected void processLogic() {
        //注册广播
        NetStateChangeReceiver.registerReceiver(this);
        mImmersionBar = ImmersionBar.with(this)
                .statusBarColor(R.color.shape2)
                //布局是否在状态栏下
                .fitsSystemWindows(true);
        mImmersionBar.init();
        //主动设置viewPager缓存
//        viewPager.setOffscreenPageLimit(6);
        adapter = new ViewPagerFragmentMoreAdapter(getSupportFragmentManager(), titles);
        viewPager.setAdapter(adapter);
        initMagicIndicator();
        io.reactivex.Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        ModelPermissionImpl.newPermission().requestPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //解绑观察者
        NetStateChangeReceiver.unRegisterObserver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注释广播
        NetStateChangeReceiver.unRegisterReceiver(this);
    }

    private void initMagicIndicator() {
        magicIndicator.setBackgroundColor(getResources().getColor(R.color.shape2));
        commonNavigator = new CommonNavigator(MainActivity.this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles == null ? 0 : titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                final BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);

                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(titles[index]);
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.unselect));
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.white));
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });

                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);
                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 3));
                indicator.setLineWidth(UIUtil.dip2px(context, 15));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(getResources().getColor(R.color.pink));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);

    }

    @Override
    public void onNetDisconnected() {
        //断开网络连接回调
        ToastUtils.showToast("网络断开!");
    }

    @Override
    public void onNetConnected(NetworkType networkType) {
        //连接网络回调
        ToastUtils.showToast("网络已连接" + networkType.toString());
    }

    @Override
    public void permissionSuccess(int command) {
        ToastUtils.showToast("权限申请成功！！");
    }

    @Override
    public void permissionFail(int command) {
        ToastUtils.showToast("请打开权限，保证正常运行！！！");
    }
}
