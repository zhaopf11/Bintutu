package com.zhurui.bunnymall.mine.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.mine.bean.CollectionsStoreBean;
import com.zhurui.bunnymall.utils.Contants;

import java.util.List;

/**
 * Created by zhoux on 2017/7/21.
 */

public class StoreCollectionsAdapter extends RecyclerView.Adapter<StoreCollectionsAdapter.ViewHolder> {



    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    public List<CollectionsStoreBean> mCollectionsStoreList;
    private ImageLoader imageLoader;
    private Context context;
    public StoreCollectionsAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView text_store_state;
        TextView text_storename;
        NetworkImageView img_store;
        NetworkImageView img_store1;
        NetworkImageView img_store2;
        NetworkImageView img_store3;
        ImageView img_diamond1;
        ImageView img_diamond2;
        ImageView img_diamond3;
        ImageView img_diamond4;
        ImageView img_diamond5;
        RecyclerView recycler_product_iamge;
        public ViewHolder(View view)
        {
            super(view);
            text_store_state =(TextView)view.findViewById(R.id.text_store_state);
            text_storename =(TextView)view.findViewById(R.id.text_storename);
            img_store =(NetworkImageView)view.findViewById(R.id.img_store);
//            img_store1 =(NetworkImageView)view.findViewById(R.id.img_store1);
//            img_store2 =(NetworkImageView)view.findViewById(R.id.img_store2);
//            img_store3 =(NetworkImageView)view.findViewById(R.id.img_store3);
            img_diamond1 =(ImageView)view.findViewById(R.id.img_diamond1);
            img_diamond2 =(ImageView)view.findViewById(R.id.img_diamond2);
            img_diamond3 =(ImageView)view.findViewById(R.id.img_diamond3);
            img_diamond4 =(ImageView)view.findViewById(R.id.img_diamond4);
            img_diamond5 =(ImageView)view.findViewById(R.id.img_diamond5);
            recycler_product_iamge =(RecyclerView)view.findViewById(R.id.recycler_product_iamge);
        }


    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }



    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.adapter_store_collections,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mCollectionsStoreList !=null && mCollectionsStoreList.size() > 0){
            CollectionsStoreBean mCollectionsStore = mCollectionsStoreList.get(position);
            String imageStr = mCollectionsStore.getProductList();

            if(!TextUtils.isEmpty(imageStr)){
                String[] imageList = imageStr.split("[, ]");
                if(imageList !=null){
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    holder.recycler_product_iamge.setLayoutManager(linearLayoutManager);
                    StorePctureAdapter storePctureAdapter = new StorePctureAdapter(context,imageList);
                    holder.recycler_product_iamge.setAdapter(storePctureAdapter);
//                    if(imageList.length == 1){
//                        holder.img_store1.setImageUrl(Contants.BASE_IMGURL + imageList[0],imageLoader);
//                        holder.img_store2.setVisibility(View.GONE);
//                        holder.img_store3.setVisibility(View.GONE);
//                    }else if(imageList.length == 2){
//                        holder.img_store1.setImageUrl(Contants.BASE_IMGURL + imageList[0],imageLoader);
//                        holder.img_store2.setImageUrl(Contants.BASE_IMGURL + imageList[1],imageLoader);
//                        holder.img_store3.setVisibility(View.GONE);
//                    }else if(imageList.length == 3){
//                        holder.img_store1.setImageUrl(Contants.BASE_IMGURL + imageList[0],imageLoader);
//                        holder.img_store2.setImageUrl(Contants.BASE_IMGURL + imageList[1],imageLoader);
//                        holder.img_store3.setImageUrl(Contants.BASE_IMGURL + imageList[2],imageLoader);
//                    }
                }
            }

            if(!TextUtils.isEmpty(mCollectionsStore.getLevel())){
                if("1".equals(mCollectionsStore.getLevel())){
                    holder.img_diamond1.setVisibility(View.VISIBLE);
                    holder.img_diamond2.setVisibility(View.GONE);
                    holder.img_diamond3.setVisibility(View.GONE);
                    holder.img_diamond4.setVisibility(View.GONE);
                    holder.img_diamond5.setVisibility(View.GONE);
                }else if("2".equals(mCollectionsStore.getLevel())){
                    holder.img_diamond1.setVisibility(View.VISIBLE);
                    holder.img_diamond2.setVisibility(View.VISIBLE);
                    holder.img_diamond3.setVisibility(View.GONE);
                    holder.img_diamond4.setVisibility(View.GONE);
                    holder.img_diamond5.setVisibility(View.GONE);
                }else if("3".equals(mCollectionsStore.getLevel())){
                    holder.img_diamond1.setVisibility(View.VISIBLE);
                    holder.img_diamond2.setVisibility(View.VISIBLE);
                    holder.img_diamond3.setVisibility(View.VISIBLE);
                    holder.img_diamond4.setVisibility(View.GONE);
                    holder.img_diamond5.setVisibility(View.GONE);
                }else if("4".equals(mCollectionsStore.getLevel())){
                    holder.img_diamond1.setVisibility(View.VISIBLE);
                    holder.img_diamond2.setVisibility(View.VISIBLE);
                    holder.img_diamond3.setVisibility(View.VISIBLE);
                    holder.img_diamond4.setVisibility(View.VISIBLE);
                    holder.img_diamond5.setVisibility(View.GONE);
                }else if("5".equals(mCollectionsStore.getLevel()) || Integer.parseInt(mCollectionsStore.getLevel()) > 5){
                    holder.img_diamond1.setVisibility(View.VISIBLE);
                    holder.img_diamond2.setVisibility(View.VISIBLE);
                    holder.img_diamond3.setVisibility(View.VISIBLE);
                    holder.img_diamond4.setVisibility(View.VISIBLE);
                    holder.img_diamond5.setVisibility(View.VISIBLE);
                }
            }else{
                holder.img_diamond1.setVisibility(View.GONE);
                holder.img_diamond2.setVisibility(View.GONE);
                holder.img_diamond3.setVisibility(View.GONE);
                holder.img_diamond4.setVisibility(View.GONE);
                holder.img_diamond5.setVisibility(View.GONE);
            }

            holder.text_storename.setText(mCollectionsStore == null ? "" : mCollectionsStore.getName());
//            holder.text_productprice.setText(mCollectionsStore == null ? "" : "￥" + mCollectionsStore.getPrice());
            holder.img_store.setImageUrl(Contants.BASE_IMGURL + mCollectionsStore.getLogo(),imageLoader);
//            holder.img_store.setImageURI("http://www.bintutu.com/uploadFile/" + mCollectionsStore.getLogo());
        }
        if(null !=mOnItemClickLitener){
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
        return null == mCollectionsStoreList ? 0 : mCollectionsStoreList.size();
    }
}
