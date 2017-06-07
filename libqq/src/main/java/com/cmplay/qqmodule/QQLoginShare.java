package com.cmplay.qqmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.cmplay.loginbase.CMLog;
import com.cmplay.loginbase.ILogin;
import com.cmplay.util.UsersInfo;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * created by zhangyunxiang on 2017/3/6
 */
public class QQLoginShare implements ILogin {
    public static Tencent mTencent;
    public static String mAppid;//1105044612 钢琴块2
    public static Context mContext=null;
    public  Context loginContext=null;
    private String nickname,icon,openId;
    //private IUserInfo iUserInfo;
    private static QQLoginShare qq;

    public static QQLoginShare getInstance(Context context){
        mContext=context;
        if(qq==null) {
             qq = new QQLoginShare();
        }
       try {
           ApplicationInfo appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
           mAppid=appInfo.metaData.getString("QQ_APPKEY").substring(3);
           CMLog.d("QQ ID:"+mAppid);
      } catch (PackageManager.NameNotFoundException e) {
           e.printStackTrace();
      }
       mTencent=null;
       CMLog.d("qq:"+qq);
       return qq;
    }
//    public void setiUserInfo(IUserInfo iUserInfo){
//        this.iUserInfo=iUserInfo;
//
//    }
    //分享到QQ空间
    public void shareToQZone(Activity context, String title, String summary, String targetUrl, ArrayList<String> list) {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(mAppid, context);
            CMLog.d("mTencent:"+mTencent);
        }
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE,title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,summary);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,targetUrl);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,list);
        //本地地址的话一定要传sdcard路径，不要什么getCacheDir()或getFilesDir()
        //params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, Environment.getExternalStorageDirectory().getAbsolutePath().concat("/a.png"));
        //params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN); //打开这句话，可以实现分享纯图到QQ空间
        mTencent.shareToQzone(context,params,qqShareListener);
    }

    IUiListener qqLoginListener =new IUiListener() {
        @Override
        public void onComplete(Object o) {
            initOpenidAndToken((JSONObject)o);
            updateUserInfo();
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    };
    //使用QQ三方登录
    public void qqLogin(Context context){
        loginContext=context;
        if (mTencent == null) {
            mTencent = Tencent.createInstance(mAppid,context);
        }
         mTencent.login((Activity)context, "all",qqLoginListener);
    }
    /**
     * @Title: initOpenidAndToken
     * @Description: 初始化OPENID以及TOKEN身份验证。
     * @param @param jsonObject
     * @return void
     * @throws
     */
    private void initOpenidAndToken (JSONObject jsonObject) {

        try {
            //这里的Constants类，是 com.tencent.connect.common.Constants类，下面的几个参数也是固定的
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN );
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN );
            //OPENID,作为唯一身份标识
             openId = jsonObject.getString(Constants.PARAM_OPEN_ID );
            if (!TextUtils. isEmpty(token) && !TextUtils.isEmpty(expires)&& !TextUtils. isEmpty(openId)) {
                //设置身份的token
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);

            }
        } catch (Exception e) {

        }
    }
    private void  updateUserInfo(){
        if ( mTencent != null && mTencent.isSessionValid()) {
            UserInfo mInfo = new UserInfo(loginContext, mTencent.getQQToken());
            mInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    try {
                        JSONObject json = new JSONObject(o.toString());
                        nickname = json.optString("nickname");
                        icon = json.optString("figureurl_qq_2");
                        //String gender = json.optString("gender");

                        UsersInfo.getInstance().setId(openId);
                        UsersInfo.getInstance().setName(nickname);
                        UsersInfo.getInstance().setImgUrl(icon);
//                        if(iUserInfo!=null) {
//                            iUserInfo.setUserInfo(nickname, icon, openId);
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            });
        }
    };
    //分享到QQ
    public void shareToQQ(Activity context, String title, String summary, String targetUrl, String imageUrl){
        if (mTencent == null) {
            mTencent = Tencent.createInstance(mAppid, context);
            CMLog.d("mTencent:"+mTencent);
        }
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE,title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,targetUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,imageUrl);
        mTencent.shareToQQ(context, params, qqShareListener);

    }
    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {

//            Toast.makeText(mContext, "取消分享",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete(Object response) {

//            Toast.makeText(mContext, "分享成功",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError e) {

//          Toast.makeText(mContext, "分享失败"+e.errorMessage,Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, qqLoginListener);
        Tencent.onActivityResultData(requestCode, resultCode, data, qqShareListener);
    }

}
