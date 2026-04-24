package com.vzhukov.junior.project;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static final String PREF_NAME = "DovePrefs";
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    
    public static final String KEY_FIRST_RUN = "is_first_run";
    public static final String KEY_EMDR_SPEED = "emdr_speed";
    public static final String KEY_VIBRATION_ENABLED = "vibration_enabled";
    public static final String KEY_FAVORITE_GROUNDING = "fav_grounding";
    public static final String KEY_FIRST_LAUNCH = "is_first_launch";
    
    public static final String KEY_FAVORITE_METHOD_STR = "fav_method_string";

    public static void init(Context context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            editor = prefs.edit();
        }
    }

    
    public static void setBoolean(String key, boolean value) {
        if (editor != null) {
            editor.putBoolean(key, value).apply();
        }
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        if (prefs == null) return defaultValue;
        return prefs.getBoolean(key, defaultValue);
    }

    
    public static void setInt(String key, int value) {
        if (editor != null) {
            editor.putInt(key, value).apply();
        }
    }

    public static int getInt(String key, int defaultValue) {
        if (prefs == null) return defaultValue;
        return prefs.getInt(key, defaultValue);
    }

    
    public static void setString(String key, String value) {
        if (editor != null) {
            editor.putString(key, value).apply();
        }
    }

    public static String getString(String key, String defaultValue) {
        if (prefs == null) return defaultValue;
        return prefs.getString(key, defaultValue);
    }
}