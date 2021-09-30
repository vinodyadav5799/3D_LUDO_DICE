package com.itbooth.mobility.ludodice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class Utils {

    public static String countKey = "COUNT_KEY";
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

}
