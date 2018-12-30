package com.project.stephencao.remusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.project.stephencao.remusic.R;
import com.project.stephencao.remusic.adapter.MyDisplaySongListAdapter;
import com.project.stephencao.remusic.bean.MusicInfo;
import com.project.stephencao.remusic.utils.ConstantValues;
import com.project.stephencao.remusic.utils.LocalMusicUtil;

import java.util.ArrayList;
import java.util.List;

public class DisplaySongActivity extends Activity implements View.OnClickListener {
    private ListView mListView;
    private ImageButton mBackIbtn;
    private TextView mArtistTv;
    private String mRegex;
    private List<MusicInfo> mMusicInfoList;
    private List<MusicInfo> mRemoveList;
    private String value = "";
    private String condition = "";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantValues.DISPLAY_CERTAIN_SONGS: {
                    MyDisplaySongListAdapter myDisplaySongListAdapter = null;
                    if ("url".equals(condition)) {
                        mRemoveList = new ArrayList<>();
                        for (MusicInfo musicInfo : mMusicInfoList) {
                            if (!musicInfo.url.matches(mRegex)) {
                                mRemoveList.add(musicInfo);
                            }
                        }
                        mMusicInfoList.removeAll(mRemoveList);
                    }
                    myDisplaySongListAdapter =
                            new MyDisplaySongListAdapter(DisplaySongActivity.this, mMusicInfoList);
                    mListView.setAdapter(myDisplaySongListAdapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(DisplaySongActivity.this, PlaySongActivity.class);
                            intent.putExtra(ConstantValues.MUSIC_INFO, mMusicInfoList.get(position));
                            startActivity(intent);
                        }
                    });
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_song);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String artistName = intent.getStringExtra("artist");
        String album = intent.getStringExtra("album");
        String url = intent.getStringExtra("url");
        if (artistName != null && !"".equals(artistName)) {
            value = artistName;
            condition = "artist";
        } else if (album != null && !"".equals(album)) {
            value = album;
            condition = "album";
        } else if (url != null && !"".equals(url)) {
            value = url;
            mRegex = value + "/[^/]+";
            condition = "url";
        }
        mArtistTv.setText(value);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if ("album".equals(condition) || "artist".equals(condition)) {
                    mMusicInfoList = LocalMusicUtil.getMusicInfoListInCondition(DisplaySongActivity.this,
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, condition, value, false);
                } else {
                    mMusicInfoList = LocalMusicUtil.getMusicInfoListInCondition(DisplaySongActivity.this,
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, condition, value, true);
                }
                mHandler.sendEmptyMessage(ConstantValues.DISPLAY_CERTAIN_SONGS);
            }
        }).start();
    }

    private void initView() {
        mListView = findViewById(R.id.lv_display_song_list_view);
        mArtistTv = findViewById(R.id.tv_display_songs_description);
        mBackIbtn = findViewById(R.id.ibtn_display_songs_back);
        mBackIbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_display_songs_back: {
                finish();
            }
        }
    }
}
