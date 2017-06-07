package com.cmplay.util;

/**
 * Created by Administrator on 2017/4/11.
 */
public class UsersInfo {
    private String id;
    private String name;
    private String imgUrl;
    private static  UsersInfo ui=new UsersInfo();
    public static UsersInfo getInstance() {
        return ui;
    }
    private UsersInfo()
    {
        //私有构造函数
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    //    private SharedPreferences sp;
    //    public String getId(Context context){
    //       sp =context.getSharedPreferences("userinfo",Activity.MODE_PRIVATE);
    //       String id=sp.getString("id","001");
    //       return id;
    //    }
    //    public String getName(Context context){
    //        sp =context.getSharedPreferences("userinfo",Activity.MODE_PRIVATE);
    //        String name=sp.getString("name","CMT");
    //        return name;
    //    }
    //    public String getImgUrl(Context context){
    //        sp =context.getSharedPreferences("userinfo",Activity.MODE_PRIVATE);
    //        String imgUrl=sp.getString("imgUrl","http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=猎豹图标&step_word=&hs=0&pn=2&spn=0&di=187760730960&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=4113446063%2C4223846105&os=1805588120%2C3136053683&simid=4184492263%2C631399551&adpicid=0&lpn=0&ln=1954&fr=&fmq=1491810941181_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0136285544c8ff0000019ae927fd0c.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bzv55s_z%26e3Bv54_z%26e3BvgAzdH3Fo56hAzdH3FZMzvxNzA8N2%3D%3D_z%26e3Bip4s%3FfotpviPw2j%3D5g&gsm=0&rpstart=0&rpnum=0");
    //        return imgUrl;
    //    }

}
