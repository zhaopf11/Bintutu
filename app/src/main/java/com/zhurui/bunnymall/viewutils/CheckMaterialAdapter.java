package com.zhurui.bunnymall.viewutils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.viewutils.bean.CheckMaterialSortBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaopf on 2018/1/11.
 */

public class CheckMaterialAdapter extends BaseAdapter {
    private Context context;
    public List<CheckMaterialSortBean> checkMaterialList = new ArrayList<>();
    private LayoutInflater mInflater;
    public CheckMaterialAdapter(Context context, List<CheckMaterialSortBean> checkMaterialList) {
        this.context = context;
        this.checkMaterialList = checkMaterialList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(checkMaterialList != null){
            return checkMaterialList.size();
        }else{
            return 0;
        }
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
        if(null ==convertView){
            convertView = mInflater.inflate(R.layout.adapter_item,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CheckMaterialSortBean checkMaterialSortBean  = checkMaterialList.get(position);
        viewHolder.textView.setText(checkMaterialSortBean.getName());
        return convertView;
    }

    class ViewHolder{
        TextView textView;
        public ViewHolder(View view) {
           textView = (TextView) view.findViewById(R.id.textview);
        }
    }
}
