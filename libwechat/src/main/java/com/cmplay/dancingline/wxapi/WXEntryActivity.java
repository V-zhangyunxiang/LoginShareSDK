package com.cmplay.dancingline.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.cmplay.util.UsersInfo;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zyx.wechatmodule.WxLoginShare;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by zhangyunxiang on 2017/3/2.
 * 这个类所在的包名必须跟你在微信开放平台上所填的包名一致,即使Build.gradle的id是一致的也不行
 * 并且一定是在package+wxapi包下，类名也必须是WXEntryActivity
 * 清单文件中的声明不能忘写(注意包名)
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    //private static IUserInfo iUserInfo;
    private String nickname,headimgurl,openId;
    private String data = null;
    private String url;
    private String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //接收微信回调结果时,要带上第三个布尔参数
        api= WXAPIFactory.createWXAPI(this, WxLoginShare.APP_ID,false);
        api.registerApp(WxLoginShare.APP_ID);
        //如果分享的时候，该界面没有开启，那么微信开始这个activity时，会调用onCreate，所以这里要处理微信的返回结果
        api.handleIntent(getIntent(), this);
    }
//    public  void setiUserInfo(IUserInfo iUserInfo){
//        this.iUserInfo=iUserInfo;
//    }
    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq baseReq) {

    }
    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode){
            case BaseResp.ErrCode.ERR_OK:
                if(baseResp instanceof SendAuth.Resp){
                    SendAuth.Resp sendResp = (SendAuth.Resp) baseResp;
                    //获取微信传回的code
                    String code = sendResp.code;
                    url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+WxLoginShare.APP_ID+"&secret="+WxLoginShare.APP_SERCET+"&code="+code+"&grant_type=authorization_code";
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String data=getURL(url);
                                JSONObject json = new JSONObject(data);
                                String access_token=json.getString("access_token");
                                openId=json.getString("openid");

                                String url="https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openId;
                                String useData=getURL(url);
                                JSONObject json2=new JSONObject(useData);
                                nickname=json2.getString("nickname");
                                headimgurl=json2.getString("headimgurl");

                                UsersInfo.getInstance().setId(openId);
                                UsersInfo.getInstance().setName(nickname);
                                UsersInfo.getInstance().setImgUrl(headimgurl);
//                                if(iUserInfo!=null) {
//                                    iUserInfo.setUserInfo(nickname, headimgurl, openId);
//                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                    finish();
                }else {
                    //result="分享成功";
                    //Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                finish();
                break;
            default:
                break;
        }

    }
    //加载网络文本
    public String getURL(final String strUrl) {
        InputStream is = null;
        HttpURLConnection httpCon = null;
        try {
            // 将地址转化为URL
            URL url = new URL(strUrl);
            // 打开链接
            httpCon = (HttpURLConnection) url.openConnection();
            // 设置超时时间
            httpCon.setConnectTimeout(5000);
            // 获取字节流
            is = httpCon.getInputStream();
            // 将字节流转化为字符流
            InputStreamReader reader = new InputStreamReader(is);
            // 设置缓冲区
            BufferedReader buffere = new BufferedReader(reader);
            // 取数据
            String str;
            StringBuffer sb = new StringBuffer();
            while ((str = buffere.readLine()) != null) {
                sb.append(str);// 连接字符
            }
            data = sb.toString();
        } catch (MalformedURLException e) {
            //System.out.println("网页无法显示");
            e.printStackTrace();
        } catch (IOException e) {
            //System.out.println("网络异常");
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                httpCon.disconnect();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return data;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //如果分享的时候，该已经开启，那么微信开始这个activity时，会调用onNewIntent，所以这里要处理微信的返回结果
        setIntent(intent);
        api.handleIntent(intent, this);
    }
}
