package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.graphics.Paint;
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
import com.zhurui.bunnymall.home.bean.PreferentialBean;
import com.zhurui.bunnymall.home.bean.TeamBuyBean;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.TimeUtils;
import com.zhurui.bunnymall.viewutils.Anticlockwise;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by zhoux on 2017/7/18.
 */

public class BuyLimitAdapter extends RecyclerView.Adapter<BuyLimitAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private boolean isTeam = false;
    public List<PreferentialBean> preferentialBeanList;
    private ImageLoader imageLoader;
    private Context context;
    public int state;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public BuyLimitAdapter(Context context, boolean isTeam) {
        mInflater = LayoutInflater.from(context);
        this.isTeam = isTeam;
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private NetworkImageView img_product;
        private TextView text_name;
        private TextView text_countname;
        private TextView text_count;
        private TextView text_unit;
        private Anticlockwise chronometer;
        private TextView text_gonow;
        private TextView text_nowprice;
        private TextView text_beforeprice;

        public ViewHolder(View view) {
            super(view);
            img_product = (NetworkImageView) view.findViewById(R.id.img_product);
            text_name = (TextView) view.findViewById(R.id.text_name);
            text_countname = (TextView) view.findViewById(R.id.text_countname);
            text_count = (TextView) view.findViewById(R.id.text_count);
            text_unit = (TextView) view.findViewById(R.id.text_unit);
            chronometer = (Anticlockwise) view.findViewById(R.id.chronometer);
            text_gonow = (TextView) view.findViewById(R.id.text_gonow);
            text_nowprice = (TextView) view.findViewById(R.id.text_nowprice);
            text_beforeprice = (TextView) view.findViewById(R.id.text_beforeprice);
            text_beforeprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        }


    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemClickPosition(int position);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_limit_buy,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        uiControl(holder, state);
        PreferentialBean preferentialBean = preferentialBeanList.get(position);
        holder.img_product.setTag(Contants.BASE_IMGURL + preferentialBean.getImage());
        holder.img_product.setImageUrl(Contants.BASE_IMGURL + preferentialBean.getImage(), imageLoader);
        holder.img_product.setErrorImageResId(R.drawable.list_normal);
        holder.img_product.setDefaultImageResId(R.drawable.list_normal);
        if (preferentialBean.getShoppingPrice().contains(".")) {
            String[] b = preferentialBean.getShoppingPrice().split("[ .]");
            if (b[1].equals("00")) {
                holder.text_beforeprice.setText("¥" + b[0]);
            } else {
                holder.text_beforeprice.setText("¥" + preferentialBean.getShoppingPrice());
            }
        } else {
            holder.text_beforeprice.setText("¥" + preferentialBean.getShoppingPrice());
        }

        if (preferentialBean.getPrice().contains(".")) {
            String[] b = preferentialBean.getPrice().split("[ .]");
            if (b[1].equals("00")) {
                holder.text_nowprice.setText("¥" + b[0]);
            } else {
                holder.text_nowprice.setText("¥" + preferentialBean.getPrice());
            }
        } else {
            holder.text_nowprice.setText("¥" + preferentialBean.getPrice());
        }
        holder.text_count.setText(preferentialBean.getStock() + "");
        holder.text_name.setText(preferentialBean.getName());
        long currentTime = TimeUtils.getCurrentTimeInLong();
        if (isTeam) {
            if (3 == state && Integer.parseInt(preferentialBean.getStock()) > 0) {
                isStart(currentTime, Long.parseLong(preferentialBean.getStartTimeMills()), Long.parseLong(preferentialBean.getEndTimeMills()), holder);
            } else if(2 == state){
                uiControl(holder, 2);
            } else {
                uiControl(holder, 4);
            }

        } else {
            if (3 == state && Integer.parseInt(preferentialBean.getStock()) > 0) {
                isStart(currentTime, Long.parseLong(preferentialBean.getStartTimeMills()), Long.parseLong(preferentialBean.getEndTimeMills()), holder);
            } else if(2 == state){
                uiControl(holder, 2);
            } else {
                uiControl(holder, 4);
            }
        }

        if (null != mOnItemClickLitener) {
            holder.text_gonow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (3 == state && Integer.parseInt(preferentialBean.getStock()) > 0
                            && (currentTime > Long.parseLong(preferentialBean.getStartTimeMills()) && currentTime < Long.parseLong(preferentialBean.getEndTimeMills()))) {
                        mOnItemClickLitener.onItemClick(holder.itemView, position);
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (3 == state && Integer.parseInt(preferentialBean.getStock()) > 0
                            && (currentTime > Long.parseLong(preferentialBean.getStartTimeMills()) && currentTime < Long.parseLong(preferentialBean.getEndTimeMills()))) {
                        mOnItemClickLitener.onItemClickPosition(position);
                    }
                }
            });
        }
    }

    private void isStart(long currentTime, long startTime, long endTime, ViewHolder holder) {
        if (currentTime > startTime && currentTime < endTime) {
            holder.chronometer.initTime((endTime - currentTime) / 1000);
            holder.chronometer.reStart();
            holder.chronometer.setOnTimeCompleteListener(new Anticlockwise.OnTimeCompleteListener() {
                @Override
                public void onTimeComplete() {
                    uiControl(holder, 4);
                }
            });
            uiControl(holder, 3);
        } else {
            uiControl(holder, 4);
        }
    }


    private void uiControl(ViewHolder holder, int activityState) {
        switch (activityState) {
            case 2:
                if (isTeam) {
                    holder.text_countname.setText("剩余名额：");
                    holder.text_count.setTextColor(context.getResources().getColor(R.color.font_orange));
                    holder.text_unit.setVisibility(View.VISIBLE);
                    holder.chronometer.setVisibility(View.VISIBLE);
                    holder.text_nowprice.setTextColor(context.getResources().getColor(R.color.font_orange));
                    holder.text_gonow.setText("即将开始");
                    holder.text_gonow.setBackground(context.getResources().getDrawable(R.drawable.gray_corner_bg1));
                    holder.text_gonow.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.chronometer.onPause();
                    holder.chronometer.setTextColor(context.getResources().getColor(R.color.font_orange));
                    holder.chronometer.setText("24:00:00");
                } else {
                    holder.text_countname.setText("库存：");
                    holder.text_count.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.text_unit.setVisibility(View.GONE);
                    holder.text_nowprice.setTextColor(context.getResources().getColor(R.color.font_orange));
                    holder.text_gonow.setText("马上抢");
                    holder.chronometer.onPause();
                    holder.chronometer.setTextColor(context.getResources().getColor(R.color.font_orange));
                    holder.chronometer.setText("24:00:00");
                    holder.text_gonow.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.text_gonow.setBackground(context.getResources().getDrawable(R.drawable.gray_corner_bg1));
                }
                break;
            case 3:
                if (isTeam) {
                    holder.text_countname.setText("剩余名额：");
                    holder.text_count.setTextColor(context.getResources().getColor(R.color.font_orange));
                    holder.text_unit.setVisibility(View.VISIBLE);
                    holder.chronometer.setVisibility(View.VISIBLE);
                    holder.text_nowprice.setTextColor(context.getResources().getColor(R.color.font_orange));
                    holder.text_gonow.setText("立即加入");
                    holder.text_gonow.setTextColor(context.getResources().getColor(R.color.font_orange));
                    holder.chronometer.setTextColor(context.getResources().getColor(R.color.font_orange));
                } else {
                    holder.text_countname.setText("库存：");
                    holder.text_count.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.text_unit.setVisibility(View.GONE);
                    holder.chronometer.setVisibility(View.VISIBLE);
                    holder.text_nowprice.setTextColor(context.getResources().getColor(R.color.font_orange));
                    holder.text_gonow.setText("马上抢");
                    holder.text_gonow.setTextColor(context.getResources().getColor(R.color.font_orange));
                    holder.chronometer.setTextColor(context.getResources().getColor(R.color.font_orange));

                }
                holder.itemView.setEnabled(true);
                holder.text_gonow.setEnabled(true);
                break;
            case 4:
                if (isTeam) {
                    holder.text_countname.setText("剩余名额：");
                    holder.text_count.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.text_unit.setVisibility(View.VISIBLE);
                    holder.chronometer.setVisibility(View.VISIBLE);
                    holder.text_nowprice.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.text_gonow.setText("已结束");
                    holder.text_gonow.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.text_gonow.setBackground(context.getResources().getDrawable(R.drawable.gray_corner_bg1));
                    holder.chronometer.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.chronometer.initTime(0);

                } else {
                    holder.text_countname.setText("库存：");
                    holder.text_count.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.text_unit.setVisibility(View.GONE);
                    holder.chronometer.setVisibility(View.VISIBLE);
                    holder.text_nowprice.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.text_gonow.setText("已结束");
                    holder.text_gonow.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.text_gonow.setBackground(context.getResources().getDrawable(R.drawable.gray_corner_bg1));
                    holder.chronometer.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.chronometer.initTime(0);
                }
//                holder.itemView.setEnabled(false);
//                holder.text_gonow.setEnabled(false);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return null == preferentialBeanList ? 0 : preferentialBeanList.size();
    }
}
