package com.zhurui.bunnymall.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import com.umeng.analytics.MobclickAgent;
import com.zhurui.bunnymall.common.http.OkHttpHelper;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.activity.LoginActivity;
import com.zhurui.bunnymall.mine.bean.User;
import com.zhurui.bunnymall.utils.ActivityManager;
import com.zhurui.bunnymall.viewutils.DialogUtil;

import javax.inject.Inject;


/**
 */
public class BaseActivity extends AppCompatActivity {


    private View.OnClickListener onBackListener;

    protected static final String TAG = BaseActivity.class.getSimpleName();

    @Inject
    public OkHttpHelper mHttpHelper;

    public Context mContext;

    public DialogUtil dialogUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        BaseApplication.component().inject(this);

        ActivityManager.getInstance().addActivity(this);
        dialogUtil = new DialogUtil();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().remove(this);
    }

    public void startActivity(Intent intent, boolean isNeedLogin) {


        if (isNeedLogin) {

            User user = BaseApplication.getInstance().getUser();
            if (user != null && 0!=user.getUserID() && !"".equals(BaseApplication.getInstance().getToken())) {
                super.startActivity(intent);
            } else {

                BaseApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(this
                        , LoginActivity.class);
                super.startActivity(loginIntent);

            }
//
        } else {
            super.startActivity(intent);
        }

    }


    public void startActivityForResult(Intent intent, boolean isNeedLogin, int requestCode) {


        if (isNeedLogin) {
            User user = BaseApplication.getInstance().getUser();
            if (user != null && 0!=user.getUserID() && !"".equals(BaseApplication.getInstance().getToken())) {
                super.startActivityForResult(intent,requestCode);
            } else {
                BaseApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(mContext, LoginActivity.class);
                super.startActivityForResult(loginIntent,requestCode);
            }

        } else {
            super.startActivityForResult(intent,requestCode);
        }

    }



    protected void setupToolbar(Toolbar mToolbar, boolean homeIconVisible) {
//        if (mToolbar == null) {
//            throw new RuntimeException("toolbar cannot be null!");
//        }
//        if (null == getSupportActionBar()) {
//            setSupportActionBar(mToolbar);
//        }
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(homeIconVisible);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        mToolbar.setNavigationIcon(R.drawable.fanhui);
    }

    protected void setupToolbar(Toolbar mToolbar, boolean homeIconVisible, View.OnClickListener onBackListener) {
        setupToolbar(mToolbar, homeIconVisible);
        this.onBackListener = onBackListener;
    }

    /**
     * 重写Actionbar返回上一级的动画,以及避免重复创建实例
     *
     * @param item 菜单项
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (onBackListener != null) {
                onBackListener.onClick(null);
            } else {
                finish();
            }
        }
        return false;
    }


    public void showNormalErr(BaseRespMsg respMsg) {
//        if (respMsg == null || TextUtils.isEmpty(respMsg.getMessage())) {
//            showFail();
//        } else {
//            ToastUtils.show(respMsg.getMessage());
//        }

    }

    public void showFail() {

//        if (Looper.myLooper() != Looper.getMainLooper()){
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ToastUtils.show(mContext, getString(R.string.err_net_connect));
//                }
//            });
//        }else
//            ToastUtils.show(this, getString(R.string.err_net_connect));
//    }
    }

    /**
     * 友盟统计方法
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
