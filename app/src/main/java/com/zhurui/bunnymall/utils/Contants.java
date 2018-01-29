package com.zhurui.bunnymall.utils;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;

import java.util.List;

public class Contants {

    // 企业
    public static String siteid = "au_1000";// 示例kf_9979,kf_8002,kf_3004,zf_1000,yy_1000
    public static String sdkkey = "FEA65C0A-7517-439B-BBFA-B776F9772501";// 示例FB7677EF-00AC-169D-1CAD-DEDA35F9C07B

    public static final String USER_JSON = "user_json";
    public static final String TOKEN = "token";
    public static final String PROPERTY_JSON ="property_json";
    public static final String sinkey = "jggJ9WGj)f(#j)(#*TJ";

//    public static String BASE_URL = "http://115.29.165.57:8080/appserver/appserver";//服务器地址
//    public static  String  BASE_URL ="http://192.168.10.81:80/bintutuserverdemo1/appserver";

//    public static String UPLOAD_URL = "http://115.29.165.57:8080/appfileserver/httpserver";//上传图片
//    public static String IMAGE_BASEURL = "http://115.29.165.57:8080/appfileserver/"; //分类图片
//    public static String IMAGE_HEADURL = "http://115.29.165.57:8080/appfileserver/upload/";//读取图片
//    public static String PRODUCT_DETAIL_URL="http://115.29.165.57:8080/appserver/";//商品详情地址
    //3D地址
    public static String THIRD_URL="http://www.othkin.com/CustomizedService/Bintutu/IsProductExisting.php?id=";
    public static String THIRD_REQUEST_URL="http://www.othkin.com/CustomizedService/Bintutu/Index.html?id=";

    public static String PRODUCT_SHARE_URL="http://api.bintutu.com/KHApi/h5/detail.html?productId=";//分享地址
    public static String BASE_IMGURL = "http://www.bintutu.com/uploadFile/";//所有商品的图片

    //生产环境
    public static String BASE_URL = "http://api.bintutu.com/KHApi/appserver";//接口地址
    public static String UPLOAD_URL = "http://api.bintutu.com/KHAppUpload/httpserver";//上传图片
    public static String IMAGE_HEADURL = "http://appimage.bintutu.com/";//读取图片
    public static String IMAGE_BASEURL = "http://appimage.bintutu.com/KHAppUpload/"; //分类图片
    public static String PRODUCT_DETAIL_URL="http://api.bintutu.com/KHApi/";//商品详情地址


//    本地环境
//    public static String BASE_URL = "http://192.168.0.127:8081/KHApi/appserver";//接口地址
//    public static String UPLOAD_URL = "http://192.168.0.127:8081/KHAppUpload/httpserver";//上传图片
//    public static String IMAGE_HEADURL = "http://192.168.0.127:8081/KHAppUpload/upload/";//读取图片
//    public static String IMAGE_BASEURL = "http://192.168.0.127:8081/KHAppUpload/"; //分类图片
//    public static String PRODUCT_DETAIL_URL="http://192.168.0.127:8081/KHApi/";//商品详情地址

    //双庆
//    public static String BASE_URL = "http://192.168.0.210:8081/KHApi/appserver";//接口地址
//    public static String UPLOAD_URL = "http://192.168.0.210:8081/KHAppUpload/httpserver";//上传图片
//    public static String IMAGE_HEADURL = "http://192.168.0.210:8081/KHAppUpload/upload/";//读取图片
//    public static String IMAGE_BASEURL = "http://192.168.0.210:8081/KHAppUpload/"; //分类图片
//    public static String PRODUCT_DETAIL_URL="http://192.168.0.210:8081/KHApi/";//商品详情地址
//    public static String BANNER_IMAGE="http://shoesWWW.atb2c.com:8080/uploadFile/";//商品详情地址


    public static int beforePosition =0;
    public static Drawable headDrawable = null;
    //是否已经判断过是否要更新
    public static volatile  boolean isFirstUpdate = true;
    public static boolean isClassify= false;
    public static boolean WECHAT_PAY=false;
    public static float WXPAY_NUMBER=0;
    public static boolean isRecharge = false;
    public static boolean paySuccess = false;

    public static String shopOrderID ;
    public static List<String> shopOrderIDList ;

    public static class API {
    }
}
