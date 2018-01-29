package com.zhurui.bunnymall.mine.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhurui.bunnymall.MainActivity;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.FileCacheUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaoneng.uiapi.Ntalker;

public class SettingActivity extends BaseActivity {
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.text_size)
    TextView text_size;

    @Bind(R.id.text_openpush)
    TextView text_openpush;
    @Bind(R.id.btn_loginout)
    Button btn_loginout;

    @Bind(R.id.rel_clear)
    RelativeLayout rel_clear;
    @Bind(R.id.rel_about)
    RelativeLayout rel_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        text_title.setText(R.string.setting);
        text_right.setVisibility(View.GONE);
        try {
            text_size.setText(FileCacheUtils.getTotalCacheSize(BaseApplication.getInstance()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.rel_about)
    public void openObout(){
        startActivity(new Intent(BaseApplication.getInstance(),AboutActivity.class),false);
    }

    @OnClick(R.id.rel_clear)
    public void clear(){
        FileCacheUtils.clearAllCache(BaseApplication.getInstance());
        try {
            text_size.setText(FileCacheUtils.getTotalCacheSize(BaseApplication.getInstance()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.text_openpush)
    public void openPush(){
        //打开通知系统界面
        startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    @OnClick(R.id.rel_info_safe)
    public void evaluateApplication(){
        //给我评价
        try{
            Uri uri = Uri.parse("market://details?id="+getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch(Exception e){
            Toast.makeText(SettingActivity.this, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @OnClick(R.id.btn_loginout)
    public void loginOut(){
        BaseApplication.getInstance().clearUser();
        Contants.headDrawable = null;
        Intent intent = new Intent(BaseApplication.getInstance(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        Ntalker.getBaseInstance().logout();
        finish();
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }

//    private boolean isOpenGps() {
//        LocationManager locationManager = (LocationManager)this.
//                getSystemService(Context.);
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//    }
}
