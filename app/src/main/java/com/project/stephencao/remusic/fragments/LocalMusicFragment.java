package com.project.stephencao.remusic.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import com.project.stephencao.remusic.R;
import com.project.stephencao.remusic.activity.CreatedSongListActivity;
import com.project.stephencao.remusic.activity.LocalMusicDisplayActivity;
import com.project.stephencao.remusic.activity.RecentPlayMusicActivity;
import com.project.stephencao.remusic.engine.MusicListDao;
import com.project.stephencao.remusic.utils.ConstantValues;
import com.project.stephencao.remusic.utils.SharedPreferencesUtil;
import com.project.stephencao.remusic.view.MyLocalMusicItem;

public class LocalMusicFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private int mLocalMusicCount, mRecentPlayCount, mSingerListCount;
    private MyLocalMusicItem mLocalMusicFile, mRecentPlayFile, mMySingerFile;
    private boolean mIsCreatedListExtend = false;
    private ImageButton mDeleteSongListIbtn;
    private boolean mIsBookmarkListExtend = false;
    private RelativeLayout mSongListRootRl;
    private LinearLayout mCreatedSongList, mBookmarkSongList;
    private ImageView mCreatedSongListIcon, mBookmarkSongListIcon, mSongListIconIv;
    private TextView mCreatedSongListDescription, mBookmarkSongListDescrpition, mSongListTitleTv, mSongListCountTv;
    public static final String BUNDLE_TITLE = "title";
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case ConstantValues.SHOULD_DELETE_SONG_LIST:{
                   Toast.makeText(getContext(), "Song list has been cleared.", Toast.LENGTH_SHORT).show();
                   mCreatedSongListDescription.setText("创建的歌单(1)");
                   mSongListIconIv.setImageBitmap(null);
                   mSongListCountTv.setText("0 首");
                   break;
               }
           }
        }
    };

    public static LocalMusicFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        LocalMusicFragment fragment = new LocalMusicFragment();
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
        mView = LayoutInflater.from(getContext()).inflate(R.layout.view_local_music_fragment, container, false);
        initView();
        return mView;
    }

    @Override
    public void onResume() {
        mLocalMusicCount = SharedPreferencesUtil.getInteger(getContext(), ConstantValues.LOCAL_MUSIC_FILE_COUNT);
        mLocalMusicFile.setOptionContent("本地音乐(" + mLocalMusicCount + ")");
        mRecentPlayCount = SharedPreferencesUtil.getInteger(getContext(), ConstantValues.RECENT_PLAY_COUNT);
        mRecentPlayFile.setOptionContent("最近播放(" + mRecentPlayCount + ")");
        mSingerListCount = SharedPreferencesUtil.getInteger(getContext(), ConstantValues.SINGER_LIST_COUNT);
        mMySingerFile.setOptionContent("我的歌手(" + mSingerListCount + ")");

        String bookmarkImageUrl = SharedPreferencesUtil.getString(getContext(), ConstantValues.BOOKMARK_LIST_ICON);
        String imageUrl = SharedPreferencesUtil.getString(getContext(), bookmarkImageUrl);
        mSongListIconIv.setImageBitmap(BitmapFactory.decodeFile(imageUrl));
        int bookmarkSongCount = SharedPreferencesUtil.getInteger(getContext(), ConstantValues.BOOKMARK_PLAY_COUNT);
        mSongListCountTv.setText(bookmarkSongCount + " 首");
        if (bookmarkSongCount > 0) {
            mCreatedSongListDescription.setText("创建的歌单(1)");
        }
        super.onResume();
    }

    private void initView() {
        mLocalMusicFile = mView.findViewById(R.id.mi_local_music_files);
        mLocalMusicFile.setIcon(R.drawable.local_music);
        mLocalMusicFile.setOnClickListener(this);

        mRecentPlayFile = mView.findViewById(R.id.mi_local_music_recent);
        mRecentPlayFile.setIcon(R.drawable.recent_play);
        mRecentPlayFile.setOnClickListener(this);

        MyLocalMusicItem mDownloadFile = mView.findViewById(R.id.mi_local_music_download);
        mDownloadFile.setIcon(R.drawable.download);
        mDownloadFile.setOptionContent("下载管理(0)");
        mDownloadFile.setOnClickListener(this);

        mMySingerFile = mView.findViewById(R.id.mi_local_music_singers);
        mMySingerFile.setIcon(R.drawable.my_singer);

        mMySingerFile.setOnClickListener(this);

        mCreatedSongList = mView.findViewById(R.id.ll_local_music_created_song_lists);
        mBookmarkSongList = mView.findViewById(R.id.ll_local_music_bookmark_song_lists);
        mCreatedSongList.setOnClickListener(this);
        mBookmarkSongList.setOnClickListener(this);

        mCreatedSongListIcon = mView.findViewById(R.id.iv_local_music_created_song_lists_icon);
        mCreatedSongListDescription = mView.findViewById(R.id.tv_local_music_created_song_lists_decription);

        mBookmarkSongListIcon = mView.findViewById(R.id.iv_local_music_bookmark_song_lists_icon);
        mBookmarkSongListDescrpition = mView.findViewById(R.id.tv_local_music_bookmark_song_lists_decription);

        mSongListRootRl = mView.findViewById(R.id.rl_local_music_song_list_display);
        mSongListRootRl.setOnClickListener(this);
        mSongListIconIv = mView.findViewById(R.id.iv_local_music_song_list_image);
        mSongListTitleTv = mView.findViewById(R.id.tv_local_music_song_list_title);
        mSongListCountTv = mView.findViewById(R.id.tv_local_music_song_list_count);

        mDeleteSongListIbtn = mView.findViewById(R.id.ibtn_local_music_song_list_delete);
        mDeleteSongListIbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mi_local_music_files: {
                startActivity(new Intent(getContext(), LocalMusicDisplayActivity.class));
                break;
            }
            case R.id.mi_local_music_recent: {
                startActivity(new Intent(getContext(), RecentPlayMusicActivity.class));
                break;
            }
            case R.id.mi_local_music_download: {
                break;
            }
            case R.id.mi_local_music_singers: {
                Intent intent = new Intent(getContext(), LocalMusicDisplayActivity.class);
                intent.putExtra(ConstantValues.IS_JUMP_TO_SINGER_PAGE, true);
                startActivity(intent);
                break;
            }
            case R.id.ll_local_music_created_song_lists: {
                if (!mIsCreatedListExtend) {
                    mCreatedSongListIcon.setBackgroundResource(R.drawable.arrow_down);
                    mSongListRootRl.setVisibility(View.VISIBLE);
                    mIsCreatedListExtend = true;
                } else {
                    mCreatedSongListIcon.setBackgroundResource(R.drawable.arrow_right);
                    mSongListRootRl.setVisibility(View.GONE);
                    mIsCreatedListExtend = false;
                }
                break;
            }
            case R.id.ll_local_music_bookmark_song_lists: {
                if (!mIsBookmarkListExtend) {
                    mBookmarkSongListIcon.setBackgroundResource(R.drawable.arrow_down);
                    mIsBookmarkListExtend = true;
                } else {
                    mBookmarkSongListIcon.setBackgroundResource(R.drawable.arrow_right);
                    mIsBookmarkListExtend = false;
                }
                break;
            }
            case R.id.rl_local_music_song_list_display: {
                startActivity(new Intent(getContext(), CreatedSongListActivity.class));
                break;
            }
            case R.id.ibtn_local_music_song_list_delete: {
                showDeleteSongListDialog();
                break;
            }
        }
    }

    private void showDeleteSongListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final AlertDialog alertDialog = builder.create();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_delete_song_list_dialog, null);
        alertDialog.setView(view);
        Button cancelButton = view.findViewById(R.id.btn_delete_song_list_cancel);
        Button confirmButton = view.findViewById(R.id.btn_delete_song_list_confirm);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MusicListDao.deleteAllFromDatabase(getContext(), ConstantValues.BOOKMARK_LIST_DATABASE_NAME);
                        mHandler.sendEmptyMessage(ConstantValues.SHOULD_DELETE_SONG_LIST);
                    }
                }).start();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

}
