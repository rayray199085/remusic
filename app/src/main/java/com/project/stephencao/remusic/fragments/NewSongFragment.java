package com.project.stephencao.remusic.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.project.stephencao.remusic.R;
import com.project.stephencao.remusic.adapter.MyNewSongsViewPagerAdapter;
import com.project.stephencao.remusic.utils.ConstantValues;

import java.util.*;

public class NewSongFragment extends Fragment implements View.OnClickListener {
    public static final String BUNDLE_TITLE = "title";
    private View mView;
    private Timer mTimer;
    private OnJumpToMoreListener mListener;
    private List<ImageView> mDotIvList;
    private int mPageIndex = 0;
    private ImageView mAlbumDotIv1, mAlbumDotIv2, mAlbumDotIv3, mAlbumDotIv4, mAlbumDotIv5, mAlbumDotIv6, mAlbumDotIv7, mAlbumDotIv8;
    private ViewPager mViewPager;
    private TextView mMoreTv, mEditPositionTv;
    private static int[] albums = {R.drawable.album1, R.drawable.album2, R.drawable.album3, R.drawable.album4,
            R.drawable.album5, R.drawable.album6, R.drawable.album7, R.drawable.album8,};
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantValues.RECYCLE_DISPLAY_VIEWPAGER: {
                    resetAllAlbumDots();
                    mDotIvList.get(mPageIndex).setBackgroundResource(R.drawable.dot_red);
                    mViewPager.setCurrentItem(mPageIndex++);
                    break;
                }
            }
        }
    };

    public static NewSongFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        NewSongFragment fragment = new NewSongFragment();
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
        mView = inflater.inflate(R.layout.view_new_songs_fragment, container, false);
        initView();
        return mView;
    }

    private void initView() {
        mViewPager = mView.findViewById(R.id.vp_new_songs_view_pager);
        mMoreTv = mView.findViewById(R.id.tv_new_songs_more);
        mEditPositionTv = mView.findViewById(R.id.tv_new_songs_adjust_positions);
        mMoreTv.setOnClickListener(this);
        mEditPositionTv.setOnClickListener(this);
        mDotIvList = new ArrayList<>();
        mAlbumDotIv1 = mView.findViewById(R.id.iv_page_position_1);
        mAlbumDotIv2 = mView.findViewById(R.id.iv_page_position_2);
        mAlbumDotIv3 = mView.findViewById(R.id.iv_page_position_3);
        mAlbumDotIv4 = mView.findViewById(R.id.iv_page_position_4);
        mAlbumDotIv5 = mView.findViewById(R.id.iv_page_position_5);
        mAlbumDotIv6 = mView.findViewById(R.id.iv_page_position_6);
        mAlbumDotIv7 = mView.findViewById(R.id.iv_page_position_7);
        mAlbumDotIv8 = mView.findViewById(R.id.iv_page_position_8);
        mDotIvList.add(mAlbumDotIv1);
        mDotIvList.add(mAlbumDotIv2);
        mDotIvList.add(mAlbumDotIv3);
        mDotIvList.add(mAlbumDotIv4);
        mDotIvList.add(mAlbumDotIv5);
        mDotIvList.add(mAlbumDotIv6);
        mDotIvList.add(mAlbumDotIv7);
        mDotIvList.add(mAlbumDotIv8);
        initViewPager();
    }

    private void initViewPager() {
        MyNewSongsViewPagerAdapter myNewSongsViewPagerAdapter = new MyNewSongsViewPagerAdapter(getContext(), albums);
        mViewPager.setAdapter(myNewSongsViewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                resetAllAlbumDots();
                mDotIvList.get(i).setBackgroundResource(R.drawable.dot_red);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (mPageIndex == albums.length) {
                    mPageIndex = 0;
                }
                mHandler.sendEmptyMessage(ConstantValues.RECYCLE_DISPLAY_VIEWPAGER);
            }
        };
        mTimer.schedule(timerTask,0,3000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_new_songs_more: {
                if(mListener!=null){
                    mListener.doJump();
                }
                break;
            }
            case R.id.tv_new_songs_adjust_positions: {
                break;
            }
        }
    }

    private void resetAllAlbumDots() {
        mAlbumDotIv1.setBackgroundResource(R.drawable.dot_grey);
        mAlbumDotIv2.setBackgroundResource(R.drawable.dot_grey);
        mAlbumDotIv3.setBackgroundResource(R.drawable.dot_grey);
        mAlbumDotIv4.setBackgroundResource(R.drawable.dot_grey);
        mAlbumDotIv5.setBackgroundResource(R.drawable.dot_grey);
        mAlbumDotIv6.setBackgroundResource(R.drawable.dot_grey);
        mAlbumDotIv7.setBackgroundResource(R.drawable.dot_grey);
        mAlbumDotIv8.setBackgroundResource(R.drawable.dot_grey);
    }

    public void setOnJumpToMoreListener(OnJumpToMoreListener onJumpToMoreListener){
        mListener = onJumpToMoreListener;
    }
    public interface OnJumpToMoreListener{
        public void doJump();
    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }
}
