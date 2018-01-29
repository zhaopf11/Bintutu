package com.zhurui.bunnymall.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.mine.activity.RegisterActivity;
import com.zhurui.bunnymall.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoticeWebViewActivity extends BaseActivity {

    @Bind(R.id.webview)
    WebView webView;
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtn_back;
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.rel_layout)
    RelativeLayout rel_layout;
    @Bind(R.id.progressBar1)
    ProgressBar pg1;
    private String messagetype;
    private String contentStr;
    private String url;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_webview);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        text_right.setVisibility(View.GONE);
        messagetype = getIntent().getStringExtra("messagetype");
        boolean isproductH5 = getIntent().getBooleanExtra("isproductH5",false);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        if(!TextUtils.isEmpty(title)){
            text_title.setText(title);
        }
        text_title.setVisibility(View.VISIBLE);
        if(isproductH5){
            rel_layout.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(url)){
            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setAllowFileAccess(false);
            webView.getSettings().setJavaScriptEnabled(true);// 设置与Js交互的权限
            webView.addJavascriptInterface(new AndroidtoJs(), "aj");
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient(){
                //覆写shouldOverrideUrlLoading实现内部显示网页
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO 自动生成的方法存根
                    view.loadUrl(url);
                    return true;
                }
            });
            WebSettings seting=webView.getSettings();
            seting.setJavaScriptEnabled(true);//设置webview支持javascript脚本
            webView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    // TODO 自动生成的方法存根
                    pg1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pg1.setProgress(newProgress);//设置进度值
                    if(newProgress==100){
                        pg1.setVisibility(View.GONE);//加载完网页进度条消失
                    }
                }
            });

        }else{
            ToastUtils.show(NoticeWebViewActivity.this,"地址有误，请稍后重试");
        }
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }

    // 继承自Object类
    public class AndroidtoJs extends Object {

        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        Handler handler = new Handler();

        @JavascriptInterface
        public void runOnAndroidJavaScript(final String flag,final String productId) {
            handler.post(new Runnable() {
                public void run() {
                    // todo 对传递过来的参数进行处理
                    Intent intent = new Intent(BaseApplication.getInstance(),ProductNewDetailActivity.class);
                    intent.putExtra("productType",flag);
                    intent.putExtra("productId",Long.parseLong(productId));
                    startActivity(intent);
                }
            });
        }

        @JavascriptInterface
        public void goToRegister() {
            handler.post(new Runnable() {
                public void run() {
                    Intent intent = new Intent(BaseApplication.getInstance(),RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }

        @JavascriptInterface
        public void seeMoreToGoodList(final String smallTypeId2) {
            handler.post(new Runnable() {
                public void run() {
                    Intent intent = new Intent(BaseApplication.getInstance(),ProductGridNormalActivity.class);
                    intent.putExtra("smalltypeid2",smallTypeId2);
                    startActivity(intent);
                }
            });
        }
    }
}
