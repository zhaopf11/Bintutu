package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.home.bean.ChimaListBean;
import com.zhurui.bunnymall.home.bean.CustomGroupProperty;
import com.zhurui.bunnymall.home.bean.ProListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaopf on 2017/10/25 0025.
 */

public class ProductAttrbuteAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<ChimaListBean> chimaList;
    private List<ProListBean> proList;
    private String string;
    private Context context;
    public ProductAttrbuteAdapter(Context context,String str,List<ChimaListBean> chimaList,List<ProListBean> proList) {
        mInflater = LayoutInflater.from(context);
        this.string = str;
        this.chimaList = chimaList;
        this.proList = proList;
        this.context = context;
    }

    @Override
    public int getCount() {
        int size = 0;
        if("1".equals(string)){
            size = chimaList.size();
        }else{
            size = proList.size();
        }
        return size;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.adapter_product_attrbute, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if("1".equals(string)){
            if(chimaList != null && chimaList.size() >0){
                ChimaListBean chima  = chimaList.get(position);
                if(chima != null){
                    viewHolder.text_attr.setText(chima.getName());
                }
            }
        }else{
            if(proList != null && proList.size() >0){
                ProListBean pro = proList.get(position);
                if(pro != null){
                    viewHolder.text_attr.setText( pro.getName());
                }
            }
        }
        return convertView;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text_attr;
        public ViewHolder(View view) {
            super(view);
            text_attr = (TextView)view.findViewById(R.id.text_attr);
        }
    }
}
