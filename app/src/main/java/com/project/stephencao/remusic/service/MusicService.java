package com.project.stephencao.remusic.service;

import android.app.Service;
import android.content.*;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.project.stephencao.remusic.R;
import com.project.stephencao.remusic.bean.MusicInfo;
import com.project.stephencao.remusic.engine.MusicListDao;
import com.project.stephencao.remusic.utils.ConstantValues;
import com.project.stephencao.remusic.utils.MyMusicPlayer;
import com.project.stephencao.remusic.utils.SharedPreferencesUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service implements View.OnClickListener {
    private WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private WindowManager.LayoutParams params = mParams;
    private WindowManager mWindowManager;
    private MyMusicBroadcastReceiver myMusicBroadcastReceiver;
    private View mMusicPlayerView;
    private List<MusicInfo> musicInfoList;
    private int mCurrentIndex = 0;
    private Point mOutSize;
    private int mCurrentPosition;
    private boolean mIsPause = false;
    private MediaPlayer mMediaPlayer;
    private MusicInfo mMusicInfo;
    private ProgressBar mProgressBar;
    private ImageButton mNextIbtn, mListIbtn, mStartIbtn, mPauseIbtn, mPreviousIbtn;
    private ImageView mIconIv;
    private Timer mTimer;
    private MyPlayAllBroadcastReceiver myPlayAllBroadcastReceiver;
    private TextView mTitleTv, mArtistTv;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantValues.START_DETECT_PROGRESS_PAR: {
                    updateProgressBarProgress();
                    break;
                }
                case ConstantValues.INSERT_DB_SUCCESS: {
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyMusicBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.music.player.get.data".equals(intent.getAction())) {
                mMusicInfo = (MusicInfo) intent.getSerializableExtra(ConstantValues.MUSIC_INFO);
                String imagePath = SharedPreferencesUtil.getString(getApplicationContext(), mMusicInfo.url);
                mIconIv.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                mArtistTv.setText(mMusicInfo.artist);
                mTitleTv.setText(mMusicInfo.title);
                mProgressBar.setMax((int) mMusicInfo.duration);
                mTimer = new Timer();
                mHandler.sendEmptyMessageDelayed(ConstantValues.START_DETECT_PROGRESS_PAR, 500);
                prePareMusic();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_music_player_list: {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MusicListDao.insertIntoDataBase(getApplicationContext(), mMusicInfo, ConstantValues.BOOKMARK_LIST_DATABASE_NAME);
                        mHandler.sendEmptyMessage(ConstantValues.INSERT_DB_SUCCESS);
                    }
                }).start();
                break;
            }
            case R.id.ibtn_music_player_start: {
                prePareMusic();
                break;
            }
            case R.id.ibtn_music_player_pause: {
                if (mMediaPlayer != null) {
                    Intent intent = new Intent();
                    intent.setAction("com.music.player.pause_music");
                    if (!mIsPause) {
                        mCurrentPosition = MyMusicPlayer.pauseSong(getApplicationContext(), mMediaPlayer);
                        intent.putExtra(ConstantValues.SHOULD_PAUSE, true);
                        sendBroadcast(intent);
                        mIsPause = true;
                    } else {
                        if (mCurrentPosition != -1) {
                            MyMusicPlayer.continuePlaySong(getApplicationContext(), mMediaPlayer, mCurrentPosition);
                            intent.putExtra(ConstantValues.SHOULD_PAUSE, false);
                            sendBroadcast(intent);
                            mIsPause = false;
                        }
                    }
                }
                break;
            }
            case R.id.ibtn_music_player_next: {
                if (musicInfoList != null && mCurrentIndex + 1 < musicInfoList.size()) {
                    mCurrentIndex++;
                    sendBroadcastToPlaySongList();
                }
                else {
                    Toast.makeText(getApplicationContext(),"这是最后一首歌",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.ibtn_music_player_previous: {
                if (musicInfoList != null && mCurrentIndex - 1 > -1) {
                    mCurrentIndex--;
                    sendBroadcastToPlaySongList();
                }
                else {
                    Toast.makeText(getApplicationContext(),"这是第一首歌",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void prePareMusic() {
        if (mMusicInfo != null) {
            mMediaPlayer = MyMusicPlayer.playSong(getApplicationContext(), mMusicInfo.url);
            mCurrentPosition = 0;
            mIsPause = false;

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mCurrentIndex++;
                    if (musicInfoList != null && mCurrentIndex < musicInfoList.size()) {
                        sendBroadcastToPlaySongList();
                    }
                }
            });
        }
    }

    @Override
    public void onCreate() {
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mOutSize = new Point();
        mWindowManager.getDefaultDisplay().getSize(mOutSize);
        showMusicPlayer();
        registerMyReceiver();
        super.onCreate();
    }

    private void updateProgressBarProgress() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                int currentPosition = MyMusicPlayer.getCurrentPosition(getApplicationContext(), mMediaPlayer);
                mProgressBar.setProgress(currentPosition);
            }
        };
        if (mTimer != null) {
            mTimer.schedule(timerTask, 0, 1000);
        }
    }

    private void registerMyReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.music.player.get.data");
        myMusicBroadcastReceiver = new MyMusicBroadcastReceiver();
        registerReceiver(myMusicBroadcastReceiver, intentFilter);

        IntentFilter intentPlayAllFilter = new IntentFilter();
        intentPlayAllFilter.addAction("com.music.player.play.all");
        myPlayAllBroadcastReceiver = new MyPlayAllBroadcastReceiver();
        registerReceiver(myPlayAllBroadcastReceiver, intentPlayAllFilter);
    }

    private void showMusicPlayer() {
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.BOTTOM;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mMusicPlayerView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_music_player, null);
        mIconIv = mMusicPlayerView.findViewById(R.id.iv_music_player_icon);
        mTitleTv = mMusicPlayerView.findViewById(R.id.tv_music_player_title);
        mArtistTv = mMusicPlayerView.findViewById(R.id.tv_music_player_artist);
        mListIbtn = mMusicPlayerView.findViewById(R.id.ibtn_music_player_list);
        mListIbtn.setOnClickListener(this);
        mStartIbtn = mMusicPlayerView.findViewById(R.id.ibtn_music_player_start);
        mStartIbtn.setOnClickListener(this);
        mNextIbtn = mMusicPlayerView.findViewById(R.id.ibtn_music_player_next);
        mNextIbtn.setOnClickListener(this);
        mPreviousIbtn = mMusicPlayerView.findViewById(R.id.ibtn_music_player_previous);
        mPreviousIbtn.setOnClickListener(this);
        mPauseIbtn = mMusicPlayerView.findViewById(R.id.ibtn_music_player_pause);
        mPauseIbtn.setOnClickListener(this);
        mProgressBar = mMusicPlayerView.findViewById(R.id.pb_music_player_progress_bar);
        mWindowManager.addView(mMusicPlayerView, params);
    }

    @Override
    public void onDestroy() {
        if (mWindowManager != null && mMusicPlayerView != null) {
            mWindowManager.removeView(mMusicPlayerView);
        }
        if (myMusicBroadcastReceiver != null) {
            unregisterReceiver(myMusicBroadcastReceiver);
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (myPlayAllBroadcastReceiver != null) {
            unregisterReceiver(myPlayAllBroadcastReceiver);
        }
        MyMusicPlayer.stopMusic();
        Intent intent = new Intent();
        intent.setAction("com.music.player.pause_music");
        intent.putExtra(ConstantValues.SHOULD_PAUSE, true);
        sendBroadcast(intent);
        super.onDestroy();
    }

    class MyPlayAllBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.music.player.play.all".equals(intent.getAction())) {
                boolean isPlayList = intent.getBooleanExtra(ConstantValues.BOOKMARK_MUSIC_LIST, false);
                if (isPlayList) {
                    getBookmarkList();
                }
            }
        }
    }

    private void getBookmarkList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                musicInfoList = MusicListDao.queryAllFromDatabase(getApplicationContext(),
                        ConstantValues.BOOKMARK_LIST_DATABASE_NAME);
                sendBroadcastToPlaySongList();
            }
        }).start();
    }

    private void sendBroadcastToPlaySongList() {
        Intent intent = new Intent();
        intent.setAction("com.music.player.get.data");
        intent.putExtra(ConstantValues.MUSIC_INFO, musicInfoList.get(mCurrentIndex));
        sendBroadcast(intent);
    }
}
