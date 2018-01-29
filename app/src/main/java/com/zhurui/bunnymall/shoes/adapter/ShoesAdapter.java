package com.zhurui.bunnymall.shoes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.home.adapter.GuessRecommandAdapter;
import com.zhurui.bunnymall.home.adapter.HomeBannerAdapter;
import com.zhurui.bunnymall.mine.adapter.FootPrintAdapter;
import com.zhurui.bunnymall.mine.bean.FootPrintBean;
import com.zhurui.bunnymall.shoes.bean.ShoesListBean;
import com.zhurui.bunnymall.utils.Contants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaopf on 2017/12/14.
 */

public class ShoesAdapter extends RecyclerView.Adapter<ShoesAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    public List<ShoesListBean> shoesList = new ArrayList<>();
    private Context context;
    private OnItemClickLitener mOnItemClickLitener;
    public ShoesAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_shoes, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(shoesList != null && shoesList.size() > 0){
            ShoesListBean shoesListBean = shoesList.get(position);
            holder.text_title.getBackground().setAlpha(120);//0~255透明度值
            holder.text_title.setText(shoesListBean.getTitle());
            Glide.with(context).load(Contants.BASE_IMGURL+shoesListBean.getImage())
                    .placeholder(R.drawable.list_normal)
                    .error(R.drawable.list_normal)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)//是将图片原尺寸缓存到本地。
                    .into(holder.shoes_image);
            holder.shoes_image.setTag(R.id.imageloader_uri,Contants.BASE_IMGURL+shoesListBean.getImage());
            holder.shoes_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.shoes_image, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return shoesList.size();
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

//    @Override
//    public int getCount() {
//        return shoesList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder viewHolder = null;
//        if(null ==convertView){
//            convertView = mInflater.inflate(R.layout.adapter_shoes,parent,false);
//            viewHolder = new ViewHolder(convertView);
//            convertView.setTag(viewHolder);
//        }else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        if(shoesList != null && shoesList.size() > 0){
//            ShoesListBean shoesListBean = shoesList.get(position);
////            viewHolder.text_title.getBackground().setAlpha(80);//0~255透明度值
//            viewHolder.text_title.setText(shoesListBean.getTitle());
//            Glide.with(context).load(Contants.BASE_IMGURL+shoesListBean.getImage())
//                    .placeholder(R.drawable.list_normal)
//                    .error(R.drawable.list_normal)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)//是将图片原尺寸缓存到本地。
//                    .into(viewHolder.shoes_image);
//            viewHolder.shoes_image.setTag(R.id.imageloader_uri,Contants.BASE_IMGURL+shoesListBean.getImage());
//        }
//
//        return convertView;
//    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text_title;
        private ImageView shoes_image;
        public ViewHolder(View view) {
            super(view);
            shoes_image = (ImageView) view.findViewById(R.id.shoes_image);
            text_title = (TextView) view.findViewById(R.id.text_title);
        }
    }

}
