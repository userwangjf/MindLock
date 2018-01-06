package com.wangjf.myutils;

import android.os.Looper;
import android.util.Log;

/**
 * Created by wangjf on 17-11-23.
 */

public class ThreadUtils {

    public static final String TAG = "ThreadUtils";

    public static boolean isInMainThread() {
        Looper myLooper = Looper.myLooper();
        Looper mainLooper = Looper.getMainLooper();
        if(myLooper == mainLooper)
            Log.i(TAG, "isInMainThread == true");
        else
            Log.i(TAG, "isInMainThread == false");
        return myLooper == mainLooper;
    }

}
