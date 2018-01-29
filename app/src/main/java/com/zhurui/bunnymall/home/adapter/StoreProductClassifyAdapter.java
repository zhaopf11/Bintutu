package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.home.bean.BigStoreClassifyBean;
import com.zhurui.bunnymall.home.bean.SmallStoreClassifyBean;
import com.zhurui.bunnymall.viewutils.GridViewInScrollView;

import java.util.List;
import java.util.Map;

/**
 * Created by zhoux on 2017/7/19.
 */

public class StoreProductClassifyAdapter extends RecyclerView.Adapter<StoreProductClassifyAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private StoreProductClassifyGirdAdapter gridAdapter;
    private Context context;

    public List<BigStoreClassifyBean> bigStoreClassifyBeen;
    public Map<String,List<SmallStoreClassifyBean>> listMap;

    public StoreProductClassifyAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
        this.context = context;

    }

     class ViewHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rel_allproduct;
        GridViewInScrollView grid_product;
        TextView text_bigclassify;
        public ViewHolder(View view)
        {
            super(view);
            rel_allproduct = (RelativeLayout) view.findViewById(R.id.rel_allproduct);
            grid_product = (GridViewInScrollView)view.findViewById(R.id.grid_product);
            text_bigclassify = (TextView)view.findViewById(R.id.text_bigclassify);
        }


    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, String  smalltypeId);
        void onGridItemClick(View view,String smalltypeId,String fashionId);

    }



    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_store_productclassify,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        BigStoreClassifyBean bigStoreClassifyBean = bigStoreClassifyBeen.get(position);
        List<SmallStoreClassifyBean> smallStoreClassifyBeen = listMap.get(bigStoreClassifyBean.getSmallTypeID());
        holder.text_bigclassify.setText(bigStoreClassifyBean.getName());
        gridAdapter = new StoreProductClassifyGirdAdapter(context);
        holder.grid_product.setAdapter(gridAdapter);

        gridAdapter.smallStoreClassifyBeen = smallStoreClassifyBeen;
        gridAdapter.notifyDataSetChanged();
        if(null !=mOnItemClickLitener){
            holder.rel_allproduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, bigStoreClassifyBean.getSmallTypeID());
                }
            });
            holder.grid_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int childposition, long id) {
                    mOnItemClickLitener.onGridItemClick(holder.itemView,bigStoreClassifyBean.getSmallTypeID(),smallStoreClassifyBeen.get(childposition).getFashionID());
                }
            });
        }




    }

    @Override
    public int getItemCount() {
        return null ==bigStoreClassifyBeen?0:bigStoreClassifyBeen.size();
    }
}

