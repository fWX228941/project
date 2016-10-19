package com.android.haobanyi.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fWX228941 on 2016/3/23.
 * 描述：基于SharedPreferences接口类的设置项工具类
 * 仅仅支持类型：String / Int / Long / Float / Boolean / String[]
 * 设计方案参考：calendar/Utils.java  getSharedPreference()
 * 统一使用apply()方法来代替commit()，这是Android 2.3（API级别为9）之后推出的异步执行写操作
 */
public class PreferenceUtil {
    //final的常量必须先初始化
    private static final String SHARED_PREFS_NAME = "com.android.haobanyi_preferences";

    /**
     * 布尔类型的设置值
     * @param context
     * @param key  String
     * @param defaultValue  boolean
     * @return boolean
     */
    public static boolean getSharedPreference(Context context, String key, boolean defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME,Context.MODE_PRIVATE);
        return prefs.getBoolean(key, defaultValue);
    }

    public static void setSharedPreference(Context context, String key, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    /*整形*/
    public static int getSharedPreference(Context context, String key, int defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME,Context.MODE_PRIVATE);
        return prefs.getInt(key, defaultValue);
    }

    public static void setSharedPreference(Context context, String key, int value) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /*字符串类型*/
    public static String getSharedPreference(Context context, String key, String defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME,Context.MODE_PRIVATE);
        return prefs.getString(key, defaultValue);
    }

    public static void setSharedPreference(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }


}
