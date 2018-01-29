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
import com.zhurui.bunnymall.home.bean.NewProduct;
import com.zhurui.bunnymall.home.bean.Product;
import com.zhurui.bunnymall.home.bean.StoreProductBean;
import com.zhurui.bunnymall.utils.Contants;

import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by zhoux on 2017/7/13.
 */

public class RecommandAdapter extends RecyclerView.Adapter<RecommandAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private boolean isStore;
    private Context context;
    private List<NewProduct> newProducts;
    private ImageLoader imageLoader;
    DecimalFormat decimalFormat=new DecimalFormat("0.00");
    public List<StoreProductBean> storeProductBeen;
    public RecommandAdapter(Context context, boolean isStore, List<NewProduct> newProducts) {
        mInflater = LayoutInflater.from(context);
        this.isStore = isStore;
        this.context = context;
        this.newProducts = newProducts;
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_competitiveproduct;
        NetworkImageView img_newproduct;
        TextView text_price;

        public ViewHolder(View view) {
            super(view);
            if(isStore){
                img_newproduct = (NetworkImageView) view.findViewById(R.id.img_newproduct);
                text_price = (TextView) view.findViewById(R.id.text_price);
            }else{
                img_competitiveproduct = (ImageView) view.findViewById(R.id.img_competitiveproduct);
            }
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
        View view;
        if(isStore){
            view = mInflater.inflate(R.layout.adapter_newrecommand, parent, false);
        }else{
            view = mInflater.inflate(R.layout.adapter_competitive_products, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(isStore){
            StoreProductBean storeProductBean = storeProductBeen.get(position);
            holder.img_newproduct.setDefaultImageResId(R.drawable.new_recommand_normal);
            holder.img_newproduct.setErrorImageResId(R.drawable.new_recommand_normal);
            holder.img_newproduct.setImageUrl(Contants.BASE_IMGURL+storeProductBean.getMainImage(),imageLoader);
            holder.text_price.setText(storeProductBean.getPrice());
        }else {
            NewProduct newProduct = newProducts.get(position);
//            holder.img_competitiveproduct.setDefaultImageResId(R.drawable.new_recommand_normal);
//            holder.img_competitiveproduct.setErrorImageResId(R.drawable.new_recommand_normal);
//            holder.img_competitiveproduct.setImageUrl(Contants.BASE_IMGURL+newProduct.getImage(),imageLoader);
//            holder.text_price.setText(newProduct.getPrice());
            Glide.with(context).load(Contants.BASE_IMGURL+newProduct.getImage())
                    .placeholder(R.drawable.list_normal)
                    .error(R.drawable.list_normal)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)//是将图片原尺寸缓存到本地。
                    .into(holder.img_competitiveproduct);
            holder.img_competitiveproduct.setTag(R.id.imageloader_uri,Contants.BASE_IMGURL+newProduct.getImage());
        }

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
        if(isStore){
            return null == storeProductBeen ? 0 : storeProductBeen.size();
        }else {
            return null == newProducts ? 0 : newProducts.size();
        }
    }
}
