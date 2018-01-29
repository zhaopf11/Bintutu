package com.zhurui.bunnymall.mine.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.utils.Contants;

import java.util.List;

/**
 * Created by zhaopf on 2017/9/11 0011.
 */

public class StorePctureAdapter extends RecyclerView.Adapter<StorePctureAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private String[] iamgeList;
    private ImageLoader imageLoader;
    public StorePctureAdapter(Context context, String[] imageList) {
        mInflater = LayoutInflater.from(context);
        this.iamgeList = imageList;
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
    }

    @Override
    public StorePctureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_store_picture,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StorePctureAdapter.ViewHolder holder, int position) {
        holder.store_iamge.setImageUrl(Contants.BASE_IMGURL + iamgeList[position],imageLoader);
    }

    @Override
    public int getItemCount() {
        return iamgeList == null ? 0 : iamgeList.length;
    }

    class  ViewHolder extends RecyclerView.ViewHolder{
        NetworkImageView store_iamge;
        public ViewHolder(View itemView) {
            super(itemView);
            store_iamge =(NetworkImageView)itemView.findViewById(R.id.store_iamge);

        }
    }
}
