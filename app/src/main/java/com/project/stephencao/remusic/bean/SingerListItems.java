package com.project.stephencao.remusic.bean;

import android.graphics.Bitmap;

public class SingerListItems {
    public Bitmap bitmap;
    public String artist;
    public int songCount;
    public String album;
    public String url;
    public String parentFileName;
    public String parentPath;

    @Override
    public String toString() {
        return "SingerListItems{" +
                "bitmap=" + bitmap +
                ", artist='" + artist + '\'' +
                ", songCount=" + songCount +
                ", album='" + album + '\'' +
                ", url='" + url + '\'' +
                ", parentFileName='" + parentFileName + '\'' +
                ", parentPath='" + parentPath + '\'' +
                '}';
    }
}
