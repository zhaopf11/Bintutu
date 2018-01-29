package com.zhurui.bunnymall.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.mine.bean.CancleReasonBaen;
import com.zhurui.bunnymall.viewutils.ProvinceAdapter;

import java.util.List;

/**
 * Created by zhaopf on 2017/9/8 0008.
 */

public class CancleReasonAdapter  extends RecyclerView.Adapter<CancleReasonAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    public List<CancleReasonBaen> cancleReasonlist;
    private Context context;
    private CancleReasonBaen cancleReasonBaen;
    public CancleReasonAdapter(Context context,List<CancleReasonBaen> cancleReasonlist){
        mInflater = LayoutInflater.from(context);
        this.cancleReasonlist = cancleReasonlist;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_canclereason, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(cancleReasonlist != null && cancleReasonlist.size() > 0){
            cancleReasonBaen = cancleReasonlist.get(position);
            holder.text_canclereason.setText(cancleReasonBaen.getReansonStr());
            if(!cancleReasonBaen.getSelect()){
                holder.text_canclereason.setTextColor(context.getResources().getColor(R.color.gray3));
            }else{
                holder.text_canclereason.setTextColor(context.getResources().getColor(R.color.color_d2ba91));
            }
        }
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        holder.text_canclereason.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(cancleReasonlist!=null && cancleReasonlist.size() >0){
                    for(int i=0;i<cancleReasonlist.size();i++){
                        if (i==position) {
                            cancleReasonlist.get(i).setSelect(true);
                        }else{
                            cancleReasonlist.get(i).setSelect(false);
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cancleReasonlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private  TextView text_canclereason;
        public ViewHolder(View view) {
            super(view);
            text_canclereason = (TextView) view.findViewById(R.id.text_canclereason);
        }
    }
}
