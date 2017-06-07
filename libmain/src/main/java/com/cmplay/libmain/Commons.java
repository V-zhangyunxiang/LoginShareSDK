package com.cmplay.libmain;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by zyx on 2017/3/28.
 */
public class Commons {
    /**
     * 根据包名判断是否安装
     * @param packageName
     * @return
     */
    public static boolean isHasPackage(Context c, String packageName) {
        if (null == c || null == packageName)
            return false;

        boolean bHas = true;
        try {
            c.getPackageManager().getPackageInfo(packageName, PackageManager.GET_GIDS);
        } catch (/* NameNotFoundException */Exception e) {
            // 抛出找不到的异常，说明该程序已经被卸载
            bHas = false;
        }
        return bHas;
    }
}
