package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.home.bean.SmallStoreClassifyBean;

import java.util.List;

/**
 * Created by zhoux on 2017/7/19.
 */

public class StoreProductClassifyGirdAdapter extends BaseAdapter {



    private LayoutInflater mInflater;
    private Context context ;

    public List<SmallStoreClassifyBean> smallStoreClassifyBeen;
    public StoreProductClassifyGirdAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_productname;
        public ViewHolder(View view) {
            super(view);
            text_productname = (TextView)view.findViewById(R.id.text_productname);
        }
    }

    @Override
    public int getCount() {
        return null ==smallStoreClassifyBeen?0:smallStoreClassifyBeen.size();
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
        SmallStoreClassifyBean storeClassifyBean = smallStoreClassifyBeen.get(position);
        ViewHolder viewHolder = null;
        if(null == convertView){
            convertView = mInflater.inflate(R.layout.adapter_store_productclassify_grid,
                    parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text_productname.setText(storeClassifyBean.getName());
        return convertView;
    }


}

