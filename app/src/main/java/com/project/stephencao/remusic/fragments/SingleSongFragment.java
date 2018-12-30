package com.project.stephencao.remusic.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.project.stephencao.remusic.R;
import com.project.stephencao.remusic.activity.PlaySongActivity;
import com.project.stephencao.remusic.adapter.MyLocalMusicFileListAdapter;
import com.project.stephencao.remusic.bean.MusicInfo;
import com.project.stephencao.remusic.utils.ConstantValues;
import com.project.stephencao.remusic.utils.LocalMusicUtil;
import com.project.stephencao.remusic.utils.SharedPreferencesUtil;

import java.util.List;

public class SingleSongFragment extends Fragment {
    private View mView;
    private List<MusicInfo> mExternalMusicInfoList;
    private ListView mListView;
    private MyLocalMusicFileListAdapter myLocalMusicFileListAdapter;
    public static final String BUNDLE_TITLE = "title";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantValues.DISPLAY_DEFAULT_LOCAL_MUSIC_FILES: {
                    myLocalMusicFileListAdapter = new MyLocalMusicFileListAdapter(getContext(),mExternalMusicInfoList);
                    mListView.setAdapter(myLocalMusicFileListAdapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           Intent intent = new Intent(getContext(), PlaySongActivity.class);
                           intent.putExtra(ConstantValues.MUSIC_INFO,mExternalMusicInfoList.get(position));
                           startActivity(intent);
                        }
                    });
                    break;
                }
            }
        }
    };

    public static SingleSongFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        SingleSongFragment fragment = new SingleSongFragment();
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
        mView = inflater.inflate(R.layout.view_single_song_fragment, container, false);
        initView();
        initData();
        return mView;
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // get external music info
                mExternalMusicInfoList = LocalMusicUtil.getMusicInfoList(getContext(),
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
                mHandler.sendEmptyMessage(ConstantValues.DISPLAY_DEFAULT_LOCAL_MUSIC_FILES);
                SharedPreferencesUtil.putInteger(
                        getContext(), ConstantValues.LOCAL_MUSIC_FILE_COUNT, mExternalMusicInfoList.size());
            }
        }).start();
    }

    private void initView() {
        mListView = mView.findViewById(R.id.lv_single_song_fragment_content_display);
    }
}
