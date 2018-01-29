package com.zhurui.bunnymall.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.home.bean.CardBean;
import com.zhurui.bunnymall.mine.bean.CouponBean;

import java.util.List;

/**
 * Created by zhoux on 2017/7/26.
 */

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.ViewHolder> {



    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    public int COUPON_STATE = -1;
    private Context context;
    public int choosePosition = -1;
//    public List<CardBean> cardBeen;
    public List<CouponBean> couponBeans;

    public CouponsAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView text_unit;
        TextView text_money;
        TextView text_coupontype;
        TextView text_coupondata;
        TextView text_couponcontent;
        ImageView img_isused;
        ImageView img_choose;
        public ViewHolder(View view)
        {
            super(view);
            text_unit=(TextView)view.findViewById(R.id.text_unit);
            text_money=(TextView)view.findViewById(R.id.text_money);
            text_coupontype=(TextView)view.findViewById(R.id.text_coupontype);
            text_coupondata=(TextView)view.findViewById(R.id.text_coupondata);
            text_couponcontent=(TextView)view.findViewById(R.id.text_couponcontent);
            img_isused=(ImageView)view.findViewById(R.id.img_isused);
            img_choose=(ImageView)view.findViewById(R.id.img_choose);

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

        View view = mInflater.inflate(R.layout.adapter_coupons_noused,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CouponBean couponBean = couponBeans.get(position);
        holder.text_money.setText(couponBean.getMoney());
        holder.text_coupontype.setText(couponBean.getC_cardSourceTypeName());
        holder.text_coupondata.setText(couponBean.getAddTime().split(" ")[0]+" - "+couponBean.getDeadDate());
        holder.text_couponcontent.setText(couponBean.getTitle());
        switch (COUPON_STATE){
            case 0:
                holder.text_unit.setTextColor(context.getResources().getColor(R.color.color_d2ba91));
                holder.text_money.setTextColor(context.getResources().getColor(R.color.color_d2ba91));
                holder.text_coupontype.setTextColor(context.getResources().getColor(R.color.gray3));
                holder.img_isused.setVisibility(View.GONE);

                break;
            case 1:
                holder.text_unit.setTextColor(context.getResources().getColor(R.color.gray));
                holder.text_money.setTextColor(context.getResources().getColor(R.color.gray));
                holder.text_coupontype.setTextColor(context.getResources().getColor(R.color.gray));
                holder.img_isused.setVisibility(View.VISIBLE);
                holder.img_isused.setImageResource(R.drawable.coupon_hasused);
                break;
            case 2:
                holder.text_unit.setTextColor(context.getResources().getColor(R.color.gray));
                holder.text_money.setTextColor(context.getResources().getColor(R.color.gray));
                holder.text_coupontype.setTextColor(context.getResources().getColor(R.color.gray));
                holder.img_isused.setVisibility(View.VISIBLE);
                holder.img_isused.setImageResource(R.drawable.coupon_haspassed);
                break;
            case 3:
                holder.text_unit.setTextColor(context.getResources().getColor(R.color.color_d2ba91));
                holder.text_money.setTextColor(context.getResources().getColor(R.color.color_d2ba91));
                holder.text_coupontype.setTextColor(context.getResources().getColor(R.color.gray3));
                holder.img_isused.setVisibility(View.GONE);
                holder.img_choose.setVisibility(View.GONE);
                if(choosePosition == position){
                    holder.img_choose.setVisibility(View.VISIBLE);
                }

                break;
        }
        if(null !=mOnItemClickLitener){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                    if(3==COUPON_STATE){
                        choosePosition =position;
                        notifyDataSetChanged();
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return null==couponBeans?0:couponBeans.size();
    }
}
