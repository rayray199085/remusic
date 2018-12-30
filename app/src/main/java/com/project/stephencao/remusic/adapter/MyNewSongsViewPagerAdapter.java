package com.project.stephencao.remusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MyNewSongsViewPagerAdapter extends PagerAdapter {
    private Context context;
    private int[] albums;

    public MyNewSongsViewPagerAdapter(Context context, int[] albums) {
        this.context = context;
        this.albums = albums;
    }

    @Override
    public int getCount() {
        return albums.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundResource(albums[position]);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
