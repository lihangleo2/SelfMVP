package com.lihang.selfmvp.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lihang.selfmvp.ui.downfragment.DownFragment;
import com.lihang.selfmvp.ui.getfragment.GETFragment;
import com.lihang.selfmvp.ui.postfragment.POSTFragment;
import com.lihang.selfmvp.ui.uploadfragment.UploadFragment;


/**
 * Created by Administrator on 2018/1/19.
 * 这是多fragment的Adapter
 */

public class ViewPagerFragmentMoreAdapter extends FragmentStatePagerAdapter {

    private String[] arr;

    public ViewPagerFragmentMoreAdapter(FragmentManager fm, String[] arr) {
        super(fm);
        this.arr = arr;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return GETFragment.newFragment();
        } else if (position == 1) {
            return DownFragment.newFragment();
        } else if (position == 2) {
            return POSTFragment.newFragment();
        } else {
            return UploadFragment.newFragment();
        }
    }

    @Override
    public int getCount() {
        return arr != null ? arr.length : 0;
    }
}
