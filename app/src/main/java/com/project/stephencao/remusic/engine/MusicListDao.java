package com.project.stephencao.remusic.engine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.project.stephencao.remusic.bean.MusicInfo;
import com.project.stephencao.remusic.utils.MyMusicListDatabaseUtil;

import java.util.ArrayList;
import java.util.List;

public class MusicListDao {
    public static void insertIntoDataBase(Context context, MusicInfo musicInfo, String dbName) {
        MyMusicListDatabaseUtil myMusicListDatabaseUtil = new MyMusicListDatabaseUtil(context);
        SQLiteDatabase sqLiteDatabase = myMusicListDatabaseUtil.getWritableDatabase();
        queryCertainItem(context,musicInfo,dbName);
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", musicInfo.title);
        contentValues.put("artist", musicInfo.artist);
        contentValues.put("album", musicInfo.album);
        contentValues.put("duration", String.valueOf(musicInfo.duration));
        contentValues.put("url", musicInfo.url);
        sqLiteDatabase.insert(dbName, null, contentValues);

        sqLiteDatabase.close();
        myMusicListDatabaseUtil.close();
    }
    public static List<MusicInfo> queryAllFromDatabase(Context context, String dbName){
        List<MusicInfo> musicInfoList = new ArrayList<>();
        MyMusicListDatabaseUtil myMusicListDatabaseUtil = new MyMusicListDatabaseUtil(context);
        SQLiteDatabase sqLiteDatabase = myMusicListDatabaseUtil.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(dbName, new String[]{"title", "artist", "album", "duration", "url"},
                null, null, null, null, "_id desc");
        while (cursor.moveToNext()){
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.title = cursor.getString(0);
            musicInfo.artist = cursor.getString(1);
            musicInfo.album = cursor.getString(2);
            musicInfo.duration = Long.valueOf(cursor.getString(3));
            musicInfo.url = cursor.getString(4);
            musicInfoList.add(musicInfo);
        }
        cursor.close();
        sqLiteDatabase.close();
        myMusicListDatabaseUtil.close();
        return musicInfoList;
    }

    public static void queryCertainItem (Context context, MusicInfo musicInfo, String dbName){
        MyMusicListDatabaseUtil myMusicListDatabaseUtil = new MyMusicListDatabaseUtil(context);
        SQLiteDatabase sqLiteDatabase = myMusicListDatabaseUtil.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(dbName, null, "title=? AND artist=?",
                new String[]{musicInfo.title, musicInfo.artist}, null, null, null);
        if(cursor.getCount()>0){
            sqLiteDatabase.delete(dbName,"title=? AND artist=?",new String[]{musicInfo.title, musicInfo.artist});
        }
        cursor.close();
        sqLiteDatabase.close();
        myMusicListDatabaseUtil.close();

    }
    public static void deleteAllFromDatabase(Context context, String dbName){
        MyMusicListDatabaseUtil myMusicListDatabaseUtil = new MyMusicListDatabaseUtil(context);
        SQLiteDatabase sqLiteDatabase = myMusicListDatabaseUtil.getWritableDatabase();
        sqLiteDatabase.delete(dbName,null,null);
        sqLiteDatabase.close();
        myMusicListDatabaseUtil.close();
    }
}
