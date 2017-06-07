package com.cmplay.loginbase;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/3/8.
 */
public class LoginHelper {
    private static WeakReference<Activity> activityWeakReference;
    public static void initActivity(Activity mainActivity){
        activityWeakReference=new WeakReference<Activity>(mainActivity);
    }
    public static Activity getWeakActivity(){
        if(activityWeakReference!=null) {
            Activity mActivity = activityWeakReference.get();
            return mActivity;
        }else{
            throw new RuntimeException("WeakActivity is recycled");
        }

    }

}
