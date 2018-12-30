package com.project.stephencao.remusic.fragments;

import android.content.Intent;
import android.graphics.BitmapFactory;
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
import android.widget.ListView;
import com.project.stephencao.remusic.R;
import com.project.stephencao.remusic.activity.DisplaySongActivity;
import com.project.stephencao.remusic.adapter.MyLocalMusicSingerAdapter;
import com.project.stephencao.remusic.bean.MusicInfo;
import com.project.stephencao.remusic.bean.SingerListItems;
import com.project.stephencao.remusic.utils.ConstantValues;
import com.project.stephencao.remusic.utils.LocalMusicUtil;
import com.project.stephencao.remusic.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class SingerFragment extends Fragment {
    private View mView;
    private ListView mListView;
    private List<SingerListItems> mSingerListItemsList;
    private List<MusicInfo> mExternalMusicInfoList;
    public static final String BUNDLE_TITLE = "title";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantValues.DISPLAY_DEFAULT_LOCAL_MUSIC_FILES: {
                    prepareListViewItemData();
                    SharedPreferencesUtil.putInteger(getContext(),ConstantValues.SINGER_LIST_COUNT,mSingerListItemsList.size());
                    MyLocalMusicSingerAdapter myLocalMusicSingerAdapter =
                            new MyLocalMusicSingerAdapter(getContext(),mSingerListItemsList,ConstantValues.IS_SINGER_ITEM);
                    mListView.setAdapter(myLocalMusicSingerAdapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getContext(), DisplaySongActivity.class);
                            intent.putExtra("artist",mSingerListItemsList.get(position).artist);
                            startActivity(intent);
                        }
                    });
                    break;
                }
            }
        }
    };

    private void prepareListViewItemData() {
       mSingerListItemsList = new ArrayList<>();
        for (MusicInfo musicInfo : mExternalMusicInfoList) {
            boolean isExist = false;
            int position = 0;
            while (position <mSingerListItemsList.size()) {
                if (musicInfo.artist.equals(mSingerListItemsList.get(position).artist)) {
                   mSingerListItemsList.get(position).songCount++;
                    isExist = true;
                }
                position++;
            }
            if(!isExist){
                SingerListItems items = new SingerListItems();
                items.artist = musicInfo.artist;
                items.songCount = 1;
                String imagePath = SharedPreferencesUtil.getString(getContext(), musicInfo.url);
                items.bitmap = BitmapFactory.decodeFile(imagePath);
               mSingerListItemsList.add(items);
            }
        }
    }


    public static SingerFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        SingerFragment fragment = new SingerFragment();
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
        mView = inflater.inflate(R.layout.view_singer_fragment, container, false);
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
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
                mHandler.sendEmptyMessage(ConstantValues.DISPLAY_DEFAULT_LOCAL_MUSIC_FILES);
            }
        }).start();
    }

    private void initView() {
        mListView = mView.findViewById(R.id.lv_singer_fragment_content_display);
    }
}
