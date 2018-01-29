package com.zhurui.bunnymall.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zhurui.bunnymall.R;

/**
 * Created by zhoux on 2017/7/24.
 */

public class FootMessageAdapter extends RecyclerView.Adapter<FootMessageAdapter.ViewHolder> {



    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    public boolean isEdit = false;

    public FootMessageAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        CheckBox checkbox_foot;

        public ViewHolder(View view)
        {
            super(view);
            checkbox_foot = (CheckBox) view.findViewById(R.id.checkbox_foot);
        }


    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void isChecked(boolean isChecked,int position);
    }



    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.adapter_foot_message,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(null !=mOnItemClickLitener){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });

            holder.checkbox_foot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mOnItemClickLitener.isChecked(isChecked,position);
                }
            });
        }

        if(isEdit){
            holder.checkbox_foot.setVisibility(View.VISIBLE);
            holder.checkbox_foot.setChecked(false);
        }else {
            holder.checkbox_foot.setVisibility(View.GONE);

        }



    }

    @Override
    public int getItemCount() {
        return 5;
    }
}

