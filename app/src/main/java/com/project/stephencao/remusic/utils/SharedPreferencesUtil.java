package com.project.stephencao.remusic.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    public static void putInteger(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("remusicSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static int getInteger(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("remusicSettings", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }
    public static boolean getBoolean(Context context, String key,boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("remusicSettings", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, value);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("remusicSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }
    public static String getString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("remusicSettings", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("remusicSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }
}
