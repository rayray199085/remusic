package com.project.stephencao.remusic.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import com.project.stephencao.remusic.R;
import com.project.stephencao.remusic.bean.MusicInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class LocalMusicUtil {
    public static List<MusicInfo> getMusicInfoList(Context context, Uri uri, String order) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri,
                null, null, null, order);
        List<MusicInfo> musicInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));// NON-ZERO IS MUSIC
            Long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            if (isMusic != 0 && duration > 25000 && url.endsWith(".mp3")) {
                MusicInfo musicInfo = new MusicInfo();
                musicInfo.id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                musicInfo.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                musicInfo.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                musicInfo.duration = duration;
                musicInfo.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                musicInfo.url = url;
                musicInfo.album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                musicInfo.album_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
//                if (shouldAddBitmap) {
//                    musicInfo.bitmap = getAlbumArt(context, musicInfo.album_id);
//                    if (isFirstTime) {
//                        storeBitmapToLocalFile(context, musicInfo.bitmap, num, musicInfo.url);
//                    }
//                }
                musicInfo.isMusic = isMusic;
                musicInfoList.add(musicInfo);
            }
        }
        cursor.close();
        SharedPreferencesUtil.putBoolean(context, ConstantValues.FIRST_TIME_STORE_PICTURES, false);
        return musicInfoList;
    }

    private static void storeBitmapToLocalFile(Context context, Bitmap bitmap, int[] num, String url) {
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures", "myImage" + num[0] + ".jpg");
            SharedPreferencesUtil.putString(context, url, file.getAbsolutePath());
            num[0]++;
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Bitmap getAlbumArt(Context context, long album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cursor = context.getContentResolver().
                query(Uri.parse(mUriAlbums + "/" + Long.toString(album_id)),
                        projection, null, null, null);
        String album_art = null;
        if (cursor.getCount() > 0 && cursor.getColumnCount() > 0) {
            cursor.moveToNext();
            album_art = cursor.getString(0);
        }
        cursor.close();
        Bitmap bitmap;
        if (album_art != null) {
            bitmap = BitmapFactory.decodeFile(album_art);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.music);
        }
        cursor.close();
        return bitmap;
    }

    public static List<MusicInfo> getMusicInfoListInCondition(Context context, Uri uri, String condition, String value,
                                                              boolean isRoughCondition) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        if (!isRoughCondition) {
            cursor = contentResolver.query(uri,
                    null, condition + "=?", new String[]{value}, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        } else {
            cursor = contentResolver.query(uri, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        }
        List<MusicInfo> musicInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));// NON-ZERO IS MUSIC
            Long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            if (isMusic != 0 && duration > 25000 && url.endsWith(".mp3")) {
                MusicInfo musicInfo = new MusicInfo();
                musicInfo.id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                musicInfo.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                musicInfo.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                musicInfo.duration = duration;
                musicInfo.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                musicInfo.url = url;
                musicInfo.album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                musicInfo.album_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                musicInfoList.add(musicInfo);
            }
        }
        cursor.close();
        return musicInfoList;
    }

    public static void initSongPictures(Context context, Uri uri) {
        int[] num = new int[1];
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri,
                null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        while (cursor.moveToNext()) {
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));// NON-ZERO IS MUSIC
            Long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            if (isMusic != 0 && duration > 25000 && url.endsWith(".mp3")) {
                String mUrl = url;
                long album_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                Bitmap mBitmap = getAlbumArt(context, album_id);
                storeBitmapToLocalFile(context, mBitmap, num, mUrl);
            }
        }
        cursor.close();
    }
}
