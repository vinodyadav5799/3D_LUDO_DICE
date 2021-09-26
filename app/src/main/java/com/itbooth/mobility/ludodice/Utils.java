package com.itbooth.mobility.ludodice;

import android.content.Intent;

import java.util.Random;

public class Utils {

    public static int generateRandomNumber() {
        Random rand = new Random();
        //int randomNum = rand.nextInt((max - min) + min) + min;
        int randomNum = rand.nextInt(6) + 1;
        return randomNum;
    }

}
