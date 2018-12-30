package com.project.stephencao.remusic.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MyMainPageViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private String[] mTitles;

    public MyMainPageViewPagerAdapter(FragmentManager supportFragmentManager, List<Fragment> list, String[] titles) {
        super(supportFragmentManager);
        fragmentList = list;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
