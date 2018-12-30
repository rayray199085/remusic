package com.project.stephencao.remusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.project.stephencao.remusic.R;
import com.project.stephencao.remusic.adapter.MyDisplaySongListAdapter;
import com.project.stephencao.remusic.bean.MusicInfo;
import com.project.stephencao.remusic.engine.MusicListDao;
import com.project.stephencao.remusic.utils.ConstantValues;
import com.project.stephencao.remusic.utils.SharedPreferencesUtil;

import java.util.List;

public class RecentPlayMusicActivity extends Activity implements View.OnClickListener {
    private ImageButton mBackIbtn, mSearchIbtn, mSelectIbtn;
    private TextView mDescriptionTv;
    private ListView mListView;
    private List<MusicInfo> musicInfoList;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ConstantValues.FILL_LIST_VIEW:{
                    MyDisplaySongListAdapter myDisplaySongListAdapter = new MyDisplaySongListAdapter(
                            RecentPlayMusicActivity.this,musicInfoList);
                    mListView.setAdapter(myDisplaySongListAdapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(RecentPlayMusicActivity.this, PlaySongActivity.class);
                            intent.putExtra(ConstantValues.MUSIC_INFO,musicInfoList.get(position));
                            startActivity(intent);
                        }
                    });
                    break;
                }
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_play);
        initView();
        initData();
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                musicInfoList = MusicListDao.queryAllFromDatabase(
                        RecentPlayMusicActivity.this, ConstantValues.RECENT_PLAY_DATABASE_NAME);
                mHandler.sendEmptyMessage(ConstantValues.FILL_LIST_VIEW);
                SharedPreferencesUtil.putInteger(RecentPlayMusicActivity.this,
                        ConstantValues.RECENT_PLAY_COUNT,musicInfoList.size());
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
        mListView = findViewById(R.id.lv_recent_play_song_list);
        mDescriptionTv = findViewById(R.id.tv_local_music_title_bar_description);
        mDescriptionTv.setText("最近播放");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtn_local_music_title_bar_back:{
                finish();
                break;
            }
        }
    }
}
