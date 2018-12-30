package com.project.stephencao.remusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import com.project.stephencao.remusic.R;
import com.project.stephencao.remusic.adapter.MyMainPageViewPagerAdapter;
import com.project.stephencao.remusic.fragments.*;
import com.project.stephencao.remusic.utils.ConstantValues;
import com.project.stephencao.remusic.utils.LocalMusicUtil;
import com.project.stephencao.remusic.utils.SharedPreferencesUtil;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class LocalMusicDisplayActivity extends FragmentActivity implements View.OnClickListener {

    private static final String[] TITLES = new String[]{"单曲", "歌手", "专辑","文件夹"};
    private ImageButton mBackIbtn, mMoreIbtn, mSearchIbtn;
    private TabPageIndicator mTabPageIndicator;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private ViewPager mViewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
        initView();
        initMusicImages();
    }

    private void initMusicImages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isFirstTime = SharedPreferencesUtil.getBoolean(LocalMusicDisplayActivity.this,
                        ConstantValues.FIRST_TIME_STORE_PICTURES, true);
                if(isFirstTime){
                    LocalMusicUtil.initSongPictures(LocalMusicDisplayActivity.this, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                    SharedPreferencesUtil.putBoolean(LocalMusicDisplayActivity.this, ConstantValues.FIRST_TIME_STORE_PICTURES, false);
                }
            }
        }).start();
    }

    private void initView() {
        mBackIbtn = findViewById(R.id.ibtn_local_music_title_bar_back);
        mMoreIbtn = findViewById(R.id.ibtn_local_music_title_bar_selection);
        mSearchIbtn = findViewById(R.id.ibtn_local_music_title_bar_search);
        mBackIbtn.setOnClickListener(this);
        mMoreIbtn.setOnClickListener(this);
        mSearchIbtn.setOnClickListener(this);
        initAdapter();
        mTabPageIndicator = findViewById(R.id.ti_local_music_display_indicator);
        mViewPager = findViewById(R.id.vp_local_music_display_pager);
        mViewPager.setAdapter(mAdapter);
        mTabPageIndicator.setViewPager(mViewPager);
        Intent intent = getIntent();
        boolean isJumpToSinger = intent.getBooleanExtra(ConstantValues.IS_JUMP_TO_SINGER_PAGE, false);
        if(isJumpToSinger){
            mTabPageIndicator.setCurrentItem(1);
        }


    }

    private void initAdapter() {
        mFragmentList.add(SingleSongFragment.newInstance(TITLES[0]));
        mFragmentList.add(SingerFragment.newInstance(TITLES[1]));
        mFragmentList.add(AlbumFragment.newInstance(TITLES[2]));
        mFragmentList.add(MusicFileFragment.newInstance(TITLES[3]));
        mAdapter = new MyMainPageViewPagerAdapter(getSupportFragmentManager(), mFragmentList, TITLES);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtn_local_music_title_bar_back:{
                finish();
                break;
            }
            case R.id.ibtn_local_music_title_bar_selection:{
                break;
            }
            case R.id.ibtn_local_music_title_bar_search:{
                break;
            }

        }
    }


}
