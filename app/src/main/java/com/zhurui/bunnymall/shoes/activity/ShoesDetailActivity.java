package com.zhurui.bunnymall.shoes.activity;


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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.home.activity.NoticeWebViewActivity;
import com.zhurui.bunnymall.home.activity.ProductNewDetailActivity;
import com.zhurui.bunnymall.home.activity.StoreHomeActivity;
import com.zhurui.bunnymall.mine.activity.MyWalletActivity;
import com.zhurui.bunnymall.mine.msg.WalletRespMsg;
import com.zhurui.bunnymall.shoes.bean.ShoesListBean;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ShareUtils;
import com.zhurui.bunnymall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhaopf on 2017/12/14.
 */

public class ShoesDetailActivity extends BaseActivity {
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.webview)
    WebView webView;
    @Bind(R.id.progressBar1)
    ProgressBar pg1;
    private ShoesListBean shoesBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoes_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        shoesBean = (ShoesListBean) getIntent().getSerializableExtra("shoesBean");
        if (shoesBean != null) {
            if ("1013".equals(shoesBean.getInfoTypeID())) {
                text_title.setText("匠人访谈");
            } else if ("1014".equals(shoesBean.getInfoTypeID())) {
                text_title.setText("鞋闻趣事");
            }
//            text_title.setText(shoesBean.getTitle());
        }
        text_right.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        showWebView(shoesBean);
    }

    /**
     * 展示webview
     * @param shoesBean
     */
    private void showWebView(ShoesListBean shoesBean) {
        if(!TextUtils.isEmpty(shoesBean.getUrl())){
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setAllowFileAccess(false);
            webView.getSettings().setJavaScriptEnabled(true);// 设置与Js交互的权限
            webView.addJavascriptInterface(new AndroidtoJs(), "aj");
            webView.loadUrl(Contants.PRODUCT_DETAIL_URL + shoesBean.getUrl());
            webView.setWebViewClient(new WebViewClient(){
                //覆写shouldOverrideUrlLoading实现内部显示网页
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            WebSettings seting=webView.getSettings();
            seting.setJavaScriptEnabled(true);//设置webview支持javascript脚本
            webView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    pg1.setVisibility(View.GONE);//开始加载网页时显示进度条
                    pg1.setProgress(newProgress);//设置进度值
                    if(newProgress==100){
                        pg1.setVisibility(View.GONE);//加载完网页进度条消失
                    }
                }
            });
        }else{
            ToastUtils.show(BaseApplication.getInstance(), "地址有误，请稍后重试");
        }
    }

    // 继承自Object类
    public class AndroidtoJs extends Object {

        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        Handler handler = new Handler();

        @JavascriptInterface
        public void goToStore(final String supplierid) {
            handler.post(new Runnable() {
                public void run() {
                    // todo 对传递过来的参数进行处理
                    Intent intent = new Intent(BaseApplication.getInstance(), StoreHomeActivity.class);
                    intent.putExtra("supplierid", supplierid);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * 分享功能
     */
    @OnClick(R.id.imageView)
    public void share() {
        String shareUrl = Contants.PRODUCT_DETAIL_URL + shoesBean.getShareUrl();
        ShareUtils.openShare(this, shoesBean.getTitle(), shareUrl, shoesBean.getImage(), shoesBean.getSubTitle());
    }

    @OnClick(R.id.imgbtn_back)
    public void back() {
        finish();
    }
}
