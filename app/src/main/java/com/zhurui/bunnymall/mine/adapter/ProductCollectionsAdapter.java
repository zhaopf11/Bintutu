package com.zhurui.bunnymall.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.mine.bean.CollectionsProductBean;
import com.zhurui.bunnymall.utils.Contants;

import java.util.List;

/**
 * Created by zhoux on 2017/7/21.
 */

public class ProductCollectionsAdapter extends RecyclerView.Adapter<ProductCollectionsAdapter.ViewHolder> {



    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    public List<CollectionsProductBean> mCollectionsProducList;
    private ImageLoader imageLoader;

    public ProductCollectionsAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView text_findsimilarity;
        TextView text_productprice;
        TextView text_productintroduce;
        NetworkImageView img_product;
        public ViewHolder(View view)
        {
            super(view);
            text_findsimilarity =(TextView)view.findViewById(R.id.text_findsimilarity);
            text_productprice =(TextView)view.findViewById(R.id.text_productprice);
            text_productintroduce =(TextView)view.findViewById(R.id.text_productintroduce);
            img_product =(NetworkImageView)view.findViewById(R.id.img_product);
        }


    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemSalimilarity(int position);
    }



    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.adapter_collection_product,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mCollectionsProducList !=null && mCollectionsProducList.size() >0){
            CollectionsProductBean mCollectionsProduc = mCollectionsProducList.get(position);
            holder.text_productintroduce.setText(mCollectionsProduc == null ? "" : mCollectionsProduc.getName());
            holder.text_productprice.setText(mCollectionsProduc == null ? "" : "¥" + mCollectionsProduc.getPrice());
            holder.img_product.setImageUrl(Contants.BASE_IMGURL + mCollectionsProduc.getMainImage(),imageLoader);
        }

        if(null !=mOnItemClickLitener){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });

            holder.text_findsimilarity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemSalimilarity(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null == mCollectionsProducList ? 0 : mCollectionsProducList.size();
    }
}
