package com.cmplay.libmain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cmplay.facebookdemo.FaceBook;
import com.cmplay.qqmodule.QQLoginShare;
import com.cmplay.sinamodule.SinaActivity;
import com.cmplay.util.Util;
import com.zyx.wechatmodule.WxLoginShare;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/4/1.
 */
public class CMPLoginUtils extends Activity {

    public final static String TAG = "zyx";
    private static QQLoginShare qq;
    private static WxLoginShare wx;
    private static SinaActivity sa;
    private static FaceBook fb;
    //private static WXEntryActivity wxEntryActivity;
    private static WeakReference<Activity> mActRef;

    /**
     * get the activity WeakReference
     * @return
     */
    public static Activity getActivityRef(){
        if (mActRef == null) {
            throw new RuntimeException("CMPLoginUtils haven't been start");
        }
        final Activity activity = mActRef.get();
        return activity;
    }

    public interface StartUpCallback {
        void onStartUp();
    }

    public static StartUpCallback sCallback;

    public static void starEntryActivity(Context context, StartUpCallback callback) {
        Intent intent = new Intent(context, CMPLoginUtils.class);
        Util.startActivity(context, intent);
        sCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActRef = new WeakReference<Activity>(this);
        if (null != sCallback) {
            sCallback.onStartUp();
            //sCallback = null;
        }
    }

    public  void MqqLogin(Context context){
        if(context!=null) {
            starEntryActivity(context, new StartUpCallback() {
                @Override
                public void onStartUp() {
                    Activity eEntryActivity = getActivityRef();
                    if (null != eEntryActivity) {
                        qq = QQLoginShare.getInstance(eEntryActivity);
                        //qq.setiUserInfo(CMPLoginUtils.this);
                        qq.qqLogin(eEntryActivity);
                    }
                }
            });
        }
    }
    public  void weChatLogin(Context context){
           wx= WxLoginShare.getInstance(context);
        wx.weChatLogin();
//        if(context!=null) {
//            starEntryActivity(context, new StartUpCallback() {
//                @Override
//                public void onStartUp() {
//                    Activity eEntryActivity = getActivityRef();
//                    if (null != eEntryActivity) {
//                        wx= WxLoginShare.getInstance(eEntryActivity);
////                        wxEntryActivity= WxLoginShare.getEntryInstance();
////                        wxEntryActivity.setiUserInfo(CMPLoginUtils.this);
//                        wx.weChatLogin();
//                    }
//                }
//            });
//        }

    }
    public void SinaLogin(Context context){
        if(context!=null) {
            starEntryActivity(context, new StartUpCallback() {
                @Override
                public void onStartUp() {
                    Activity eEntryActivity = getActivityRef();
                    if (null != eEntryActivity) {
                        sa=SinaActivity.getInstance(eEntryActivity);
                        sa.initWBSDK();
                        //sa.setiUserInfo(CMPLoginUtils.this);
                        sa.SinaLogin();
                    }
                }
            });
        }
    }
    public void faceBookLogin(Context context){
        if(context!=null) {
            starEntryActivity(context, new StartUpCallback() {
                @Override
                public void onStartUp() {
                    Activity eEntryActivity = getActivityRef();
                    if (null != eEntryActivity) {
                        fb=FaceBook.getInstance(eEntryActivity);
                        //fb.setiUserInfo(CMPLoginUtils.this);
                        fb.fbLogin();
                    }
                }
            });
        }

    }
    /**
     * 判断facebook是否安装
     * @param context
     * @return
     */
    public static boolean isFacebookInstalled(Context context) {
        return Commons.isHasPackage(context, "com.facebook.katana");
    }
    /**
     * 判断微信是否已经安装
     * @param context
     * @return
     */
    public static boolean isWechatInstalled(Context context) {
        return Commons.isHasPackage(context, "com.tencent.mm");
    }
    /**
     * 判断QQ是否已经安装
     * @param context
     * @return
     */
    public static boolean isQQInstalled(Context context) {
        return Commons.isHasPackage(context, "com.tencent.mobileqq");
    }
    /**
     * 判断新浪微博是否安装
     * @param context
     * @return
     */
    public static boolean isSinaWeiboInstalled(Context context) {
        return Commons.isHasPackage(context, "com.sina.weibo");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==0){
            getActivityRef().finish();
        }
        if(qq!=null){
            qq.onActivityResult(requestCode,resultCode,data);
            getActivityRef().finish();
        }
        if(sa!=null){
            sa.onActivityResult(requestCode,resultCode,data);
            getActivityRef().finish();
        }
        if(fb!=null){
            fb.onActivityResult(requestCode,resultCode,data);
            getActivityRef().finish();
        }
    }
//    @Override
//    public void setUserInfo(final String name, final String imgUrl, final String id) {
//          UsersInfo.getInstance().setId(id);
//          UsersInfo.getInstance().setName(name);
//          UsersInfo.getInstance().setImgUrl(imgUrl);
////        SharedPreferences sp=getSharedPreferences("userinfo",Activity.MODE_PRIVATE);
////        SharedPreferences.Editor ed=sp.edit();
////        ed.putString("name",name);
////        ed.putString("imgUrl",imgUrl);
////        ed.putString("id",id);
////        ed.commit();
//          getActivityRef().finish();
//
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
