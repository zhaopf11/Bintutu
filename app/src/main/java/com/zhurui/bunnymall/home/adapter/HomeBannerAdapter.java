package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.bean.BannerInfo;
import com.zhurui.bunnymall.home.bean.NewProduct;
import com.zhurui.bunnymall.home.bean.StoreProductBean;
import com.zhurui.bunnymall.utils.Contants;

import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by zhaopgf on 2017/12/12.
 */

public class HomeBannerAdapter extends RecyclerView.Adapter<HomeBannerAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private Context context;
    private List<BannerInfo> bannerInfos;
    private ImageLoader imageLoader;
    public HomeBannerAdapter(Context context, boolean isStore, List<BannerInfo> bannerInfos) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.bannerInfos = bannerInfos;
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_competitiveproduct;

        public ViewHolder(View view) {
            super(view);
            img_competitiveproduct = (ImageView) view.findViewById(R.id.img_competitiveproduct);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_banner, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BannerInfo bannerInfo = bannerInfos.get(position);
//        holder.img_competitiveproduct.setDefaultImageResId(R.drawable.new_recommand_normal);
//        holder.img_competitiveproduct.setErrorImageResId(R.drawable.new_recommand_normal);
//        holder.img_competitiveproduct.setImageUrl(Contants.BASE_IMGURL+bannerInfo.getBannerImage(),imageLoader);
        Glide.with(context).load(Contants.BASE_IMGURL + bannerInfo.getBannerImage())
                .placeholder(R.drawable.list_normal)
                .error(R.drawable.list_normal)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//是将图片原尺寸缓存到本地。
                .into(holder.img_competitiveproduct);
        holder.img_competitiveproduct.setTag(R.id.imageloader_uri, Contants.BASE_IMGURL + bannerInfo.getBannerImage());

        if (null != mOnItemClickLitener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return null == bannerInfos ? 0 : bannerInfos.size();
    }
}
