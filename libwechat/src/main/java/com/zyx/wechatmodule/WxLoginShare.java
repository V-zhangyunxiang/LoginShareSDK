package com.zyx.wechatmodule;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cmplay.loginbase.CMLog;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by zhangyunxiang on 2017/3/2.
 * 这个分享的主类所在的包不需要与在微信开放平台上所填的包名一致,只要build.gradle中的id一致(在平台注册的)就可以
 * 分享的关键：签名文件,所用的签名文件必须是你注册到开放平台上的,与MD5值相一致才能分享成功
 */
public class WxLoginShare {
    private static final int THUMB_SIZE = 150;
    public  static String APP_ID=null;//微信的id
    public  static String APP_SERCET=null;//微信的密匙
    private static IWXAPI api;
    public  static Context mContext=null;
    //private static WXEntryActivity wxea;
    private static WxLoginShare wx;
    public static WxLoginShare getInstance(Context context){
        mContext=context;
        if(wx==null) {
            wx = new WxLoginShare();
            CMLog.d("wx:"+wx);
        }
        try {
            ApplicationInfo appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            APP_ID=appInfo.metaData.getString("WX_APPKEY");
            APP_SERCET=appInfo.metaData.getString("WX_SECRET");
            CMLog.d("APP_ID:"+APP_ID);
            //创建微信接口
            api= WXAPIFactory.createWXAPI(mContext,APP_ID);
            api.registerApp(APP_ID);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
            return wx;
        }
//    public static WXEntryActivity getEntryInstance(){
//        if(wxea==null) {
//            wxea = new WXEntryActivity();
//        }
//        return  wxea;
//    }

    /**
     * 微信第三方登录
     * @param
     */
    public void weChatLogin(){
        SendAuth.Req req = new SendAuth.Req();
        //授权读取用户信息
        req.scope = "snsapi_userinfo";
        //自定义信息
        req.state = "wechat_sdk_demo_test";
        //向微信发送请求
        api.sendReq(req);
    }
    /**
     * sendReq是第三方app主动发送消息给微信，发送完成之后会切回到第三方app界面。
     * sendResp是微信向第三方app请求数据，第三方app回应数据之后会切回到微信界面。
     * 分享Url地址到微信
     */
    public  void shareUrlWx(String url,String title,String description,int scene,String path){
        Bitmap bitmap= BitmapFactory.decodeFile(path);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);//缩略图大小
        msg.thumbData = Util.bmpToByteArray(thumbBmp,true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        switch (scene){
            case 0:
                req.scene=SendMessageToWX.Req.WXSceneSession;
                break;
            case 1:
                req.scene=SendMessageToWX.Req.WXSceneTimeline;
                break;
            case 2:
                req.scene=SendMessageToWX.Req.WXSceneFavorite;
                break;
        }
        api.sendReq(req);
    }
    /**
     * 分享图片到微信
     * @param
     */
    public void  shareImageWx(int scene,String path) {
        //Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        Bitmap bitmap= BitmapFactory.decodeFile(path);
        WXImageObject imgObj = new WXImageObject(bitmap);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);//缩略图大小
        bitmap.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);  // 设置缩略图

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        //三目运算符-> 当？左边的值为true时，选择:左边的结果,为false时,选择右边的结果
        /*
           scene填WXSceneSession,那么消息会发送至微信的会话内,默认为WXSceneSession
           scene填WXSceneTimeline,那么消息会发送至朋友圈
           scene填WXSceneFavorite,那么会添加到微信收藏
        */
        switch (scene){
            case 0:
                req.scene=SendMessageToWX.Req.WXSceneSession;
                break;
            case 1:
                req.scene=SendMessageToWX.Req.WXSceneTimeline;
                break;
            case 2:
                req.scene=SendMessageToWX.Req.WXSceneFavorite;
                break;
        }
        api.sendReq(req);
    }
    //transaction字段用于标识一个唯一请求
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


}
