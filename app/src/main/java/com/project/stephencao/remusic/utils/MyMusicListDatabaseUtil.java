package com.project.stephencao.remusic.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyMusicListDatabaseUtil extends SQLiteOpenHelper {
    public MyMusicListDatabaseUtil(Context context){
        this(context,"music_list.db",null,1);
    }
    public MyMusicListDatabaseUtil(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table bookmark(_id integer primary key autoincrement,title varchar(30)," +
                "artist varchar(30),album varchar(30),duration varchar(30),url varchar(100))");
        db.execSQL("create table recent_play(_id integer primary key autoincrement,title varchar(30)," +
                "artist varchar(30),album varchar(30),duration varchar(30),url varchar(100))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
