package com.zhurui.bunnymall.home.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.bean.BannerInfo;
import com.zhurui.bunnymall.utils.Contants;

import java.util.List;

/**
 * Created by zhoux on 2017/7/31.
 */

public class TestNormalAdapter extends StaticPagerAdapter {
    private ImageLoader imageLoader;
    private List<BannerInfo> bannerInfos;

    public TestNormalAdapter(List<BannerInfo> bannerInfos){
        this.bannerInfos = bannerInfos;
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
    }

    @Override
    public View getView(ViewGroup container, int position) {

//
//        if(0 == position){
//            WebView webView = new WebView(container.getContext());
//            webView.getSettings().setLoadWithOverviewMode(true);
//            webView.getSettings().setUseWideViewPort(true);
//            webView.getSettings().setJavaScriptEnabled(true);
//            webView.getSettings().setLoadsImagesAutomatically(true);
//            webView.loadUrl("http://www.othkin.com/Test/H5Portal/B22/Index.html?id=67787");
//            return webView;
//        }else {
            NetworkImageView view = new NetworkImageView(container.getContext());
            view.setImageUrl(Contants.BASE_IMGURL+bannerInfos.get(position).getBannerImage(),imageLoader);
            view.setDefaultImageResId(R.drawable.banner_normal);
            view.setErrorImageResId(R.drawable.banner_normal);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
//        }


    }


    @Override
    public int getCount() {
        return bannerInfos.size();
    }

}
