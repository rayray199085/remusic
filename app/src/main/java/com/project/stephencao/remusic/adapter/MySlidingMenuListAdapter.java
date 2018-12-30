package com.project.stephencao.remusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.project.stephencao.remusic.R;
import com.project.stephencao.remusic.bean.SlidingMenuItem;

import java.util.List;

public class MySlidingMenuListAdapter extends BaseAdapter {
    private Context context;
    private List<SlidingMenuItem> slidingMenuItemList;
    private LayoutInflater inflater;


    public MySlidingMenuListAdapter(Context context, List<SlidingMenuItem> slidingMenuItemList) {
        this.context = context;
        this.slidingMenuItemList = slidingMenuItemList;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return slidingMenuItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return slidingMenuItemList.get(position);
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
            convertView = inflater.inflate(R.layout.view_sliding_menu_item, null);
            viewHolder.imageView = convertView.findViewById(R.id.iv_sliding_menu_item_icon);
            viewHolder.textView = convertView.findViewById(R.id.tv_sliding_menu_item_description);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SlidingMenuItem item = slidingMenuItemList.get(position);
        viewHolder.imageView.setBackgroundResource(item.drawableID);
        viewHolder.textView.setText(item.itemDescription);
        return convertView;
    }

    class ViewHolder {
        public ImageView imageView;
        public TextView textView;
    }
}
