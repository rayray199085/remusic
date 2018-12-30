package com.project.stephencao.remusic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.project.stephencao.remusic.R;

public class MyLocalMusicItem extends RelativeLayout {
    private View mView;
    private TextView mOptionTv;
    private ImageView mIconIv;

    public MyLocalMusicItem(Context context) {
        this(context, null);
    }

    public MyLocalMusicItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLocalMusicItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mView = LayoutInflater.from(context).inflate(R.layout.view_local_music_item, this);
        initView();
    }

    private void initView() {
        mIconIv = mView.findViewById(R.id.iv_local_music_icon);
        mOptionTv = mView.findViewById(R.id.tv_local_music_option);
    }

    public void setIcon(int drawableId){
        mIconIv.setBackgroundResource(drawableId);
    }
    public void setOptionContent(String content){
        mOptionTv.setText(content);
    }

}
