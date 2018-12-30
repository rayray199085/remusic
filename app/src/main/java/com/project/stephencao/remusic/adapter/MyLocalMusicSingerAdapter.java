package com.project.stephencao.remusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.project.stephencao.remusic.R;
import com.project.stephencao.remusic.bean.SingerListItems;
import com.project.stephencao.remusic.utils.ConstantValues;

import java.util.List;

public class MyLocalMusicSingerAdapter extends BaseAdapter {
    private Context context;
    private List<SingerListItems> singerListItemsList;
    private LayoutInflater inflater;
    private int type;

    public MyLocalMusicSingerAdapter(Context context, List<SingerListItems> singerListItemsList, int type) {
        this.context = context;
        this.singerListItemsList = singerListItemsList;
        inflater = LayoutInflater.from(this.context);
        this.type = type;
    }

    @Override
    public int getCount() {
        return singerListItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return singerListItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.view_local_music_singer_list_item, null);
            viewHolder.iconImageView = convertView.findViewById(R.id.iv_singer_list_album_icon);
            viewHolder.setImageView = convertView.findViewById(R.id.iv_singer_list_manage);
            viewHolder.songCountTextView = convertView.findViewById(R.id.tv_singer_list_song_number);
            viewHolder.artistTextView = convertView.findViewById(R.id.tv_singer_list_artist);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SingerListItems items = singerListItemsList.get(position);
        switch (type){
            case ConstantValues.IS_SINGER_ITEM:{
                viewHolder.songCountTextView.setText(items.songCount + " 首");
                viewHolder.iconImageView.setImageBitmap(items.bitmap);
                viewHolder.artistTextView.setText(items.artist);
                break;
            }case ConstantValues.IS_ALBUM_ITEM:{
                viewHolder.songCountTextView.setText(items.artist + " : " + items.songCount + " 首");
                viewHolder.iconImageView.setImageBitmap(items.bitmap);
                viewHolder.artistTextView.setText(items.album);
                break;
            }case ConstantValues.IS_FILE_ITEM:{
                viewHolder.songCountTextView.setText(items.songCount + " 首 "+ items.parentPath);
                viewHolder.iconImageView.setBackgroundResource(R.drawable.file_icon);
                viewHolder.artistTextView.setText(items.parentFileName);
                break;
            }
        }
        return convertView;
    }
    class ViewHolder {
        public ImageView iconImageView, setImageView;
        public TextView songCountTextView, artistTextView;
    }
}
