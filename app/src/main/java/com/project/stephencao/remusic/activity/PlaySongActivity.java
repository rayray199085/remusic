package com.project.stephencao.remusic.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.*;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.project.stephencao.remusic.R;
import com.project.stephencao.remusic.bean.MusicInfo;
import com.project.stephencao.remusic.engine.MusicListDao;
import com.project.stephencao.remusic.service.MusicService;
import com.project.stephencao.remusic.utils.ConstantValues;
import com.project.stephencao.remusic.utils.SharedPreferencesUtil;

public class PlaySongActivity extends Activity implements View.OnClickListener {
    private ImageButton mOffServiceIbtn, mSearchIbtn,mSelectIbtn,mBackIbtn;
    private ImageView mDiscLightIv, mDiscPinIv, mAlbumIconIv;
    private TextView mTitleTv;
    private MusicInfo mMusicInfo;
    private MyPauseMusicBroadcastReceiver myPauseMusicBroadcastReceiver;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantValues.SEND_A_BROADCAST: {
                    Intent receiverIntent = new Intent();
                    receiverIntent.putExtra(ConstantValues.MUSIC_INFO, mMusicInfo);
                    receiverIntent.setAction("com.music.player.get.data");
                    sendBroadcast(receiverIntent);
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        initView();
        initData(null);
        initAnimation();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        initData(intent);
        initAnimation();
    }

    private void initAnimation() {
        RotateAnimation pinRotateAnimationOn = initPinOnAnimation();
        mDiscPinIv.startAnimation(pinRotateAnimationOn);
        pinRotateAnimationOn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RotateAnimation discRotateAnimation = initDiscAnimation();
                mDiscLightIv.startAnimation(discRotateAnimation);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void stopAnimation(){
        mDiscLightIv.clearAnimation();
        mDiscPinIv.startAnimation(stopPinOnAnimation());
    }

    private RotateAnimation stopPinOnAnimation(){
        RotateAnimation pinRotateAnimationOn = new RotateAnimation(15.0f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
        pinRotateAnimationOn.setDuration(1000);
        pinRotateAnimationOn.setFillAfter(true);
        mDiscPinIv.clearAnimation();
        return pinRotateAnimationOn;
    }

    private RotateAnimation initDiscAnimation() {
        RotateAnimation discRotateAnimation = new RotateAnimation(0.0f,
                360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        discRotateAnimation.setRepeatCount(Animation.INFINITE);
        discRotateAnimation.setDuration(200);
        mDiscLightIv.clearAnimation();
        return discRotateAnimation;
    }

    private RotateAnimation initPinOnAnimation() {
        RotateAnimation pinRotateAnimationOn = new RotateAnimation(0.0f, 15.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
        pinRotateAnimationOn.setDuration(1000);
        pinRotateAnimationOn.setFillAfter(true);
        mDiscPinIv.clearAnimation();
        return pinRotateAnimationOn;
    }

    private void initView() {
        mOffServiceIbtn = findViewById(R.id.ibtn_play_song_activity_end);
        mOffServiceIbtn.setOnClickListener(this);
        mDiscLightIv = findViewById(R.id.iv_activity_play_song_disc_light);
        mDiscPinIv = findViewById(R.id.iv_activity_play_song_disc_pin);
        mAlbumIconIv = findViewById(R.id.iv_activity_play_song_disc_icon);
        mSearchIbtn = findViewById(R.id.ibtn_local_music_title_bar_search);
        mSearchIbtn.setVisibility(View.GONE);
        mSelectIbtn = findViewById(R.id.ibtn_local_music_title_bar_selection);
        mSelectIbtn.setVisibility(View.GONE);
        mBackIbtn = findViewById(R.id.ibtn_local_music_title_bar_back);
        mBackIbtn.setOnClickListener(this);
        mTitleTv = findViewById(R.id.tv_local_music_title_bar_description);
    }

    private void initData(Intent nextSongIntent) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.music.player.pause_music");
        myPauseMusicBroadcastReceiver = new MyPauseMusicBroadcastReceiver();
        registerReceiver(myPauseMusicBroadcastReceiver, intentFilter);

        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        if(nextSongIntent==null) {
            mMusicInfo = (MusicInfo) getIntent().getSerializableExtra(ConstantValues.MUSIC_INFO);
        }
        else {
            mMusicInfo = (MusicInfo) nextSongIntent.getSerializableExtra(ConstantValues.MUSIC_INFO);
        }
        mHandler.sendEmptyMessageDelayed(ConstantValues.SEND_A_BROADCAST, 500);
        String iconPath = SharedPreferencesUtil.getString(this, mMusicInfo.url);
        mAlbumIconIv.setImageBitmap(BitmapFactory.decodeFile(iconPath));
        mTitleTv.setText(mMusicInfo.title);
        recordRecentPlaySongs(mMusicInfo);
    }

    private void recordRecentPlaySongs(final MusicInfo mMusicInfo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MusicListDao.insertIntoDataBase(PlaySongActivity.this,mMusicInfo,ConstantValues.RECENT_PLAY_DATABASE_NAME);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_play_song_activity_end: {
                stopService(new Intent(this, MusicService.class));
                break;
            }case R.id.ibtn_local_music_title_bar_back:{
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        mDiscLightIv.clearAnimation();
        mDiscPinIv.clearAnimation();
        if(myPauseMusicBroadcastReceiver!=null){
            unregisterReceiver(myPauseMusicBroadcastReceiver);
        }
        super.onDestroy();
    }

    class MyPauseMusicBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.music.player.pause_music".equals(intent.getAction())) {
                boolean shouldPause = intent.getBooleanExtra(ConstantValues.SHOULD_PAUSE,true);
                if(shouldPause){
                    stopAnimation();
                }else {
                    initAnimation();
                }

            }
        }
    }
}
