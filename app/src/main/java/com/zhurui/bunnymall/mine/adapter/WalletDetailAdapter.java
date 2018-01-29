package com.zhurui.bunnymall.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.mine.bean.BalanceDetailBean;
import com.zhurui.bunnymall.mine.bean.PointDetailBean;

import java.util.List;

/**
 * Created by zhoux on 2017/7/25.
 */

public class WalletDetailAdapter extends RecyclerView.Adapter<WalletDetailAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    public List<BalanceDetailBean> balanceDetailBeen;
    public List<PointDetailBean> pointDetailBeen;

    public boolean isBalance = false;

    public WalletDetailAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_detailtitle;
        TextView text_detaildata;
        TextView text_money;
        public ViewHolder(View view) {
            super(view);
            text_detailtitle= (TextView) view.findViewById(R.id.text_detailtitle);
            text_detaildata=(TextView) view.findViewById(R.id.text_detaildata);
            text_money=(TextView)view.findViewById(R.id.text_money);
        }


    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.adapter_wallet_detail,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(isBalance){
            BalanceDetailBean balanceDetailBean = balanceDetailBeen.get(position);
            holder.text_detailtitle.setText(balanceDetailBean.getC_userMoneyHistoryTypeName());
            holder.text_detaildata.setText(balanceDetailBean.getAddTime());
            switch (balanceDetailBean.getUserMoneyHistoryTypeID()) {
                case "2":case "4":case "6":case "7":case "8":
                    holder.text_money.setText("+" + balanceDetailBean.getMoney());
                    break;
                case "3":case "5":
                    holder.text_money.setText("-" + balanceDetailBean.getMoney());
                    break;
            }
        }else {
            PointDetailBean pointDetailBean = pointDetailBeen.get(position);
            holder.text_detailtitle.setText(pointDetailBean.getC_userPointHistoryTypeName());
            holder.text_detaildata.setText(pointDetailBean.getAddTime());
            switch (pointDetailBean.getUserPointHistoryTypeID()) {
                case "1":case "3":case "4":case "6":case "8":case "9":
                    holder.text_money.setText("+" + pointDetailBean.getPoint());
                    break;
                case "5":case "7":
                    holder.text_money.setText("-" + pointDetailBean.getPoint());
                    break;
            }
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
        if(isBalance){
            return null == balanceDetailBeen ? 0 : balanceDetailBeen.size();
        }else {
            return null==pointDetailBeen?0:pointDetailBeen.size();
        }
    }
}

