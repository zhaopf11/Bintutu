package com.zhurui.bunnymall.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.mine.activity.OrderDetailActivity;
import com.zhurui.bunnymall.mine.bean.OrderBean;
import com.zhurui.bunnymall.utils.BintutuUtils;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.viewutils.bean.ProductAttrbuteSxstr;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by zhoux on 2017/8/1.
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private Context context;
    public int ORDER_STATE = -1;
    public List<OrderBean> orderProductList;
    private ImageLoader imageLoader;
    private int order_state;
    private String returnGoodsStatus;
    private String refundStatus;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public OrderDetailAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relative_return;
        RelativeLayout rel_top;
        LinearLayout lin_center;
        RelativeLayout rel_bottom;
        TextView text_orderstate;
        TextView text_left;
        TextView text_right;
        TextView text_center;
        TextView checkbox_group;
        NetworkImageView img_product;
        TextView text_productintroduce;
        TextView text_count;
        TextView text_price;
        TextView return_left;
        TextView return_right;
        TextView return_center;
        RelativeLayout lin_check_material;
        RelativeLayout lin_personality_request;
        RelativeLayout lin_customize_size;
        TextView personality_request_text;
        TextView check_material_text;
        TextView customize_size_text;
        TextView personality_request_text1;
        TextView check_material_text1;
        TextView customize_size_text1;
        public ViewHolder(View view) {
            super(view);
            relative_return = (RelativeLayout) view.findViewById(R.id.relative_return);
            rel_top = (RelativeLayout) view.findViewById(R.id.rel_top);
            lin_center = (LinearLayout) view.findViewById(R.id.lin_center);
            rel_bottom = (RelativeLayout) view.findViewById(R.id.rel_bottom);
            text_orderstate = (TextView) view.findViewById(R.id.text_orderstate);
            text_orderstate.setVisibility(View.VISIBLE);
            rel_bottom.setVisibility(View.VISIBLE);
            text_left = (TextView) view.findViewById(R.id.text_left);
            text_right = (TextView) view.findViewById(R.id.text_right);
            text_center = (TextView) view.findViewById(R.id.text_center);
            checkbox_group = (TextView) view.findViewById(R.id.checkbox_group);
            img_product = (NetworkImageView) view.findViewById(R.id.img_product);
            text_productintroduce = (TextView) view.findViewById(R.id.text_productintroduce);
            text_count = (TextView) view.findViewById(R.id.text_count);
            text_price = (TextView) view.findViewById(R.id.text_price);
            return_left = (TextView) view.findViewById(R.id.return_left);
            return_center = (TextView) view.findViewById(R.id.return_center);
            return_right = (TextView) view.findViewById(R.id.return_right);
            lin_check_material = (RelativeLayout)view.findViewById(R.id.lin_check_material);
            lin_personality_request = (RelativeLayout)view.findViewById(R.id.lin_personality_request);
            lin_customize_size = (RelativeLayout)view.findViewById(R.id.lin_customize_size);
            personality_request_text = (TextView) view.findViewById(R.id.personality_request_text);
            personality_request_text1 = (TextView) view.findViewById(R.id.personality_request_text1);
            check_material_text = (TextView) view.findViewById(R.id.check_material_text);
            check_material_text1 = (TextView) view.findViewById(R.id.check_material_text1);
            customize_size_text = (TextView) view.findViewById(R.id.customize_size_text);
            customize_size_text1 = (TextView) view.findViewById(R.id.customize_size_text1);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void rightBtnClick(int position, int status);

        void centerBtnClick(int position, int status);

        void leftBtnClick(int position, int status);

        void returnRightBtnClick(int position, int status, String returnGoodsStatus);

        void retrunCenterBtnClick(int position, int status, String returnGoodsStatus);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.adapter_make_order,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (orderProductList != null && orderProductList.size() > 0) {
            if(0 == position){
                //显示店铺 或者隐藏
                holder.rel_top.setVisibility(View.VISIBLE);
                if(orderProductList.size() ==1){
                    holder.rel_bottom.setVisibility(View.VISIBLE);
                }else{
                    holder.rel_bottom.setVisibility(View.GONE);
                }
            }else{
                if (position == orderProductList.size() - 1) {
                    holder.rel_top.setVisibility(View.GONE);
                }else{
                    holder.rel_bottom.setVisibility(View.GONE);
                    holder.rel_top.setVisibility(View.GONE);
                }
            }
            OrderBean orderBean = orderProductList.get(position);
            if (orderBean != null) {
                holder.img_product.setImageUrl(Contants.BASE_IMGURL + orderBean.getMainImage(), imageLoader);
                holder.text_productintroduce.setText(orderBean.getName());
                holder.text_count.setText("x" + orderBean.getNumber());
                float sigleShuxingprice = Float.parseFloat(orderBean.getShuxingPrice())/Float.parseFloat(orderBean.getNumber());
                float totalPrice = Float.parseFloat(orderBean.getPrice()) + sigleShuxingprice;
                holder.text_price.setText("¥" + decimalFormat.format(totalPrice));
                ProductAttrbuteSxstr producSxstr = orderBean.getDingZhiShuXing();
                //展示属性定制
                showProductAttrSxstr(holder,producSxstr);
                if (OrderDetailActivity.orderProductDetail != null) {
                    holder.checkbox_group.setText(OrderDetailActivity.orderProductDetail.getSupplierName());
                    order_state = Integer.parseInt(OrderDetailActivity.orderProductDetail.getStatus());
                    returnGoodsStatus = OrderDetailActivity.orderProductDetail.getCurrReturnGoodsStatus();
                    refundStatus = OrderDetailActivity.orderProductDetail.getCurrRefundStatus();
                    uiControl(holder, order_state, returnGoodsStatus, refundStatus);
                }
            }
        }
        if (null != mOnItemClickLitener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });

            holder.text_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.leftBtnClick(position, order_state);
                }
            });
            holder.text_center.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.centerBtnClick(position, order_state);
                }
            });
            holder.text_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.rightBtnClick(position, order_state);
                }
            });
            holder.return_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //退货
                    mOnItemClickLitener.returnRightBtnClick(position, order_state, returnGoodsStatus);
                }
            });
            holder.return_center.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //发货
                    mOnItemClickLitener.retrunCenterBtnClick(position, order_state, returnGoodsStatus);
                }
            });
        }

    }

    private void uiControl(ViewHolder holder, int state, String returnGoodsStatus, String refundStatus) {
        //1:待支付（L取消订单，C联系卖家，R付款）  2，8：待发货（C退款，R联系卖家）
        // 3：待收货（C查看物流，R联系卖家） 4：已签收（C评价商品，R联系卖家）
        // 5：交易成功（C评价商品，R联系卖家）  6：交易关闭
        //退款状态 refundStatus 0：待审核  1：已退款  9：已拒绝
        //退货状态 returnGoodsStatus 1：等待卖家同意  2：等待买家退货  3：等待卖家收货 4：卖家拒绝退款
        // 5：待退款   6：退款成功
        switch (state) {
            case 1:
                holder.text_orderstate.setText("待支付");
                holder.text_left.setVisibility(View.VISIBLE);
                holder.text_left.setText("取消订单");
                holder.text_center.setText("联系客服");
                holder.text_right.setText("付款");
                break;
            case 2:
                holder.text_orderstate.setText("待发货");
                holder.text_left.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(refundStatus)) {
                    holder.text_center.setEnabled(false);
                    if ("0".equals(refundStatus)) {
                        holder.text_center.setText("待审核");
                    } else if ("1".equals(refundStatus)) {
                        holder.text_center.setText("已退款");
                    } else if ("3".equals(refundStatus)) {
                        holder.text_center.setText("退款中");
                    } else if ("9".equals(refundStatus) || "4".equals(refundStatus)) {
                        holder.text_center.setText("已拒绝");
                    }
                } else {
                    holder.text_center.setEnabled(true);
                    holder.text_center.setText("退款");
                }
                holder.text_right.setText("联系客服");
                break;
            case 3:
                holder.text_orderstate.setText("待收货");
                holder.text_left.setVisibility(View.GONE);
                holder.text_center.setText("查看物流");
                holder.text_right.setText("联系客服");
                break;
            case 4://退货
                holder.text_orderstate.setText("已签收");
                holder.text_left.setVisibility(View.GONE);
                if ("1".equals(OrderDetailActivity.orderProductDetail.getCommentFlag())) {
                    holder.text_center.setText("已评价");
                } else {
                    holder.text_center.setText("评价商品");
                }
                holder.text_right.setText("联系客服");
                holder.relative_return.setVisibility(View.VISIBLE);
                holder.return_left.setEnabled(false);
                holder.return_center.setEnabled(false);
                if (!TextUtils.isEmpty(returnGoodsStatus + "")) {
                    if ("1".equals(returnGoodsStatus)) {
                        holder.return_left.setText("等待卖家同意");
                        holder.return_right.setText("退货中");
                        holder.return_center.setEnabled(false);
                        holder.return_center.setVisibility(View.GONE);
                    } else if ("2".equals(returnGoodsStatus)) {
                        holder.return_left.setText("等待买家退货");
                        holder.return_center.setEnabled(true);
                        holder.return_right.setText("退货中");
                        holder.return_left.setEnabled(true);
                        holder.return_center.setVisibility(View.VISIBLE);
                    } else if ("3".equals(returnGoodsStatus)) {
                        holder.return_left.setText("等待卖家收货");
                        holder.return_right.setText("退货中");
                        holder.return_center.setVisibility(View.VISIBLE);
                        holder.return_center.setText("已发货");
                        holder.return_center.setEnabled(false);
                    } else if ("4".equals(returnGoodsStatus)) {
                        holder.return_left.setText("卖家拒绝退款");
                        holder.return_right.setEnabled(true);
                        holder.return_right.setText("退货");
                        holder.return_center.setVisibility(View.GONE);
                    } else if ("5".equals(returnGoodsStatus)) {
                        holder.return_left.setText("待退款");
                        holder.return_right.setText("退款中");
                        holder.return_center.setVisibility(View.VISIBLE);
                        holder.return_center.setText("已发货");
                        holder.return_center.setEnabled(false);
                    } else if ("6".equals(returnGoodsStatus)) {
                        holder.return_left.setText("退款成功");
                        holder.return_center.setVisibility(View.GONE);
                        holder.return_center.setText("已发货");
                        holder.return_center.setEnabled(false);
                        holder.return_right.setVisibility(View.GONE);
                    } else {
                        holder.return_center.setVisibility(View.GONE);
                    }
                }
                break;
            case 5:
                holder.text_orderstate.setText("交易成功");
                holder.text_left.setVisibility(View.GONE);
                if ("1".equals(OrderDetailActivity.orderProductDetail.getCommentFlag())) {
                    holder.text_center.setText("已评价");
                } else {
                    holder.text_center.setText("评价商品");
                }
                holder.text_right.setText("联系客服");
                break;
            case 6:
                holder.text_orderstate.setText("交易已关闭");
                holder.text_left.setVisibility(View.GONE);
                holder.text_center.setVisibility(View.GONE);
                holder.text_right.setVisibility(View.GONE);
                if ("6".equals(returnGoodsStatus)) {
                    holder.rel_bottom.setVisibility(View.GONE);
                    holder.relative_return.setVisibility(View.VISIBLE);
                    holder.return_left.setVisibility(View.VISIBLE);
                    holder.return_left.setText("退款成功");
                    holder.return_center.setVisibility(View.GONE);
                    holder.return_right.setVisibility(View.GONE);
                }
                break;
            case 8:
                holder.text_orderstate.setText("待发货");
                holder.text_left.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(refundStatus)) {
                    holder.text_center.setEnabled(false);
                    if ("0".equals(refundStatus)) {
                        holder.text_center.setText("待审核");
                    } else if ("1".equals(refundStatus)) {
                        holder.text_center.setText("已退款");
                    } else if ("9".equals(refundStatus)) {
                        holder.text_center.setText("已拒绝");
                    }
                } else {
                    holder.text_center.setEnabled(true);
                    holder.text_center.setText("退款");
                }
                holder.text_right.setText("联系客服");
                break;

        }
    }

    @Override
    public int getItemCount() {
        return orderProductList == null ? 0 : orderProductList.size();
    }


    /**
     * 展示选中的定制属性
     * @param producSxstr
     */
    private void showProductAttrSxstr(ViewHolder holder, ProductAttrbuteSxstr producSxstr) {
        String checkmaterial = "";
        String personalityrequest = "";
        String customizesize = "";
        String yanseImage = "";

        SpannableStringBuilder materialStr = new SpannableStringBuilder("");
        SpannableStringBuilder gexingStr = new SpannableStringBuilder("");
        SpannableStringBuilder chimaStr = new SpannableStringBuilder("");

        if (producSxstr!= null) {//定制属性
            List<Object> checkMaterialList = BintutuUtils.assembleCheckMaterial(context,producSxstr);
            List<Object> gexingRequireList = BintutuUtils.assembleGexingRequire(context,producSxstr);
            if (producSxstr.getFootdatadetail() != null && producSxstr.getFootdatadetail().size() > 0
                    && producSxstr.getFootdatadetail().get(0) !=null) {//选中的一条足型数据
                List<Object> checkFootDataList = BintutuUtils.assembleFootData(context,producSxstr,producSxstr.getFootdatadetail().get(0));
                if(checkFootDataList != null && checkFootDataList.size()>0){
                    customizesize = (String) checkFootDataList.get(0);
                    chimaStr = (SpannableStringBuilder) checkFootDataList.get(1);
                }
            }

            if(checkMaterialList != null && checkMaterialList.size()>0){
                checkmaterial = (String) checkMaterialList.get(0);
                materialStr = (SpannableStringBuilder) checkMaterialList.get(1);
            }
            if(gexingRequireList != null && gexingRequireList.size()>0){
                personalityrequest = (String) gexingRequireList.get(0);
                gexingStr = (SpannableStringBuilder) gexingRequireList.get(1);
            }

            if(producSxstr.getYanseList() != null && producSxstr.getYanseList().size() > 0){//选中颜色的图片
                yanseImage = producSxstr.getYanseList().get(0).getCustomPropertiesValueImageUrl();
            }
        }
        if(!TextUtils.isEmpty(checkmaterial)){
            holder.lin_check_material.setVisibility(View.VISIBLE);
            //需求要求下划线断开，富文本来写。下划线颜色随着字体的颜色改变，
            // 因此在该控件上方放置另一个控件，来实现字体和下划线颜色不一样
            holder.check_material_text.setText(materialStr);
            holder.check_material_text1.setText(checkmaterial);
        }
        if(!TextUtils.isEmpty(personalityrequest)){
            holder.lin_personality_request.setVisibility(View.VISIBLE);
            holder.personality_request_text.setText(gexingStr);
            holder.personality_request_text1.setText(personalityrequest);
        }
        if(!TextUtils.isEmpty(customizesize)){
            holder.lin_customize_size.setVisibility(View.VISIBLE);
            holder.customize_size_text.setText(chimaStr);
            holder.customize_size_text1.setText(customizesize);
        }
        if(!TextUtils.isEmpty(yanseImage)){
            holder.img_product.setImageUrl(Contants.BASE_IMGURL + yanseImage, imageLoader);
        }
    }
}
