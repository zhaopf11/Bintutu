package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.home.bean.LogisticsDetailMessageBean;

import java.util.List;

/**
 * Created by zhoux on 2017/7/20.
 */

public class LogisticalDetailAdapter extends RecyclerView.Adapter<LogisticalDetailAdapter.ViewHolder> {



    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private Context context;
    public List<LogisticsDetailMessageBean> mList;

    public LogisticalDetailAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        View view_up;
        ImageView img_state;
        View view_down;
        TextView text_info;
        TextView txt_date;
        public ViewHolder(View view)
        {
            super(view);
            view_up = (View)view.findViewById(R.id.view_up);
            img_state = (ImageView)view.findViewById(R.id.img_state);
            view_down = (View)view.findViewById(R.id.view_down);
            text_info = (TextView)view.findViewById(R.id.text_info);
            txt_date = (TextView)view.findViewById(R.id.txt_date);

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

        View view = mInflater.inflate(R.layout.adapter_logistical_detail,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mList != null && mList.size() > 0){
            LogisticsDetailMessageBean logisticsMessageBean = mList.get(position);
            if(logisticsMessageBean != null){
                holder.text_info.setText(logisticsMessageBean.getContext());
                holder.txt_date.setText(logisticsMessageBean.getFtime());
            }
        }
        if(0==position){
            holder.view_down.setVisibility(View.VISIBLE);
            holder.view_up.setVisibility(View.VISIBLE);
        }else if(position == (getItemCount()-1)){
            holder.view_up.setVisibility(View.INVISIBLE);
            holder.view_down.setVisibility(View.VISIBLE);
            holder.img_state.setImageResource(R.drawable.dot_checked);
            holder.text_info.setTextColor(context.getResources().getColor(R.color.color_d2ba91));
            holder.text_info.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.font_30));
        }else {
            holder.view_up.setVisibility(View.VISIBLE);
            holder.view_down.setVisibility(View.VISIBLE);
        }

//        if(null !=mOnItemClickLitener){
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickLitener.onItemClick(holder.itemView, position);
//                }
//            });
//        }

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
}

