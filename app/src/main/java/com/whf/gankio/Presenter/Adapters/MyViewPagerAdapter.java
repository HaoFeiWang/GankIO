package com.whf.gankio.Presenter.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.whf.gankio.Model.Constant;

import java.util.List;

/**
 * Created by WHF on 2016/7/18.
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public MyViewPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Constant.STUDY_TAB[position];
    }
}
