package com.example.xo_game;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {

    private static final String LANGUAGE_PREF_KEY = "selected_language";
    private static final String LANGUAGE_PREF_FILE_NAME = "AppLanguagePrefs";
    private static final String SOUND_PREF_KEY = "selected_mode";
    private static final String SOUND_PREF_FILE_NAME = "AppModePrefs";

    // Method to save the selected language
    public static void saveLanguage(Context context, String language) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LANGUAGE_PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LANGUAGE_PREF_KEY, language);
        editor.apply();
    }

    // Method to get the saved language, defaulting to "en"
    public static String getLanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LANGUAGE_PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LANGUAGE_PREF_KEY, "en");
    }

    // Method to save the selected Sound mode (e.g., on mode, off mode)
    public static void saveSoundMode(Context context, Boolean mode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SOUND_PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SOUND_PREF_KEY, mode);
        editor.apply();
    }

    // Method to get the saved Sound mode, defaulting to on sound
    public static Boolean getSoundMode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SOUND_PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(SOUND_PREF_KEY, true);
    }
}