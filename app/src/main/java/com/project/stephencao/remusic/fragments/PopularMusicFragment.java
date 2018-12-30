package com.project.stephencao.remusic.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.project.stephencao.remusic.R;
import com.project.stephencao.remusic.adapter.MyMainPageViewPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class PopularMusicFragment extends Fragment implements NewSongFragment.OnJumpToMoreListener {
    private static final String[] TITLES = new String[]{"新曲", "歌单", "排行榜"};
    private List<Fragment> mFragmentList = new ArrayList<>();
    public static final String BUNDLE_TITLE = "title";
    private View mView;
    private ViewPager mPager;

    public static PopularMusicFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        PopularMusicFragment fragment = new PopularMusicFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
            return mView;
        }
        mView = LayoutInflater.from(getContext()).inflate(R.layout.view_pop_music_fragment, container, false);
        initView();
        return mView;
    }

    private void initView() {
        mFragmentList.add(NewSongFragment.newInstance(TITLES[0]));
        mFragmentList.add(SongListFragment.newInstance(TITLES[1]));
        mFragmentList.add(RankingFragment.newInstance(TITLES[2]));
        NewSongFragment newSongFragment = (NewSongFragment) mFragmentList.get(0);
        newSongFragment.setOnJumpToMoreListener(this);
        FragmentPagerAdapter adapter = new MyMainPageViewPagerAdapter(getFragmentManager(), mFragmentList, TITLES);
        mPager = mView.findViewById(R.id.vp_pop_music_view_pager);
        mPager.setAdapter(adapter);

        TabPageIndicator indicator = mView.findViewById(R.id.ti_pop_music_indicator);
        indicator.setViewPager(mPager);
    }

    @Override
    public void doJump() {
        if(mPager!=null){
            mPager.setCurrentItem(1);
        }
    }
}
