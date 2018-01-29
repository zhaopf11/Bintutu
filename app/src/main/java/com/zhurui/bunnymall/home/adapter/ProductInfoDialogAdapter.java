package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.home.bean.PropertyGroupBean;

import java.util.List;

/**
 * Created by zhoux on 2017/7/28.
 */

public class ProductInfoDialogAdapter extends RecyclerView.Adapter<ProductInfoDialogAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private Context context;
    public List<PropertyGroupBean> propertyGroupBeen;
    public List<String> valueList;

    public ProductInfoDialogAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_infotitle;
        TextView text_productinfo;

        public ViewHolder(View view) {
            super(view);
            text_infotitle = (TextView) view.findViewById(R.id.text_infotitle);
            text_productinfo = (TextView) view.findViewById(R.id.text_productinfo);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.adapter_dialog_productinfo,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text_infotitle.setText(propertyGroupBeen.get(position).getName());
        holder.text_productinfo.setText(valueList.get(position));
    }

    @Override
    public int getItemCount() {
        return null== propertyGroupBeen?0:propertyGroupBeen.size();
    }
}

