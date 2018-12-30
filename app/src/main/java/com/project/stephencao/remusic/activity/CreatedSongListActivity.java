package com.project.stephencao.remusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.*;
import com.project.stephencao.remusic.R;
import com.project.stephencao.remusic.adapter.MyDisplaySongListAdapter;
import com.project.stephencao.remusic.bean.MusicInfo;
import com.project.stephencao.remusic.engine.MusicListDao;
import com.project.stephencao.remusic.service.MusicService;
import com.project.stephencao.remusic.utils.ConstantValues;
import com.project.stephencao.remusic.utils.SharedPreferencesUtil;

import java.util.List;

public class CreatedSongListActivity extends Activity implements View.OnClickListener {
    private ImageButton mBackIbtn, mSearchIbtn, mSelectIbtn;
    private TextView mDescriptionTv;
    private ListView mListView;
    private ImageButton mPlayAllIbtn;
    private List<MusicInfo> musicInfoList;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantValues.FILL_LIST_VIEW: {
                    MyDisplaySongListAdapter myDisplaySongListAdapter = new MyDisplaySongListAdapter(
                            CreatedSongListActivity.this, musicInfoList);
                    mListView.setAdapter(myDisplaySongListAdapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(CreatedSongListActivity.this, PlaySongActivity.class);
                            intent.putExtra(ConstantValues.MUSIC_INFO, musicInfoList.get(position));
                            startActivity(intent);
                        }
                    });
                    break;
                }
                case ConstantValues.SEND_A_BROADCAST:{
                    Intent intent = new Intent();
                    intent.setAction("com.music.player.play.all");
                    intent.putExtra(ConstantValues.BOOKMARK_MUSIC_LIST, true);
                    sendBroadcast(intent);
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_song_list);
        initView();
        initData();
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                musicInfoList = MusicListDao.queryAllFromDatabase(
                        CreatedSongListActivity.this, ConstantValues.BOOKMARK_LIST_DATABASE_NAME);
                mHandler.sendEmptyMessage(ConstantValues.FILL_LIST_VIEW);
                SharedPreferencesUtil.putInteger(CreatedSongListActivity.this,
                        ConstantValues.BOOKMARK_PLAY_COUNT, musicInfoList.size());
                if (musicInfoList.size() > 0) {
                    SharedPreferencesUtil.putString(CreatedSongListActivity.this,
                            ConstantValues.BOOKMARK_LIST_ICON, musicInfoList.get(0).url);
                } else {
                    SharedPreferencesUtil.putString(CreatedSongListActivity.this,
                            ConstantValues.BOOKMARK_LIST_ICON, "");
                }
            }
        }).start();
    }

    private void initView() {
        mBackIbtn = findViewById(R.id.ibtn_local_music_title_bar_back);
        mBackIbtn.setOnClickListener(this);
        mSearchIbtn = findViewById(R.id.ibtn_local_music_title_bar_search);
        mSelectIbtn = findViewById(R.id.ibtn_local_music_title_bar_selection);
        mSearchIbtn.setVisibility(View.GONE);
        mSelectIbtn.setVisibility(View.GONE);
        mListView = findViewById(R.id.lv_activity_created_song_list_songs);
        mDescriptionTv = findViewById(R.id.tv_local_music_title_bar_description);
        mDescriptionTv.setText("已创建的歌单");
        mPlayAllIbtn = findViewById(R.id.ibtn_activity_created_song_list_play_all);
        mPlayAllIbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_local_music_title_bar_back: {
                finish();
                break;
            }
            case R.id.ibtn_activity_created_song_list_play_all: {
                if ((musicInfoList != null && musicInfoList.size()>0)) {
                    Intent intent = new Intent(CreatedSongListActivity.this, MusicService.class);
                    startService(intent);
                    mHandler.sendEmptyMessageDelayed(ConstantValues.SEND_A_BROADCAST,500);
                }
                break;
            }

        }
    }
}
