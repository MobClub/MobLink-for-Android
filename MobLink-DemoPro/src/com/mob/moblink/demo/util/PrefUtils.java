package com.mob.moblink.demo.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class PrefUtils {

    private static SharedPreferences spf;
    public static final String SHARED_PREFERENCE_NAME = "Andyon";

    /**
     * get SharedPreferences
     *
     * @param context
     * @return
     */
    public static SharedPreferences getSpf(Context context) {
        if (spf == null) {
            spf = context.getSharedPreferences(SHARED_PREFERENCE_NAME,
                    Activity.MODE_PRIVATE);
        }
        return spf;
    }

    public static boolean getBoolean(String key, boolean defValue, Context ctx) {
        return getSpf(ctx).getBoolean(key, defValue);
    }

    /**
     * put boolean value into SharedPreferences
     *
     * @param key
     * @param value
     * @param ctx
     * @return true if success
     */
    public static boolean putBoolean(String key, boolean value, Context ctx) {
        return getSpf(ctx).edit().putBoolean(key, value).commit();
    }

    public static boolean putInt(String key, int value, Context ctx) {
        return getSpf(ctx).edit().putInt(key, value).commit();
    }

    public static boolean putLong(String key, int value, Context ctx){
        return getSpf(ctx).edit().putLong(key, value).commit();
    }

    public static boolean putString(String key, String value, Context context) {
        return getSpf(context).edit().putString(key, value).commit();
    }

    public static int getInt(String key, int defaultVal, Context ctx) {
        return getSpf(ctx).getInt(key, defaultVal);
    }

    public static String getString(String key, Context ctx) {
        return getSpf(ctx).getString(key, "");
    }

    public static String getString(String key, String value, Context ctx) {
        return getSpf(ctx).getString(key, value);
    }

    public static void removeKey(String key, Context ctx) {
        getSpf(ctx).edit().remove(key).commit();
    }

    public static boolean putFloat(String key, float value, Context ctx) {
        return getSpf(ctx).edit().putFloat(key, value).commit();
    }

    public static float getFloat(String key, float defaultVal, Context ctx) {
        return getSpf(ctx).getFloat(key, defaultVal);
    }

    public static void putLong(String key, long value, Context ctx) {
        getSpf(ctx).edit().putLong(key, value).commit();
    }

    public static long getLong(String key, long defaultVal, Context ctx) {
        return getSpf(ctx).getLong(key, defaultVal);
    }


    public void setSharedPreferences(Context ctx, Map<String, String> param){
        SharedPreferences.Editor ed = getSpf(ctx).edit();
        for(Map.Entry<String, String> entry : param.entrySet()){
            ed.putString(entry.getKey(), entry.getValue());
        }
        ed.commit();
    }
}
