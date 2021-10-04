package com.itbooth.mobility.ludodice;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class Utils {

    public static String countKey = "COUNT_KEY";
    public static String musicSettingKey = "MUSIC_SETTING_KEY";
    public static String vibrationSettingKey = "VIBRATION_SETTING_KEY";

    public static int generateRandomNumber() {
        Random rand = new Random();
        //int randomNum = rand.nextInt((max - min) + min) + min;
        int randomNum = rand.nextInt(6) + 1;
        return randomNum;
    }

    public static void saveDiceCount(Context context, String count) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getApplicationContext().getPackageName(), MODE_PRIVATE).edit();
        editor.putString(countKey, count);
        editor.apply();
    }

    public static String getDiceCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getApplicationContext().getPackageName(), MODE_PRIVATE);
        return prefs.getString(countKey, "");//"No name defined" is the default value.
    }

    public static void saveMusicSetting(Context context, boolean isEnabled) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getApplicationContext().getPackageName(), MODE_PRIVATE).edit();
        editor.putBoolean(musicSettingKey, isEnabled);
        editor.apply();
    }

    public static boolean getMusicSetting(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getApplicationContext().getPackageName(), MODE_PRIVATE);
        return prefs.getBoolean(musicSettingKey, true);//"No name defined" is the default value.
    }

    public static void saveVibrationSetting(Context context, boolean isEnabled) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getApplicationContext().getPackageName(), MODE_PRIVATE).edit();
        editor.putBoolean(vibrationSettingKey, isEnabled);
        editor.apply();
    }

    public static boolean getVibrationSetting(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getApplicationContext().getPackageName(), MODE_PRIVATE);
        return prefs.getBoolean(vibrationSettingKey, true);//"No name defined" is the default value.
    }
}
