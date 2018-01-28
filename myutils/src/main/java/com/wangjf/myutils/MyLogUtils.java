package com.wangjf.myutils;

import android.util.Log;

/**
 * Created by wangjf on 18-1-20.
 */

public class MyLogUtils {
    final static String TAG = "WJF";
    final static boolean LOG_E = true;
    final static boolean LOG_I = true;
    final static boolean LOG_D = true;
    final static boolean LOG_V = true;
    final static boolean LOG_W = true;

    private MyLogUtils(){
        /* Protect from instantiations */
    }

    public static void e(String message){
        if (!LOG_E)return;

        Log.e(TAG,message);
    }


    public static void i(String message){
        if (!LOG_I)
            return;

        Log.i(TAG, message);
    }

    public static void d(String message){
        if (!LOG_D)
            return;

        Log.d(TAG, message);
    }

    public static void v(String message){
        if (!LOG_V)
            return;

        Log.v(TAG, message);
    }

    public static void w(String message){
        if (!LOG_W)
            return;

        Log.w(TAG, message);
    }

}
