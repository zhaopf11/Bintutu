package com.zhurui.bunnymall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.OkHttpHelper;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.home.msg.ScreenResp;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.UserLocalData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends Activity {
    private Boolean isFirstIn; // 首次启动标记
    private Thread startThread;
    private OkHttpHelper mHttpHelper = new OkHttpHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initProperty();
        SharedPreferences preferences = getSharedPreferences("ISFIRSTIN",
                Context.MODE_PRIVATE);
        isFirstIn = preferences.getBoolean("isFirstIn", true);
        /** 延迟3秒然后接受判断进入不同界面 **/
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Looper.prepare();
                if (!isFirstIn) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 1000);
    }


    private void initProperty() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "39");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("curretadress","上海");
            jsonObject.put("propertiesID","1");
            params.put("sendmsg", jsonObject.toString());
            getProperty(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getProperty(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<ScreenResp>(SplashActivity.this,"first") {
            @Override
            public void onSuccess(Response response, ScreenResp screenResp) {
                if (screenResp.getResult() > 0) {
                    if (null != screenResp.getList() && screenResp.getList().size() > 0) {
                        UserLocalData.putProperty(BaseApplication.getInstance(), screenResp.getList().get(0));
                    }
                }
                dismissDialog();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                dismissDialog();
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                dismissDialog();
            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
//                dismissDialog();
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpotsCallBack.dismissDialog();
    }

    public static int getScreenHeight(Context context){
        WindowManager wm=(WindowManager)context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

}
