package com.project.stephencao.remusic.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import java.io.IOException;

public class MyMusicPlayer {
    private static MediaPlayer mMusicPlayer;

    public static MediaPlayer playSong(Context context, String url) {
        if (mMusicPlayer == null) {
            mMusicPlayer = new MediaPlayer();
        }
        mMusicPlayer.reset();
        try {
            mMusicPlayer.setDataSource(context, Uri.parse(url));
            mMusicPlayer.prepare();
            mMusicPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mMusicPlayer;
    }

    public static int getCurrentPosition(Context context, MediaPlayer mediaPlayer) {
        return mediaPlayer.getCurrentPosition();
    }

    public static int pauseSong(Context context, MediaPlayer mediaPlayer) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            int currentPosition = mediaPlayer.getCurrentPosition();
            return currentPosition;
        }
        return -1;
    }

    public static void continuePlaySong(Context context, MediaPlayer mediaPlayer, int currentPosition) {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(currentPosition);
            mediaPlayer.start();
        }
    }

    public static void stopMusic() {
        if (mMusicPlayer != null) {
            mMusicPlayer.stop();
        }
    }
}
