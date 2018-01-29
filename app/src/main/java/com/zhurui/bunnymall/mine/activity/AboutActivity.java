package com.zhurui.bunnymall.mine.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhurui.bunnymall.MainActivity;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.text_version)
    TextView text_version;
    @Bind(R.id.text_title)
    TextView text_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_our);
        ButterKnife.bind(this);
        text_right.setVisibility(View.GONE);
        text_title.setText("关注宾兔兔");
        String versionName = "";
        int versioncode;
        try {
            PackageManager pm = BaseApplication.getInstance().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(BaseApplication.getInstance().getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
            if (!TextUtils.isEmpty(versionName)) {
                text_version.setText("宾兔兔 V" + versionName);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }

}
