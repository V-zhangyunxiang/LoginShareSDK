package com.cmplay.libmain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cmplay.facebookdemo.FaceBook;
import com.cmplay.loginbase.CMLog;
import com.cmplay.qqmodule.QQLoginShare;
import com.cmplay.sinamodule.SinaActivity;
import com.zyx.wechatmodule.WxLoginShare;

import java.util.ArrayList;

public class CMPShareUtils extends Activity{
    private QQLoginShare qq;
    private WxLoginShare wx;
    private SinaActivity sa;
    private FaceBook fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }
    public static void isDebug(){
        CMLog.isDebug=true;
    }
    public void shareToQQ(Context context, String title, String des, String targetUrl, String imgUrl){
        if(context!=null) {
            Intent intent = new Intent(context, CMPShareUtils.class);
            context.startActivity(intent);
            CMLog.d("Has started CMPShareUtils");
        }
        qq= QQLoginShare.getInstance(context);
        qq.shareToQQ((Activity)context,title,des,targetUrl,imgUrl);

    }
    public  void shareToQZone(Context context,String title,String des,String targetUrl,String imgUrl){
        if(context!=null) {
            Intent intent = new Intent(context, CMPShareUtils.class);
            context.startActivity(intent);
            CMLog.d("Has started CMPShareUtils");
        }
        ArrayList<String> list=new ArrayList<>();
        list.add(imgUrl);
        qq= QQLoginShare.getInstance(context);
        qq.shareToQZone((Activity)context,title,des,targetUrl,list);
    }
    public  void ShareWechatUrl(Context context,String targetUrl,String title,String des,int scene,String localpath){
        if(context!=null) {
            Intent intent = new Intent(context, CMPShareUtils.class);
            context.startActivity(intent);
            CMLog.d("Has started CMPShareUtils");
        }
       wx= WxLoginShare.getInstance(context);
       wx.shareUrlWx(targetUrl,title,des,scene,localpath);
    }
    public  void ShareWechatImage(Context context,int scene,String localpath) {
        if(context!=null) {
            Intent intent = new Intent(context, CMPShareUtils.class);
            context.startActivity(intent);
            CMLog.d("Has started CMPShareUtils");
        }
        wx= WxLoginShare.getInstance(context);
        wx.shareImageWx(scene,localpath);
    }
    public  void shareImageWeibo(Context context,String localpath){
        if(context!=null) {
            Intent intent = new Intent(context, CMPShareUtils.class);
            context.startActivity(intent);
            CMLog.d("Has started CMPShareUtils");
        }
        sa=SinaActivity.getInstance(context);
        sa.shareImageWeibo(localpath);
    }
    public  void shareUrlWeibo(Context context,String targetUrl,String title,String des,String localpath){
        if(context!=null) {
            Intent intent = new Intent(context, CMPShareUtils.class);
            context.startActivity(intent);
            CMLog.d("Has started CMPShareUtils");
        }
        sa=SinaActivity.getInstance(context);
        sa.shareUrlWeibo(targetUrl,title,des, localpath);
    }

    public  void shareImgFb(Context context,String localpath){
        fb=FaceBook.getInstance(context);
        fb.initShare();
        fb.shareImgFb(localpath);
    }
    public  void shareUrlFb(Context context,String title,String des,String targetUrl,String imgUrl){
        fb=FaceBook.getInstance(context);
        fb.initShare();
        fb.shareUrlFb(title,des,targetUrl,imgUrl);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(qq!=null){
           qq.onActivityResult(requestCode,resultCode,data);
        }
        if(sa!=null){
            sa.onActivityResult(requestCode,resultCode,data);
        }
        if(fb!=null){
            fb.onActivityResult(requestCode,resultCode,data);

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


}
