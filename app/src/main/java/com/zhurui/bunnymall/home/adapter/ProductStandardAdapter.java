package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zhurui.bunnymall.R;

/**
 * Created by zhoux on 2017/8/3.
 */

public class ProductStandardAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;


    public ProductStandardAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 4;
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
            convertView = mInflater.inflate(R.layout.adapter_productstandard_footinfo,
                    parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {
        RadioButton radio_foottype;

        public ViewHolder(View view) {
//            radio_foottype = (RadioButton) view.findViewById(R.id.radio_foottype);
        }
    }
}
