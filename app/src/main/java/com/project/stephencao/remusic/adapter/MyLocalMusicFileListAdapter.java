package com.project.stephencao.remusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.project.stephencao.remusic.R;
import com.project.stephencao.remusic.bean.MusicInfo;

import java.util.List;

public class MyLocalMusicFileListAdapter extends BaseAdapter {
    private Context context;
    private List<MusicInfo> musicInfoList;
    private LayoutInflater inflater;

    public MyLocalMusicFileListAdapter(Context context, List<MusicInfo> musicInfoList) {
        this.context = context;
        this.musicInfoList = musicInfoList;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return musicInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return musicInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.view_local_music_single_song_list_item,null);
            viewHolder.iconImageView = convertView.findViewById(R.id.iv_local_music_list_item_play_icon);
            viewHolder.setImageView = convertView.findViewById(R.id.iv_local_music_list_item_manage);
            viewHolder.titleTextView = convertView.findViewById(R.id.tv_local_music_list_item_title);
            viewHolder.artistTextView = convertView.findViewById(R.id.tv_local_music_list_item_artist);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MusicInfo musicInfo = musicInfoList.get(position);
        viewHolder.titleTextView.setText(musicInfo.title);
        viewHolder.artistTextView.setText(musicInfo.artist);
        return convertView;
    }

    class ViewHolder {
        public ImageView iconImageView, setImageView;
        public TextView titleTextView, artistTextView;
    }
}
