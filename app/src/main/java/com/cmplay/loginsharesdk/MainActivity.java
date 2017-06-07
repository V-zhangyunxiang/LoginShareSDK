package com.cmplay.loginsharesdk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.cmplay.libmain.CMPLoginUtils;
import com.cmplay.libmain.CMPShareUtils;

public class MainActivity extends AppCompatActivity {
    public CMPShareUtils csu;
    private TextView tv;
    private CMPLoginUtils clu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        tv = (TextView) findViewById(R.id.textView);
                if(csu==null) {
                    csu = new CMPShareUtils();

                }
//        if (clu == null) {
//            clu = new CMPLoginUtils();
//
//        }
    }

    public void Test(View view) {
       // csu.shareToQZone(this,"The piano piece of 2","Casual games","http://www.xiaopi.com/game/15027.html","http://pic.35pic.com/normal/08/52/55/3347542_124923071328_2.jpg");
        //csu.shareToQQ(this,"The piano piece of 2","Casual games","http://www.xiaopi.com/game/15027.html","http://pic.35pic.com/normal/08/52/55/3347542_124923071328_2.jpg");
       // csu.ShareWechatUrl(this,"http://www.xiaopi.com/game/15027.html","The piano piece of 2","Casual games",1,"/storage/emulated/0/add.png");
       //csu.ShareWechatImage(this,1, Environment.getExternalStorageDirectory().getAbsolutePath() + "/add.png");
        // csu.shareUrlWeibo(this,"http://www.xiaopi.com/game/15027.html","The piano piece of 2","Casual games","/storage/emulated/0/add.png");
         //csu.shareImageWeibo(this,"/storage/emulated/0/add.png");
         //csu.shareUrlFb(this,"The piano piece of 2","Casual games","http://www.xiaopi.com/game/15027.html","http://pic.35pic.com/normal/08/52/55/3347542_124923071328_2.jpg");
         //csu.shareImgFb(this,"/storage/emulated/0/abc.png");

        //clu.MqqLogin(this);
        //clu.SinaLogin(this);
        //clu.faceBookLogin(this);
        //clu.weChatLogin(this);
    }

}
