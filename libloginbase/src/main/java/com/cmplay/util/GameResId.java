package com.cmplay.util;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by i on 2016/11/29.
 */
public class GameResId {
    //TODO 存在问题：根据context获取的包名和模块包名不一致，导致获取不了
    public static int getItemId(Context context, String paramString1,
                                String paramString2) {
        String pkg = context.getPackageName();
        try {
            Class<?> localClass = Class.forName(context.getPackageName()
                    + ".R$" + paramString1);
            Field localField = localClass.getField(paramString2);
            int i = Integer.parseInt(localField.get(localField.getName())
                    .toString());
            return i;
        } catch (Exception localException) {
            Log.e("getIdByReflection error", localException.getMessage());
        }
        return 0;
    }

    /**
     * 根据给定的类型名和字段名，返回R文件中的字段的值
     * @param pkgName 包名
     * @param typeName 属于哪个类别的属性 （id,layout,drawable,string,color,attr......）
     * @param fieldName 字段名
     * @return 字段的值
     */
    public static int getResFieldValue( String pkgName, String typeName, String fieldName){
        int i = -1;
        try {
            Class<?> clazz = Class.forName(pkgName + ".R$"+typeName);
            i = clazz.getField(fieldName).getInt(null);
        } catch (Exception e) {
            Log.d("GameResId", "没有找到" + pkgName + ".R$" + typeName + "类型资源 " + fieldName + "请copy相应文件到对应的目录.");
            return -1;
        }
        return i;
    }

    public static int[] getItemIdArray(Context context,
                                       String paramString1, String paramString2) {   //TODO 包名和模块包名不一致，导致获取不了
        String pkg = context.getPackageName();
        try {
            Class<?> localClass = Class.forName(context.getPackageName()+ ".R$" + paramString1);
            Field localField = localClass.getField(paramString2);
            int[] i = (int[]) localField.get(localField.getName().toString());
            return i;
        } catch (Exception localException) {
            Log.e("getIdByReflection error", localException.getMessage());
        }
        return null;
    }

    public static int[] getItemIdArray(String pkgName,
                                       String paramString1, String paramString2) {
        try {
            Class<?> localClass = Class.forName(pkgName + ".R$" + paramString1);
            Field localField = localClass.getField(paramString2);
            int[] i = (int[]) localField.get(localField.getName().toString());
            return i;
        } catch (Exception localException) {
            Log.e("getIdByReflection error", localException.getMessage());
        }
        return null;
    }

    public static int getLayoutResIDByName(Context context, String name) {
        return context.getResources().getIdentifier(name, "layout",
                context.getPackageName());
    }
    public static int getIdResIDByName(Context context, String name) {
        return context.getResources().getIdentifier(name, "id",
                context.getPackageName());
    }
    public static int getStringResIDByName(Context context, String name) {
        return context.getResources().getIdentifier(name, "string",
                context.getPackageName());
    }
    public static int getDrawableResIDByName(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable",
                context.getPackageName());
    }
    public static int getDimenResIDByName(Context context, String name) {
        return context.getResources().getIdentifier(name, "dimen",
                context.getPackageName());
    }
    public static int getColorResIDByName(Context context, String name) {
        return context.getResources().getIdentifier(name, "color",
                context.getPackageName());
    }
    public static int getAnimResIDByName(Context context, String name) {
        return context.getResources().getIdentifier(name, "anim",
                context.getPackageName());
    }
    public static int getStyleResIDByName(Context context, String name) {
        return context.getResources().getIdentifier(name, "style",
                context.getPackageName());
    }
}