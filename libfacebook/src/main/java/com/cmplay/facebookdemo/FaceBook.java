package com.cmplay.facebookdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.cmplay.loginbase.CMLog;
import com.cmplay.loginbase.ILogin;
import com.cmplay.util.UsersInfo;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;

import java.util.Arrays;

public class FaceBook  implements ILogin{
    private static CallbackManager callbackManager;
    private static Context mContext;
    private static FaceBook fb;
    //private static IUserInfo iUserInfo;
    public static ShareDialog shareDialog;
    public static FaceBook getInstance(Context context){
       mContext=context;
       FacebookSdk.sdkInitialize(mContext);
       callbackManager = CallbackManager.Factory.create();
       if(fb==null) {
            fb = new FaceBook();
       }
        CMLog.d("fb:"+fb);
       return fb;
    }
    public void initShare(){
        shareDialog = new ShareDialog((Activity)mContext);
        CMLog.d("shareDialog:"+shareDialog);
        shareDialog.registerCallback(callbackManager,shareCallback);
    }
//    public  void setiUserInfo(IUserInfo iUserInfo){
//        this.iUserInfo=iUserInfo;
//    }

    public void shareUrlFb(String title,String des,String url,String imgUrl){
        if(ShareDialog.canShow(ShareLinkContent.class)){
            ShareLinkContent.Builder shareBuilder=new ShareLinkContent.Builder();
            shareBuilder.setContentDescription(des)
                    .setContentTitle(title)
                    .setImageUrl(Uri.parse(imgUrl))
                    .setContentUrl(Uri.parse(url));
            ShareLinkContent shareLinkContent=shareBuilder.build();
            shareDialog.show(shareLinkContent);

         }

    }
    public void shareImgFb(String imgUrl){

       if(ShareDialog.canShow(SharePhotoContent.class)){
           Bitmap bitmap= BitmapFactory.decodeFile(imgUrl);
           //Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 200, 200, true);//缩略图大小
            SharePhoto photo=new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            SharePhotoContent photoContent = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            shareDialog.show(photoContent);
        }
    }
    public void fbLogin(){
      // AccessToken accessToken=AccessToken.getCurrentAccessToken();
      LoginManager.getInstance().logInWithReadPermissions((Activity)mContext, Arrays.asList("public_profile","user_friends"));

      LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
          @Override
          public void onSuccess(LoginResult loginResult) {
              //拿到token获取用户信息
              getUserInfo(loginResult.getAccessToken());

          }

          @Override
          public void onCancel() {

          }

          @Override
          public void onError(FacebookException error) {

          }
      });

  }
    public void getUserInfo(AccessToken token){
        GraphRequest request=GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object != null) {
                    String id = object.optString("id");
                    String name = object.optString("name");
                    //String gender = object.optString("gender");//性别
                    //String emali = object.optString("email");//邮箱

                    //获取用户头像
                    JSONObject object_pic = object.optJSONObject("picture");
                    JSONObject object_data = object_pic.optJSONObject("data");
                    String photo = object_data.optString("url");
                    //获取地域信息
                    String locale = object.optString("locale");   //zh_CN 代表中文简体

                    UsersInfo.getInstance().setId(id);
                    UsersInfo.getInstance().setName(name);
                    UsersInfo.getInstance().setImgUrl(photo);
//                    if(iUserInfo!=null){
//                        iUserInfo.setUserInfo(name,photo,id);
//                    }
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields","id,name,picture,locale");
        request.setParameters(parameters);
        request.executeAsync();
    }
    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private static FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("Facebook", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("Facebook", String.format("Error: %s",error.toString()));
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("HelloFacebook", "Success!");
        }
    };
}
