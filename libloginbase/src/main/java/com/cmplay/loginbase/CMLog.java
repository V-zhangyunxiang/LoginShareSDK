package com.cmplay.loginbase;

import android.util.Log;

/**
 * Created by Administrator on 2017/4/19.
 */
public class CMLog {
    public static boolean isDebug = false;
    private static String TAG="zyx";
    public static void e(String msg){
        if(isDebug){
            Log.e(TAG, msg);
        }
    }
    public static void d(String msg){
        if(isDebug){
            Log.d(TAG, msg);
        }
    }
    public static void v(String msg){
        if(isDebug){
            Log.v(TAG, msg);
        }
    }
    public static void w(String msg){
        if(isDebug){
            Log.w(TAG, msg);
        }
    }
}
