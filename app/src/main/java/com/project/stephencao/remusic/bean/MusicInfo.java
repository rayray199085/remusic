package com.project.stephencao.remusic.bean;

import android.graphics.Bitmap;
import android.os.Parcelable;

import java.io.Serializable;

public class MusicInfo implements Serializable {
    public long id;
    public long album_id;
    public String title;
    public String artist;
    public long size;
    public String url;
    public int isMusic;
    public long duration;
    public String album;
    public Bitmap bitmap;

    @Override
    public String toString() {
        return "MusicInfo{" +
                "id=" + id +
                ", album_id=" + album_id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", size=" + size +
                ", url='" + url + '\'' +
                ", isMusic=" + isMusic +
                ", duration='" + duration + '\'' +
                ", album='" + album + '\'' +
                '}';
    }
}
