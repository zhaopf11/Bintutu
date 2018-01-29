package com.zhurui.bunnymall.utils;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.mine.bean.User;

import java.util.ArrayList;
import java.util.List;


public class Utils {

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    //弹出键盘
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void showInputMethod(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * dip转px
     */
    public static int dipToPx(Context context, int dip) {
        float density = context.getResources().getDisplayMetrics().density;

        return (int) (dip * density + 0.5f);
    }

    public static void bindStrText(TextView textView, String str) {
        if (textView == null || str == null) return;
        textView.setText(str);
    }

//    public static boolean isLogin() {
//        User user = App.getInstance().getUser();
//
//        if (user != null)
//            return true;
//        else return false;
//    }
//    public static boolean login() {
//        User user = App.getInstance().getUser();
//
//        if (user != null)
//            return true;
//        else {
//            Intent loginIntent = new Intent(App.getInstance(), LoginActivity.class);
//            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            App.getInstance().startActivity(loginIntent);
//
//            return false;
//        }
//    }

    /**
     * 将字符串转成数组
     * @param strin
     * @param c
     * @return
     */
    public static String[] split(String strin, String c) {
        if (strin == null) {
            return null;
        }
        ArrayList arraylist = new ArrayList();
        int begin = 0;
        int end = 0;
        while ((begin = strin.indexOf(c, end)) != -1) {
            String s2 = strin.substring(end, begin);
            if (s2.trim().length() > 0) { //
                arraylist.add(strin.substring(end, begin));
            }
            end = begin + c.length();
        }
        if (end != strin.length()) {
            arraylist.add(strin.substring(end));
        }
        int k = arraylist.size();
        String as[] = new String[k];
        return (String[]) arraylist.subList(0, k).toArray(as);
    }

    /**
     * 将list转成string
     * @param list
     * @return
     */
    public static String listToString(List<String> list){
        StringBuilder sb =new StringBuilder();
        boolean flag=false;
        if(list != null && list.size() > 0){
            for(int i=0;i < list.size();i++){
                if (flag) {
                    sb.append(",");
                }else {
                    flag=true;
                }
                sb.append(list.get(i));
            }
        }
        return sb.toString();
    }

    public static boolean isPkgInstalled(Context context,String packageName) {
        PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /**
     * 输入框下标转化到字体的最后面
     * @param editText
     * @param str
     */
    public static void cursorToEnd(EditText editText,String str){
        if(!TextUtils.isEmpty(str)){
            editText.setSelection(str.length());
            editText.requestFocus();  //这是关键
        }
    }

    /**
     * 判断用户是否登录
     * @return
     */
    public static boolean isLogin(){
        User user = BaseApplication.getInstance().getUser();
        if (user != null && 0 != user.getUserID() && !"".equals(BaseApplication.getInstance().getToken())) {
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断当前设备是手机还是平板
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 关闭输入法的方法
     * @param context
     */
    public static void downSoftInput(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
        if (imm.isActive()) {//如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
    }
}
