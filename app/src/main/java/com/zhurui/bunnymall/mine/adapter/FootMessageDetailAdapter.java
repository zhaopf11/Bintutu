package com.zhurui.bunnymall.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.mine.bean.UserFootDataListBean;

import java.util.List;

/**
 * Created by zhoux on 2017/7/24.
 */

public class FootMessageDetailAdapter extends RecyclerView.Adapter<FootMessageDetailAdapter.ViewHolder> {



    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private String[]  data = new String[]{"脚长","跖围","跗围","兜跟围","背围垂直围度(舟骨)","大拇趾趾尖高度","脚腕围度","跗围高度","舟上弯点高度","外踝骨高度","后跟凸点高度"};
    public List<UserFootDataListBean> useFootDataList;
    public FootMessageDetailAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView text_name;
        TextView text_num;
        TextView text_leftfoot;
        TextView text_rightfoot;
        public ViewHolder(View view)
        {
            super(view);
            text_name =(TextView)view.findViewById(R.id.text_name);
            text_num =(TextView)view.findViewById(R.id.text_num);
            text_leftfoot =(TextView)view.findViewById(R.id.text_leftfoot);
            text_rightfoot =(TextView)view.findViewById(R.id.text_rightfoot);
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

        View view = mInflater.inflate(R.layout.adapter_footmessage_detail,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(useFootDataList != null && useFootDataList.size() > 0){
            UserFootDataListBean userFootDataListBean = useFootDataList.get(position);
            holder.text_name.setText(userFootDataListBean == null ? "" : userFootDataListBean.getFootDataParaName());
            holder.text_num.setText((position+1)+"");
            holder.text_leftfoot.setText(userFootDataListBean == null ? "" : userFootDataListBean.getLeftData());
            holder.text_rightfoot.setText(userFootDataListBean == null ? "" : userFootDataListBean.getRightData());
        }
        if (null != mOnItemClickLitener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
//        return null ==data?0:data.length;
        return null == useFootDataList ? 0 : useFootDataList.size();
    }
}

