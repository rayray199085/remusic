package com.project.stephencao.remusic.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.project.stephencao.remusic.R;
import com.project.stephencao.remusic.adapter.MyMainPageViewPagerAdapter;
import com.project.stephencao.remusic.adapter.MySlidingMenuListAdapter;
import com.project.stephencao.remusic.bean.SlidingMenuItem;
import com.project.stephencao.remusic.fragments.AccountFragment;
import com.project.stephencao.remusic.fragments.LocalMusicFragment;
import com.project.stephencao.remusic.fragments.PopularMusicFragment;
import com.project.stephencao.remusic.utils.ConstantValues;
import com.project.stephencao.remusic.utils.SharedPreferencesUtil;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private static final String[] TITLES = new String[]{"流行", "本地", "账号"};
    private List<Fragment> mFragmentList = new ArrayList<>();
    private DrawerLayout mDrawLayout;
    private String[] mSlidingMenuDescriptions = new String[]{"夜间模式", "主题换肤", "定时关闭音乐", "下载歌曲品质", "退出"};
    private int[] mSlidingMenuIcons = new int[]{R.drawable.night, R.drawable.styles,
            R.drawable.timer, R.drawable.quality, R.drawable.off};
    private ListView mSlidingMenuListView;
    private boolean mShouldQuit = true;
    private int mSongQuality;
    private int mTimerCount;
    private int mThemes;
    private List<SlidingMenuItem> mSlidingMenuItemList;
    private ImageButton mCloseDrawerIbtn, mOpenDrawerIbtn, mSearchIbtn;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantValues.RESET_TURN_OFF_STATUS: {
                    mShouldQuit = true;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPager();
        initSlidingMenu();
    }

    private void initSlidingMenu() {
        mSongQuality = SharedPreferencesUtil.getInteger(this, ConstantValues.DOWNLOAD_SONG_QUALITY);
        mSlidingMenuItemList = new ArrayList<>();
        for (int i = 0; i < mSlidingMenuDescriptions.length; i++) {
            SlidingMenuItem item = new SlidingMenuItem();
            item.itemDescription = mSlidingMenuDescriptions[i];
            item.drawableID = mSlidingMenuIcons[i];
            mSlidingMenuItemList.add(item);
        }
        MySlidingMenuListAdapter mySlidingMenuListAdapter = new MySlidingMenuListAdapter(this, mSlidingMenuItemList);
        mSlidingMenuListView.setAdapter(mySlidingMenuListAdapter);
        mSlidingMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case ConstantValues.NIGHT_SHIFT: {
                        break;
                    }
                    case ConstantValues.THEMES: {
                        chooseThemes();
                        break;
                    }
                    case ConstantValues.TIMER_TURN_OFF_MUSIC: {
                        setUpTimerOffMusic();
                        break;
                    }
                    case ConstantValues.SONG_QUALITY: {
                        chooseSongQuality();
                        break;
                    }
                    case ConstantValues.QUIT: {
                        finish();
                        break;
                    }
                }
            }
        });
    }

    private void chooseThemes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View view = LayoutInflater.from(this).inflate(R.layout.view_themes_option, null);
        alertDialog.setView(view);
        RadioGroup radioGroupA = view.findViewById(R.id.rg_themes_radio_group_A);
        RadioGroup radioGroupB = view.findViewById(R.id.rg_themes_radio_group_B);
        int selectID = SharedPreferencesUtil.getInteger(this, ConstantValues.THEMES_SELECTION);
        if (selectID != 0) {
            if (selectID == R.id.rbtn_themes_red_color || selectID == R.id.rbtn_themes_sky_blue_color ||
                    selectID == R.id.rbtn_themes_purple_color || selectID == R.id.rbtn_themes_dark_green_color) {
                radioGroupA.check(selectID);
            } else {
                radioGroupB.check(selectID);
            }
        }
        radioGroupA.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_themes_red_color: {
                        mThemes = 0;
                        break;
                    }
                    case R.id.rbtn_themes_purple_color: {
                        mThemes = 1;
                        break;
                    }
                    case R.id.rbtn_themes_sky_blue_color: {
                        mThemes = 2;
                        break;
                    }
                    case R.id.rbtn_themes_dark_green_color: {
                        mThemes = 3;
                        break;
                    }

                }
                SharedPreferencesUtil.putInteger(MainActivity.this, ConstantValues.THEMES_SELECTION, checkedId);
                alertDialog.dismiss();
            }
        });
        radioGroupB.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_themes_green_color: {
                        mThemes = 4;
                        break;
                    }
                    case R.id.rbtn_themes_yellow_color: {
                        mThemes = 5;
                    }
                    case R.id.rbtn_themes_orange_color: {
                        mThemes = 6;
                        break;
                    }
                    case R.id.rbtn_themes_pink_color: {
                        mThemes = 7;
                        break;
                    }
                }
                SharedPreferencesUtil.putInteger(MainActivity.this, ConstantValues.THEMES_SELECTION, checkedId);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void setUpTimerOffMusic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View view = LayoutInflater.from(this).inflate(R.layout.view_timer_off_option, null);
        alertDialog.setView(view);
        RadioGroup radioGroup = view.findViewById(R.id.rg_timer_off_music_group);
        int selectID = SharedPreferencesUtil.getInteger(this, ConstantValues.TIMER_OFF_MUSIC);
        if (selectID != 0) {
            radioGroup.check(selectID);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_timer_off_music_10: {
                        mTimerCount = 10;
                        break;
                    }
                    case R.id.rbtn_timer_off_music_20: {
                        mTimerCount = 20;
                        break;
                    }
                    case R.id.rbtn_timer_off_music_30: {
                        mTimerCount = 30;
                        break;
                    }
                    case R.id.rbtn_timer_off_music_45: {
                        mTimerCount = 45;
                        break;
                    }
                    case R.id.rbtn_timer_off_music_60: {
                        mTimerCount = 60;
                        break;
                    }
                    case R.id.rbtn_timer_off_music_90: {
                        mTimerCount = 90;
                        break;
                    }
                }
                SharedPreferencesUtil.putInteger(MainActivity.this, ConstantValues.TIMER_OFF_MUSIC, checkedId);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void chooseSongQuality() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View view = LayoutInflater.from(this).inflate(R.layout.view_song_quality_option, null);
        alertDialog.setView(view);
        RadioGroup radioGroup = view.findViewById(R.id.rg_song_quality_group);
        int selectID = SharedPreferencesUtil.getInteger(this, ConstantValues.DOWNLOAD_SONG_QUALITY);
        if (selectID != 0) {
            radioGroup.check(selectID);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_song_quality_64: {
                        mSongQuality = 64;
                        break;
                    }
                    case R.id.rbtn_song_quality_128: {
                        mSongQuality = 128;
                        break;
                    }
                    case R.id.rbtn_song_quality_192: {
                        mSongQuality = 192;
                        break;
                    }
                    case R.id.rbtn_song_quality_256: {
                        mSongQuality = 256;
                        break;
                    }
                    case R.id.rbtn_song_quality_320: {
                        mSongQuality = 320;
                        break;
                    }
                }
                SharedPreferencesUtil.putInteger(MainActivity.this, ConstantValues.DOWNLOAD_SONG_QUALITY, checkedId);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void initPager() {
        mFragmentList.add(PopularMusicFragment.newInstance(TITLES[0]));
        mFragmentList.add(LocalMusicFragment.newInstance(TITLES[1]));
        mFragmentList.add(AccountFragment.newInstance(TITLES[2]));
        FragmentPagerAdapter adapter = new MyMainPageViewPagerAdapter(getSupportFragmentManager(), mFragmentList, TITLES);

        ViewPager pager = findViewById(R.id.vp_main_page_view_pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = findViewById(R.id.ti_main_page_indicator);
        indicator.setViewPager(pager);
        indicator.setCurrentItem(1);
    }

    private void initView() {
        mDrawLayout = findViewById(R.id.dl_main_activity_drawer);
        mCloseDrawerIbtn = findViewById(R.id.iv_sliding_menu_close);
        mCloseDrawerIbtn.setOnClickListener(this);
        mOpenDrawerIbtn = findViewById(R.id.ibtn_main_page_open_drawer);
        mOpenDrawerIbtn.setOnClickListener(this);
        mSearchIbtn = findViewById(R.id.ibtn_main_page_search);
        mSearchIbtn.setOnClickListener(this);
        mSlidingMenuListView = findViewById(R.id.lv_sliding_menu_options);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_sliding_menu_close: {
                mDrawLayout.closeDrawer(Gravity.LEFT);
                break;
            }
            case R.id.ibtn_main_page_open_drawer: {
                mDrawLayout.openDrawer(Gravity.LEFT);
                break;
            }
            case R.id.ibtn_main_page_search: {
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mShouldQuit) {
            mShouldQuit = false;
            Toast.makeText(this, "再按一次返回桌面", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(ConstantValues.RESET_TURN_OFF_STATUS, 2000);
        } else {
            finish();
        }

    }
}
