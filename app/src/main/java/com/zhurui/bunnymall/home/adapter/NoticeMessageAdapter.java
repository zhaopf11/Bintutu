package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.bean.LogisticsBean;
import com.zhurui.bunnymall.home.bean.NoticeMessageBean;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.Utils;

import java.util.List;

/**
 * Created by zhoux on 2017/7/19.
 */

public class NoticeMessageAdapter extends RecyclerView.Adapter<NoticeMessageAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private boolean isLogistical = false;
    private Context context;
    public List<NoticeMessageBean> mNoticeList;
    public List<LogisticsBean> mLogisticsList;
    public String typeId;
    private ImageLoader imageLoader;

    public NoticeMessageAdapter(Context context, boolean isLogistical)
    {
        mInflater = LayoutInflater.from(context);
        this.isLogistical = isLogistical;
        this.context = context;
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_date;
        TextView text_noticetitle;
        TextView text_logisticalcompany;
        NetworkImageView img_notice;
        TextView text_noticeintroduce;
        TextView text_logisticalnum;

        public ViewHolder(View view)
        {
            super(view);
            text_date = (TextView) view.findViewById(R.id.text_date);
            text_noticetitle = (TextView)view.findViewById(R.id.text_noticetitle);
            text_logisticalcompany =(TextView)view.findViewById(R.id.text_logisticalcompany);
            img_notice=(NetworkImageView)view.findViewById(R.id.img_notice);
            text_noticeintroduce = (TextView)view.findViewById(R.id.text_noticeintroduce);
            text_logisticalnum = (TextView)view.findViewById(R.id.text_logisticalnum);
            if(isLogistical){
                text_noticetitle.setTextColor(context.getResources().getColor(R.color.color_d2ba91));
//                text_noticetitle.setText("订单已签收");
                text_logisticalcompany.setVisibility(View.GONE);
                text_logisticalnum.setVisibility(View.VISIBLE);
            }

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

        View view = mInflater.inflate(R.layout.adapter_notice_message,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if("1".equals(typeId)){
            //物流信息
            if(mLogisticsList !=null && mLogisticsList.size() > 0){
                LogisticsBean logisticsBean =  mLogisticsList.get(position);
                if(logisticsBean != null){
                    String[] productList = Utils.split(logisticsBean.getProductlist(),"|||");
                    if(productList != null && productList.length > 0){
                        logisticsBean.setProductId(productList[0]);
                        logisticsBean.setProductImage(productList[1]);
                        logisticsBean.setProductContent(productList[2]);
                        logisticsBean.setProductSize(productList[3]);
                        holder.text_noticeintroduce.setText(productList[2]);
                        holder.img_notice.setImageUrl(Contants.BASE_IMGURL + productList[1],imageLoader);
                    }
                    holder.text_date.setText(logisticsBean.getOrderTime());
                    holder.text_noticetitle.setText(logisticsBean.getName());//快递
//                holder.text_logisticalcompany.setText("（"+ mNoticeMessageBean.getLogisticsCompanyName()+")");//快递
                    holder.text_logisticalnum.setText("运单编号："+ logisticsBean.getDeliveryCode());//运单编号
                }
            }
        }else if("0".equals(typeId)){
            //通知消息
            if(mNoticeList !=null && mNoticeList.size() > 0){
                NoticeMessageBean mNoticeMessageBean =  mNoticeList.get(position);
                if(mNoticeMessageBean != null){
                    holder.img_notice.setImageUrl(Contants.BASE_IMGURL + mNoticeMessageBean.getPic(),imageLoader);
                    holder.text_noticeintroduce.setText(mNoticeMessageBean.getContent());
                    holder.text_date.setText(mNoticeMessageBean.getAddTime());
                    holder.text_noticetitle.setText(mNoticeMessageBean.getTitle());//快递
//                holder.text_logisticalcompany.setText("（"+ mNoticeMessageBean.getLogisticsCompanyName()+")");//快递
//                    holder.text_logisticalnum.setText("运单编号："+ mNoticeMessageBean.getTrackingnumber());//运单编号
                }
            }
        }

        if(null !=mOnItemClickLitener){
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
        int count = 0;
        if ("1".equals(typeId)) {
            //物流
            count = mLogisticsList.size();
        } else if ("0".equals(typeId)) {
            //通知
            count = mNoticeList.size();
        }
        return count;
    }
}

