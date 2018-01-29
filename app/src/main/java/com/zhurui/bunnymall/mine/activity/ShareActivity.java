package com.zhurui.bunnymall.mine.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.utils.ShareUtils;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        text_title.setText(R.string.share_to_friend);
        text_right.setVisibility(View.GONE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    @OnClick(R.id.lin_qq)
    public void shareQq(){
        if(Utils.isPkgInstalled(ShareActivity.this,"com.tencent.mobileqq")){
            ShareUtils.shareWeb(this, "http://www.bintutu.com/toAppStore.html", "宾兔兔定制", "欢迎使用宾兔兔定制","", SHARE_MEDIA.QQ);
        }else{
            ToastUtils.show(this,"未安装QQ");
        }
    }

    @OnClick(R.id.lin_wechat)
    public void shareWechat(){
        if(Utils.isPkgInstalled(ShareActivity.this,"com.tencent.mm")){
            ShareUtils.shareWeb(this, "http://www.bintutu.com/toAppStore.html", "宾兔兔定制", "欢迎使用宾兔兔定制","", SHARE_MEDIA.WEIXIN);
        }else{
            ToastUtils.show(this,"未安装微信");
        }
    }

    @OnClick(R.id.lin_friend)
    public void shareFriend(){
        if(Utils.isPkgInstalled(ShareActivity.this,"com.tencent.mm")){
            ShareUtils.shareWeb(this, "http://www.bintutu.com/toAppStore.html", "宾兔兔定制", "欢迎使用宾兔兔定制","", SHARE_MEDIA.WEIXIN_CIRCLE);
        }else{
            ToastUtils.show(this,"未安装微信");
        }
    }

    @OnClick(R.id.lin_sina)
    public void shareSina(){
        if(Utils.isPkgInstalled(ShareActivity.this,"com.sina.weibo")){
            ShareUtils.shareWeb(this, "http://www.bintutu.com/toAppStore.html","宾兔兔定制", "欢迎使用宾兔兔定制","", SHARE_MEDIA.SINA);
        }else{
            ToastUtils.show(this,"未安装新浪微博");
        }
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
