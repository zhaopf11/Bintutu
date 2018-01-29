package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhurui.bunnymall.R;

import java.util.List;

import butterknife.ButterKnife;

import static com.tencent.open.utils.Global.getSharedPreferences;


public class GirdDropDownAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private int checkItemPosition = 0;
    int isSeletceNum = 0;
    boolean isSeletce = false;
    public void setCheckItem(int position) {
        checkItemPosition = position;
        isSeletce = true;
        SharedPreferences pref = context.getSharedPreferences("ISSELETCED", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("isSeletceNum", position);
        editor.commit();
        notifyDataSetChanged();
    }

    public GirdDropDownAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_drop_down, null);
            viewHolder = new ViewHolder();
            viewHolder.mText = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        }
        fillValue(position, viewHolder);
        return convertView;
    }

    private void fillValue(int position, ViewHolder viewHolder) {
        viewHolder.mText.setText(list.get(position));
        SharedPreferences preferences = context.getSharedPreferences("ISSELETCED",
                Context.MODE_PRIVATE);
        isSeletceNum = preferences.getInt("isSeletceNum", 0);
        if (isSeletce) {
            if (checkItemPosition == position) {
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.color_d2ba91));
                viewHolder.mText.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.down_checked), null);
            } else {
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.gray3));
                viewHolder.mText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }else{
            if (isSeletceNum == position) {
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.color_d2ba91));
                viewHolder.mText.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.down_checked), null);
            } else {
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.gray3));
                viewHolder.mText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }
    }

     class ViewHolder {

        TextView mText;


    }
}
