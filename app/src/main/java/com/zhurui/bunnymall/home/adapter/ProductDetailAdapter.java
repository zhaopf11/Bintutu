package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.utils.Contants;

import java.util.List;
import java.util.logging.ConsoleHandler;

/**
 * Created by zhoux on 2017/7/28.
 */

public class ProductDetailAdapter extends RecyclerView.Adapter<ProductDetailAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private Context context;
    private ImageLoader imageLoader;

    public List<String> imageUrlList;

    public ProductDetailAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.imageLoader = BaseApplication.getInstance().getImageLoader();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView img_product;

        public ViewHolder(View view) {
            super(view);
            img_product = (NetworkImageView) view.findViewById(R.id.img_product);
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

        View view = mInflater.inflate(R.layout.adapter_product_img,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.img_product.setImageUrl(Contants.BASE_IMGURL + imageUrlList.get(position), imageLoader);

    }

    @Override
    public int getItemCount() {
        return null == imageUrlList ? 0 : imageUrlList.size();
//        return 5;
    }
}
