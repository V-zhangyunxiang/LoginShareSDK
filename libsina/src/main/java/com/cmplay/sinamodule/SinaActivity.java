package com.cmplay.sinamodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;

import com.cmplay.loginbase.CMLog;
import com.cmplay.util.UsersInfo;
import com.cmplay.util.Util;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * created by zhangyunxiang on 2017/3/6
 */
public class SinaActivity extends Activity implements IWeiboHandler.Response{
    private static final int THUMB_SIZE = 150;
    public static String APP_KEY = null;
    public static String REDIRECT_URL = null;
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
    private static IWeiboShareAPI mWeiboShareAPI;
    private Oauth2AccessToken mAccessToken;
    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    //private static IUserInfo iUserInfo;
    private static Context mContext;
    private static SinaActivity sa;
    private String idstr,avatarHd,name;
     public static SinaActivity getInstance(Context context){
          mContext=context;
         //创建微博分享接口实例
         if(sa==null) {
             sa = new SinaActivity();
             CMLog.d("sa:"+sa);
         }
         try {
             ApplicationInfo appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
             APP_KEY=appInfo.metaData.getString("SINA_APPKEY").substring(3);
             REDIRECT_URL=appInfo.metaData.getString("SINA_URL");
             CMLog.d("APP_KEY:"+APP_KEY);
             CMLog.d("REDIRECT_URL:"+REDIRECT_URL);
             mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mContext,APP_KEY);
             CMLog.d("mWeiboShareAPI:"+mWeiboShareAPI);
             mWeiboShareAPI.registerApp();
         } catch (PackageManager.NameNotFoundException e) {
             e.printStackTrace();
         }
         return sa;
     }
//    public void setiUserInfo(IUserInfo iUserInfo){
//        this.iUserInfo=iUserInfo;
//    }
    //初始化微博登录SDK
    public void initWBSDK(){
        //快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mAuthInfo = new AuthInfo(mContext, APP_KEY, REDIRECT_URL, SCOPE);
        mSsoHandler = new SsoHandler((Activity)mContext, mAuthInfo);
    }
    //登录微博
    public void SinaLogin(){
        //SSO 授权, ALL IN ONE 如果手机安装了微博客户端则使用客户端授权,没有则进行网页授权

        mSsoHandler.authorize(new AuthListener());

    }
    /**
     * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
     * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
     * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
     * SharedPreferences 中。
     */
    private class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {

            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // 保存Token到SharedPreferences(暂时不这样做)
                AccessTokenKeeper.writeAccessToken(mContext, mAccessToken);
               // Toast.makeText(mContext, R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show();

                UsersAPI usersAPI=new UsersAPI(mContext,APP_KEY,mAccessToken);
                usersAPI.show(Long.valueOf(mAccessToken.getUid()),new SinaRequestListener());
            } else {
                /* 以下几种情况，您会收到 Code：
                 1. 当您未在平台上注册的应用程序的包名与签名时；
                 2. 当您注册的应用程序包名与签名不正确时；
                 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。*/
                String code = values.getString("code");
                //String message = getString(R.string.weibosdk_demo_toast_auth_failed);
                if (!TextUtils.isEmpty(code)) {
                  //  message = message + "\nObtained the code: " + code;
                }
                //Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
           // Toast.makeText(mContext, R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            //Toast.makeText(mContext, "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private class SinaRequestListener implements RequestListener{

        @Override
        public void onComplete(String s) {
            try {
                JSONObject jsonObject=new JSONObject(s);
                 idstr=jsonObject.getString("idstr");//唯一标识符(uid)
                 name = jsonObject.getString("name");//姓名
                 avatarHd = jsonObject.getString("avatar_hd");//头像地址

                 UsersInfo.getInstance().setId(idstr);
                 UsersInfo.getInstance().setName(name);
                 UsersInfo.getInstance().setImgUrl(avatarHd);
//                if(iUserInfo!=null){
//                    iUserInfo.setUserInfo(name,avatarHd,idstr);
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
//            Log.i("logi", "Auth exception : " + e.getMessage());
        }
    }
    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     * TODO 如果没有重写此方法，授权可能失败
     * @see {@linkActivity#onActivityResult}
     */
    //@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 新浪微博分享图片
     * @param
     */
    public void shareImageWeibo(String path){

        WeiboMessage weiboMessage = new WeiboMessage();
        ImageObject imageObject = new ImageObject();
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap bitmap=BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);//缩略图大小
        bitmap.recycle();
        Bitmap thumbData=Util.bmpToByteArray(thumbBmp, true); // 设置缩略图
        imageObject.setImageObject(thumbData);
        thumbData.recycle();
        weiboMessage.mediaObject = imageObject;

        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;
        mWeiboShareAPI.sendRequest((Activity) mContext, request);
    }

    /**
     * 新浪微博分享网页链接
     * @param
     */
    public void shareUrlWeibo(String url,String title,String description,String path){

        WeiboMultiMessage  weiboMessage=new WeiboMultiMessage();
        TextObject textObject = new TextObject();
        textObject.text = title+"，"+description+"。"+url;
        weiboMessage. textObject = textObject;

        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);//缩略图大小
        bitmap.recycle();
        Bitmap thumbData=Util.bmpToByteArray(thumbBmp, true); // 设置缩略图
        imageObject.setImageObject(thumbData);
        thumbData.recycle();
        weiboMessage.imageObject = imageObject;
//      WebpageObject webpageObject=new WebpageObject();
//      webpageObject.identify= Utility.generateGUID();//这个ID不能忘，且必须要写，不然无法分享网页
//      webpageObject.title=title;
//      webpageObject.description=description;
//      webpageObject.actionUrl=url;
//      webpageObject.setThumbImage(bitmap);
//      weiboMessage.mediaObject=webpageObject;
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        mWeiboShareAPI.sendRequest((Activity) mContext, request);
    }
    /**
     * 点击分享后的回调函数
     * @param
     */
    @Override
    public void onResponse(BaseResponse baseResponse) {
        if (baseResponse != null) {
            switch (baseResponse.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
//                    Toast.makeText(mContext, "成功", Toast.LENGTH_SHORT).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
//                    Toast.makeText(mContext, "取消", Toast.LENGTH_SHORT).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
//                    Toast.makeText(mContext, "Error Message: " + baseResponse.errMsg,
//                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

//    @Override
    protected void onNewIntent(Intent intent) {
        // super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }
}
