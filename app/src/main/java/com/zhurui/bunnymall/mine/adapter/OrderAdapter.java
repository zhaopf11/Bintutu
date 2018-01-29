package com.zhurui.bunnymall.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.mine.bean.OrderBean;
import com.zhurui.bunnymall.mine.bean.OrderShopBean;
import com.zhurui.bunnymall.viewutils.Anticlockwise;

import java.util.List;
import java.util.Map;

/**
 * Created by zhoux on 2017/7/25.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    public int ORDER_STATE = -1;
    private Context context;
    public List<OrderBean> orderBeen;
    public Map<String,OrderShopBean> orderShopBeanMap;

    public OrderAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_ordernum;
        Anticlockwise anticlockwise;
        TextView text_right;
        ImageView img_product;
        TextView text_producttitle;
        TextView text_productcount;
        TextView text_color;
        TextView text_size;
        TextView text_state;
        TextView text_price;
        TextView text_leftbtn;
        TextView text_rightbtn;
        RelativeLayout rel_bottom;
        RelativeLayout rel_ordernum;
        LinearLayout lin_center;

        public ViewHolder(View view) {
            super(view);
            text_ordernum = (TextView) view.findViewById(R.id.text_ordernum);
            anticlockwise = (Anticlockwise) view.findViewById(R.id.anticlockwise);
            text_right = (TextView) view.findViewById(R.id.text_right);
            img_product = (ImageView) view.findViewById(R.id.img_product);
            text_producttitle = (TextView) view.findViewById(R.id.text_producttitle);
            text_productcount = (TextView) view.findViewById(R.id.text_productcount);
            text_color = (TextView) view.findViewById(R.id.text_color);
            text_size = (TextView) view.findViewById(R.id.text_size);
            text_state = (TextView) view.findViewById(R.id.text_state);
            text_price = (TextView) view.findViewById(R.id.text_price);
            text_leftbtn = (TextView) view.findViewById(R.id.text_leftbtn);
            text_rightbtn = (TextView) view.findViewById(R.id.text_rightbtn);
            rel_bottom = (RelativeLayout) view.findViewById(R.id.rel_bottom);
            rel_ordernum=(RelativeLayout)view.findViewById(R.id.rel_ordernum);
            lin_center=(LinearLayout)view.findViewById(R.id.lin_center);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void rightBtnClick(int groupposition,int position);
        void leftBtnClick(int groupposition,int position);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.adapter_orders,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderBean orderBean = orderBeen.get(position);
        OrderShopBean orderShopBean =orderShopBeanMap.get(orderBean.getShopOrderID());
//        if(0==position && position<){
//            holder.rel_ordernum.setVisibility(View.VISIBLE);
//
//        }


        int status = Integer.parseInt(orderShopBean.getStatus());
        switch (status){
            case 1:
            holder.text_rightbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.rightBtnClick(position,status);
                }
            });

            holder.text_leftbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.leftBtnClick(position,status);
                }
            });
                break;
            case 2:
                holder.text_rightbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.rightBtnClick(position,status);
                    }
                });
                holder.text_leftbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.leftBtnClick(position,status);
                    }
                });
                break;
            case 3:
                holder.text_rightbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.rightBtnClick(position,status);
                    }
                });
                holder.text_leftbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.leftBtnClick(position,status);
                    }
                });
                break;
            case 4:
                holder.text_rightbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.rightBtnClick(position,status);
                    }
                });
                holder.text_leftbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.leftBtnClick(position,status);
                    }
                });
                break;
            case 5:
                holder.text_rightbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.rightBtnClick(position,status);
                    }
                });
                holder.text_leftbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.leftBtnClick(position,status);
                    }
                });
                break;
            case 8:
                holder.text_rightbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.rightBtnClick(position,status);
                    }
                });
                holder.text_leftbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.leftBtnClick(position,status);
                    }
                });
                break;

        }
        uiControl(holder,status);
        if (null != mOnItemClickLitener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    private void uiControl(ViewHolder holder, int state) {
        //1:待支付2：待发货3：待收货4：待评价5：取消6：退货
        //1：未付款2：待配货 3: 已发货 4 ：已签收 5:交易成功 6：交易关闭 7：待审核 8：代发货
        //待支付 1 代发货  2 ，8 待收货   3  待评价 4，5
        switch (state) {
            case 1:
                holder.anticlockwise.setVisibility(View.VISIBLE);
                holder.text_right.setVisibility(View.GONE);
                holder.anticlockwise.initTime(700000);
                holder.anticlockwise.setTimeFormat("dd天HH时");
                holder.anticlockwise.reStart();
                holder.text_state.setText(context.getString(R.string.topay));
                holder.rel_bottom.setVisibility(View.VISIBLE);
                holder.text_price.setVisibility(View.VISIBLE);
                holder.text_leftbtn.setVisibility(View.VISIBLE);
                holder.text_leftbtn.setText("联系卖家");
                holder.text_rightbtn.setVisibility(View.VISIBLE);
                holder.text_rightbtn.setText("付款");
                break;
            case 2:
                holder.anticlockwise.setVisibility(View.GONE);
                holder.text_right.setVisibility(View.VISIBLE);
                holder.text_right.setText("取消订单");
                holder.text_right.setBackgroundColor(context.getResources().getColor(R.color.navigation_bar_bg));
                holder.text_state.setText(context.getString(R.string.toset));
                holder.rel_bottom.setVisibility(View.VISIBLE);
                holder.text_price.setVisibility(View.GONE);
                holder.text_leftbtn.setVisibility(View.VISIBLE);
                holder.text_leftbtn.setText("联系卖家");
                holder.text_rightbtn.setVisibility(View.VISIBLE);
                holder.text_rightbtn.setText("提醒发货");
                break;
            case 3:
                holder.anticlockwise.setVisibility(View.GONE);
                holder.text_right.setVisibility(View.GONE);
                holder.text_state.setText(context.getString(R.string.toget));
                holder.rel_bottom.setVisibility(View.VISIBLE);
                holder.text_price.setVisibility(View.GONE);
                holder.text_leftbtn.setVisibility(View.VISIBLE);
                holder.text_leftbtn.setText("查看物流");
                holder.text_rightbtn.setVisibility(View.VISIBLE);
                holder.text_rightbtn.setText("确认收货");
                break;
            case 4:
                holder.anticlockwise.setVisibility(View.GONE);
                holder.text_right.setVisibility(View.VISIBLE);
                holder.text_right.setText("");
                holder.text_right.setBackgroundResource(R.drawable.order_delete);
                holder.text_state.setText(context.getString(R.string.have_sign));
                holder.rel_bottom.setVisibility(View.VISIBLE);
                holder.text_price.setVisibility(View.GONE);
                holder.text_leftbtn.setVisibility(View.VISIBLE);
                holder.text_leftbtn.setText("查看物流");
                holder.text_rightbtn.setVisibility(View.VISIBLE);
                holder.text_rightbtn.setText("评价商品");
                break;
            case 5:
                holder.anticlockwise.setVisibility(View.GONE);
                holder.text_right.setVisibility(View.VISIBLE);
                holder.text_right.setText("");
                holder.text_right.setBackgroundResource(R.drawable.order_delete);
                holder.text_state.setText(context.getString(R.string.order_success));
                holder.rel_bottom.setVisibility(View.VISIBLE);
                holder.text_price.setVisibility(View.GONE);
                holder.text_leftbtn.setVisibility(View.VISIBLE);
                holder.text_leftbtn.setText("查看物流");
                holder.text_rightbtn.setVisibility(View.VISIBLE);
                holder.text_rightbtn.setText("评价商品");
                break;
            case 6:
                holder.anticlockwise.setVisibility(View.GONE);
                holder.text_right.setVisibility(View.VISIBLE);
                holder.text_right.setText("");
                holder.text_right.setBackgroundResource(R.drawable.order_delete);
                holder.text_state.setText(context.getString(R.string.have_close));
                holder.rel_bottom.setVisibility(View.GONE);
                break;
            case 7:
                break;
            case 8:
                holder.anticlockwise.setVisibility(View.GONE);
                holder.text_right.setVisibility(View.VISIBLE);
                holder.text_right.setText("取消订单");
                holder.text_right.setBackgroundColor(context.getResources().getColor(R.color.navigation_bar_bg));
                holder.text_state.setText(context.getString(R.string.tosend));
                holder.rel_bottom.setVisibility(View.VISIBLE);
                holder.text_price.setVisibility(View.GONE);
                holder.text_leftbtn.setVisibility(View.VISIBLE);
                holder.text_leftbtn.setText("联系卖家");
                holder.text_rightbtn.setVisibility(View.VISIBLE);
                holder.text_rightbtn.setText("提醒发货");
                break;

//            case 6:
//                holder.anticlockwise.setVisibility(View.GONE);
//                holder.text_right.setVisibility(View.GONE);
//                holder.text_state.setText(context.getString(R.string.order_back));
//                holder.rel_bottom.setVisibility(View.VISIBLE);
//                holder.text_price.setVisibility(View.GONE);
//                holder.text_leftbtn.setVisibility(View.VISIBLE);
//                holder.text_leftbtn.setText("查看物流");
//                holder.text_rightbtn.setVisibility(View.VISIBLE);
//                holder.text_rightbtn.setText("联系卖家");
//                break;

        }


    }

    @Override
    public int getItemCount() {
        return null==orderBeen?0:orderBeen.size();
    }
}

