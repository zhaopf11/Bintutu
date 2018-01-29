package com.zhurui.bunnymall.utils;

import android.content.Context;
import android.text.TextUtils;

import com.zhurui.bunnymall.home.bean.ScreenObject;
import com.zhurui.bunnymall.mine.bean.User;


public class UserLocalData {


    public static void putUser(Context context, User user){


        String user_json =  JSONUtil.toJSON(user);
        PreferencesUtils.putString(context, Contants.USER_JSON,user_json);

    }

    public static void putToken(Context context, String token){

        PreferencesUtils.putString(context, Contants.TOKEN,token);
    }


    public static void putProperty(Context context,ScreenObject screenObject){
        String property_json = JSONUtil.toJSON(screenObject);
        PreferencesUtils.putString(context, Contants.PROPERTY_JSON,property_json);
    }
    public static User getUser(Context context){

        String user_json= PreferencesUtils.getString(context,Contants.USER_JSON);
        if(!TextUtils.isEmpty(user_json)){

            return  JSONUtil.fromJson(user_json,User.class);
        }
        return  new User();
    }

    public static String getToken(Context context){

        return  PreferencesUtils.getString( context,Contants.TOKEN);

    }

    public static ScreenObject getScreeObj(Context context){

        String property_json = PreferencesUtils.getString(context,Contants.PROPERTY_JSON);
        if(!TextUtils.isEmpty(property_json)){

            return  JSONUtil.fromJson(property_json,ScreenObject.class);
        }
        return  new ScreenObject();
    }


    public static void clearUser(Context context){


        PreferencesUtils.putString(context, Contants.USER_JSON,"");

    }

    public static void clearToken(Context context){

        PreferencesUtils.putString(context, Contants.TOKEN,"");
    }

    public static void clearScreeObject(Context context){
        PreferencesUtils.putString(context, Contants.PROPERTY_JSON,"");
    }

}
