package com.zhurui.bunnymall.common;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.view.WindowManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.zhurui.bunnymall.common.dagger.BaseApplicationComponent;
import com.zhurui.bunnymall.common.dagger.DaggerBaseApplicationComponent;
import com.zhurui.bunnymall.mine.bean.User;
import com.zhurui.bunnymall.utils.BitmapCache;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.UserLocalData;
import com.zhurui.bunnymall.utils.Utils;

import java.io.File;

import cn.jpush.android.api.JPushInterface;
import cn.xiaoneng.activity.ChatActivity;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.xpush.XPush;

/**
 * Created by zhoux on 2017/7/11.
 */

public class BaseApplication extends Application {
    private final String TAG = this.getClass().getSimpleName();
    private static BaseApplicationComponent appComponent;
    private static BaseApplication mInstance;
    private User user = new User();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public static BaseApplication getInstance() {

        return mInstance;
    }

    // 使用单例模式
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    {
        PlatformConfig.setWeixin("wx310f2e849382569f","d4cce706a9dc0e12a205939e24b58c5b");
        PlatformConfig.setQQZone("1106268932","Gbzo8FYOyB3QScHG");
        PlatformConfig.setSinaWeibo("2262490527","7f89b1f521f1a5ab2883aefdc103e093","https://sns.whalecloud.com/sina2/callback");
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        SDKInitializer.initialize(getApplicationContext());
        mInstance = this;
        buildComponentAndInject();
        initUser();
        UMShareAPI.get(this);
        Config.DEBUG = true;
        Fresco.initialize(this);
        JPushInterface.setDebugMode(false);// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        //初始化内存缓存目录
        File cacheDir = new File(this.getCacheDir(), "volley");
        /** 初始化RequestQueue,其实这里你可以使用Volley.newRequestQueue来创建一个RequestQueue,直接使用构造函数可以定制我们需要的RequestQueue,比如线程池的大小等等        */
        mRequestQueue = new RequestQueue(new DiskBasedCache(cacheDir), new BasicNetwork(new HurlStack()), 3);
        BitmapCache mCache = new BitmapCache();
        //初始化ImageLoader
        mImageLoader = new ImageLoader(mRequestQueue, mCache);
        //如果调用Volley.newRequestQueue,那么下面这句可以不用调用
        mRequestQueue.start();

        int enableDebug = Ntalker.getBaseInstance().enableDebug(false);
        int initSDK = Ntalker.getBaseInstance().initSDK(getApplicationContext(), Contants.siteid, Contants.sdkkey);
        //点击通知栏跳转到的activity类，默认可传入ChatActivity.class
        XPush.setNotificationClickToActivity(this, ChatActivity.class);
        //App完全退出(被杀掉进程), 再次进入时, 在SDK初始化之后, 如果是登录用户, 需要重新调用小能SDK登录接口
        if(Utils.isLogin()){
            Ntalker.getBaseInstance().login(user.getUserID() + "", user.getNick(), 0);
        }
    }


    private void initUser() {
        this.user = UserLocalData.getUser(this);
    }


    public User getUser() {
        return user;
    }


    public void putUser(User user, String token) {
        this.user = user;
        UserLocalData.putUser(this, user);
        UserLocalData.putToken(this, token);
    }

    public void clearUser() {
        this.user = new User();
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    public String getToken() {
        String token =UserLocalData.getToken(this)+"";
        return token;
    }

    private Intent intent;

    public void putIntent(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void jumpToTargetActivity(Context context) {

        context.startActivity(intent);
        this.intent = null;
    }

    public static BaseApplicationComponent component() {
        return appComponent;
    }

    public static void buildComponentAndInject() {
        appComponent = DaggerBaseApplicationComponent.Initializer.init(mInstance);
    }


    /**
     * 获取当前手机屏幕的宽
     */
    public static int getPhoneWidth() {
        WindowManager wm = (WindowManager) mInstance.getSystemService(WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }
    /**
     * 获取当前手机屏幕的高
     */
    public static int getPhoneHeight() {
        WindowManager wm = (WindowManager) mInstance.getSystemService(WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
