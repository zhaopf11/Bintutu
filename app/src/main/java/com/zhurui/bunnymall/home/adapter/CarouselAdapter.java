package com.zhurui.bunnymall.home.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.bean.CarouselBean;
import com.zhurui.bunnymall.utils.Contants;

import java.util.List;

/**
 * Created by zhoux on 2017/8/21.
 */

public class CarouselAdapter extends StaticPagerAdapter {
    private ImageLoader imageLoader;
    public List<CarouselBean> carouselBeen;

//    int[] imglist = new int[]{R.drawable.aa,R.drawable.bb,R.drawable.cc,R.drawable.dd};
    public CarouselAdapter(){
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
    }

    @Override
    public View getView(ViewGroup container, int position) {
        NetworkImageView view = new NetworkImageView(container.getContext());
        view.setImageUrl(Contants.BASE_IMGURL+carouselBeen.get(position).getBannerImage(),imageLoader);
        view.setDefaultImageResId(R.drawable.detail_normal);
        view.setErrorImageResId(R.drawable.detail_normal);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getCount() {
        return null ==carouselBeen?0:carouselBeen.size();
//        return 4;
    }


}
