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
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.cart.adapter.ShopCartAdapter;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.mine.bean.OrderBean;
import com.zhurui.bunnymall.mine.bean.OrderShopBean;
import com.zhurui.bunnymall.mine.bean.UserFootDataDetailBean;
import com.zhurui.bunnymall.utils.BintutuUtils;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.TimeUtils;
import com.zhurui.bunnymall.viewutils.Anticlockwise;
import com.zhurui.bunnymall.viewutils.bean.ProductAttrbuteSxstr;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoux on 2017/9/4.
 */

public class OrdersAdapter extends BaseExpandableListAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private ImageLoader imageLoader;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    public List<OrderShopBean> orderShopBeen;
    public Map<String, List<OrderBean>> orderBeanMap;
    private OnItemClickLitener mOnItemClickLitener;

    public OrdersAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.imageLoader = BaseApplication.getInstance().getImageLoader();

    }


    public interface OnItemClickLitener {
        void onItemClick(int groupposition, String shoporderid);

        void rightBtnClick(int groupposition, int position, int status);

        void leftBtnClick(int groupposition, int position, int status);

        void cancelOrder(int groupposition);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public int getGroupCount() {
        return null == orderShopBeen ? 0 : orderShopBeen.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        List<OrderBean> orderBeanList = orderBeanMap.get(orderShopBeen.get(groupPosition).getShopOrderID());
        return null == orderBeanList ? 0 : orderBeanList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getCombinedChildId(groupPosition, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        OrderShopBean orderShopBean = orderShopBeen.get(groupPosition);
        GroupHolder groupHolder = null;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.adapter_orders, parent, false);
            groupHolder = new GroupHolder(convertView);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.text_ordernum.setText(orderShopBean.getShopOrderID());
        uiControl(groupHolder, null, Integer.parseInt(orderShopBean.getStatus()), orderShopBean);
        if (1 == Integer.parseInt(orderShopBean.getStatus())) {
            long time = 0;
            try {
                long currentTime = TimeUtils.getCurrentTimeMillis();
                long endTime = Long.parseLong(orderShopBean.getAutoCloseTimeMills());
                if (currentTime < endTime) {
                    time = (endTime - currentTime) / 1000;
                }
                long day = time / (3600 * 24);
                long hour = time % (24 * 3600) / 3600;
                long minute = time % 3600 / 60;
                groupHolder.anticlockwise.setText(day + "天" + hour + "时");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        groupHolder.text_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.cancelOrder(groupPosition);
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        OrderShopBean orderShopBean = orderShopBeen.get(groupPosition);
        List<OrderBean> orderList = orderBeanMap.get(orderShopBean.getShopOrderID());
        OrderBean orderBean = orderBeanMap.get(orderShopBean.getShopOrderID()).get(childPosition);
        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.adapter_orders, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.lin_center.setVisibility(View.VISIBLE);
        viewHolder.img_product.setImageUrl(Contants.BASE_IMGURL + orderBean.getMainImage(), imageLoader);
        viewHolder.text_producttitle.setText(orderBean.getName());
        float sigShuxingprice = Float.parseFloat(orderBean.getShuxingPrice())/Float.parseFloat(orderBean.getNumber());
        float singPrice = Float.parseFloat(orderBean.getPrice());

        viewHolder.text_sigle_price.setText("¥" + decimalFormat.format(sigShuxingprice + singPrice));
        if ("8".equals(orderShopBeen.get(groupPosition).getShopOrderTypeID())) {
            //定制商品
            if (TextUtils.isEmpty(orderBean.getPrice()) || "0.00".equals(orderBean.getPrice()) || "0".equals(orderBean.getPrice())) {
                viewHolder.text_price.setText("实付：议价");
            } else {
//                viewHolder.text_price.setText("实付：" + orderBean.getPrice());
                float sigleShuxingprice = Float.parseFloat(orderBean.getShuxingPrice())/Float.parseFloat(orderBean.getNumber());
                float totalPrice = Float.parseFloat(orderBean.getPrice()) + sigleShuxingprice;
                viewHolder.text_price.setText("实付：" + decimalFormat.format(totalPrice));
            }
        } else {
            float sigleShuxingprice = 0;
            float singlePrice = 0;
            float singleTotalPrice = 0;
            float totalPrice = 0;
            if(orderList != null && orderList.size() > 0){
                for(int i =0;i < orderList.size(); i++){
                    sigleShuxingprice = Float.parseFloat(orderList.get(i).getShuxingPrice())/Float.parseFloat(orderList.get(i).getNumber());
                    singlePrice = Float.parseFloat(orderList.get(i).getPrice());
                    singleTotalPrice = singlePrice +sigleShuxingprice;
                    totalPrice = totalPrice + singleTotalPrice;
                }
            }
            viewHolder.text_price.setText("实付: ¥" + decimalFormat.format(totalPrice));
        }
        viewHolder.text_productcount.setText("x" + orderBean.getNumber());
        ProductAttrbuteSxstr producSxstr = orderBean.getDingZhiShuXing();
        //展示属性定制
        showProductAttrSxstr(viewHolder,producSxstr);
        int status = Integer.parseInt(orderShopBean.getStatus());
        viewHolder.text_rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.rightBtnClick(groupPosition, childPosition, status);
            }
        });

        viewHolder.text_leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.leftBtnClick(groupPosition, childPosition, status);
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.onItemClick(groupPosition, orderShopBean.getShopOrderID());
            }
        });
        uiControl(null, viewHolder, status, orderShopBean);
        if (childPosition == ((orderBeanMap.get(orderShopBean.getShopOrderID()).size()) -1)) {
            viewHolder.rel_bottom.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rel_bottom.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder extends RecyclerView.ViewHolder {
        RelativeLayout rel_bottom;
        RelativeLayout rel_ordernum;
        LinearLayout lin_center;
        TextView text_ordernum;
        Anticlockwise anticlockwise;
        ImageView text_right;

        public GroupHolder(View itemView) {
            super(itemView);
            rel_bottom = (RelativeLayout) itemView.findViewById(R.id.rel_bottom);
            rel_ordernum = (RelativeLayout) itemView.findViewById(R.id.rel_ordernum);
            lin_center = (LinearLayout) itemView.findViewById(R.id.lin_center);
            rel_ordernum.setVisibility(View.VISIBLE);
            lin_center.setVisibility(View.GONE);
            rel_bottom.setVisibility(View.GONE);
            text_ordernum = (TextView) itemView.findViewById(R.id.text_ordernum);
            anticlockwise = (Anticlockwise) itemView.findViewById(R.id.anticlockwise);
            text_right = (ImageView) itemView.findViewById(R.id.text_right);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        NetworkImageView img_product;
        TextView text_producttitle;
        TextView text_productcount;
        TextView text_state;
        TextView text_price;
        TextView text_sigle_price;
        TextView text_leftbtn;
        TextView text_rightbtn;
        RelativeLayout rel_bottom;
        RelativeLayout rel_ordernum;
        LinearLayout lin_center;
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
            img_product = (NetworkImageView) view.findViewById(R.id.img_product);
            text_producttitle = (TextView) view.findViewById(R.id.text_producttitle);
            text_productcount = (TextView) view.findViewById(R.id.text_productcount);
            text_state = (TextView) view.findViewById(R.id.text_state);
            text_sigle_price = (TextView) view.findViewById(R.id.text_sigle_price);
            text_price = (TextView) view.findViewById(R.id.text_price);
            text_leftbtn = (TextView) view.findViewById(R.id.text_leftbtn);
            text_rightbtn = (TextView) view.findViewById(R.id.text_rightbtn);
            rel_bottom = (RelativeLayout) view.findViewById(R.id.rel_bottom);
            rel_ordernum = (RelativeLayout) view.findViewById(R.id.rel_ordernum);
            rel_ordernum.setVisibility(View.GONE);
            lin_center = (LinearLayout) view.findViewById(R.id.lin_center);
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


    private void uiControl(GroupHolder groupHolder, ViewHolder holder, int state, OrderShopBean orderShopBean) {
        //1:待支付2：待发货3：待收货4：待评价5：取消6：退货
        //1：未付款2：待配货 3: 已发货 4 ：已签收 5:交易成功 6：交易关闭 7：待审核 8：代发货
        //待支付 1 代发货  2 ，8 待收货   3  待评价 4，5
        switch (state) {
            //1：未付款(联系卖家，付款)  2 ,8：待发货 ( 联系卖家，提醒发货)
            // 3: 待收货 (查看物流，确认收货)  4 ：已签收(查看物流，待评价)
            // 5:交易成功(查看物流，待评价)  6：交易关闭（联系卖家）
            case 1:
                if (null != groupHolder) {
                    groupHolder.anticlockwise.setVisibility(View.VISIBLE);
                    groupHolder.text_right.setVisibility(View.GONE);
                    groupHolder.anticlockwise.setTimeFormat("dd天HH时");
                }

                if (null != holder) {
                    holder.text_state.setText(context.getString(R.string.topay));
                    holder.rel_bottom.setVisibility(View.VISIBLE);
                    holder.text_price.setVisibility(View.VISIBLE);
                    holder.text_leftbtn.setVisibility(View.VISIBLE);
                    holder.text_leftbtn.setText("联系客服");
                    holder.text_rightbtn.setVisibility(View.VISIBLE);
                    holder.text_rightbtn.setText("付款");
                }

                break;
            case 2:
                if (null != groupHolder) {
                    groupHolder.anticlockwise.setVisibility(View.GONE);
                    groupHolder.text_right.setVisibility(View.GONE);
                }
                if (null != holder) {
                    holder.text_state.setText(context.getString(R.string.tosend));
                    holder.rel_bottom.setVisibility(View.VISIBLE);
                    holder.text_price.setVisibility(View.GONE);
                    holder.text_leftbtn.setVisibility(View.VISIBLE);
                    holder.text_leftbtn.setText("联系客服");
                    holder.text_rightbtn.setVisibility(View.VISIBLE);
                    holder.text_rightbtn.setText("提醒发货");
                }
                break;
            case 3:
                if (null != groupHolder) {
                    groupHolder.anticlockwise.setVisibility(View.GONE);
                    groupHolder.text_right.setVisibility(View.GONE);
                }
                if (null != holder) {
                    holder.text_state.setText(context.getString(R.string.toget));
                    holder.rel_bottom.setVisibility(View.VISIBLE);
                    holder.text_price.setVisibility(View.GONE);
                    holder.text_leftbtn.setVisibility(View.VISIBLE);
                    holder.text_leftbtn.setText("查看物流");
                    holder.text_rightbtn.setVisibility(View.VISIBLE);
                    holder.text_rightbtn.setText("确认收货");
                }
                break;
            case 4:
                if (null != groupHolder) {
                    groupHolder.anticlockwise.setVisibility(View.GONE);
                    groupHolder.text_right.setVisibility(View.VISIBLE);
                }
                if (null != holder) {
                    holder.text_state.setText(context.getString(R.string.have_sign));
                    holder.rel_bottom.setVisibility(View.VISIBLE);
                    holder.text_price.setVisibility(View.GONE);
                    holder.text_leftbtn.setVisibility(View.VISIBLE);
                    holder.text_leftbtn.setText("查看物流");
                    holder.text_rightbtn.setVisibility(View.VISIBLE);
                    if ("1".equals(orderShopBean.getCommentFlag())) {
                        holder.text_rightbtn.setText("已评价");
                    } else {
                        holder.text_rightbtn.setText("评价商品");
                    }
                }
                break;
            case 5:
                if (null != groupHolder) {
                    groupHolder.anticlockwise.setVisibility(View.GONE);
                    groupHolder.text_right.setVisibility(View.VISIBLE);
                }
                if (null != holder) {
                    holder.text_state.setText(context.getString(R.string.order_success));
                    holder.rel_bottom.setVisibility(View.VISIBLE);
                    holder.text_price.setVisibility(View.GONE);
                    holder.text_leftbtn.setVisibility(View.VISIBLE);
                    holder.text_leftbtn.setText("查看物流");
                    holder.text_rightbtn.setVisibility(View.VISIBLE);
                    if ("1".equals(orderShopBean.getCommentFlag())) {
                        holder.text_rightbtn.setText("已评价");
                    } else {
                        holder.text_rightbtn.setText("评价商品");
                    }
                }
                break;
            case 6:
                if (null != groupHolder) {

                    groupHolder.anticlockwise.setVisibility(View.GONE);
                    groupHolder.text_right.setVisibility(View.VISIBLE);
                }
                if (null != holder) {
                    holder.text_state.setText(context.getString(R.string.have_close));
                    holder.rel_bottom.setVisibility(View.GONE);
                    holder.text_rightbtn.setVisibility(View.VISIBLE);
                    holder.text_rightbtn.setText("联系客服");
                    holder.text_rightbtn.setBackgroundResource(R.drawable.gray_deep_corner_bg);
                    holder.text_rightbtn.setTextColor(context.getResources().getColor(R.color.gray3));
                    holder.text_price.setVisibility(View.GONE);
                    holder.text_leftbtn.setVisibility(View.GONE);
                }
                break;
            case 7:
                break;
            case 8:
                if (null != groupHolder) {
                    groupHolder.anticlockwise.setVisibility(View.GONE);
                    groupHolder.text_right.setVisibility(View.GONE);
                }
                if (null != holder) {
                    holder.text_state.setText(context.getString(R.string.tosend));
                    holder.rel_bottom.setVisibility(View.VISIBLE);
                    holder.text_price.setVisibility(View.GONE);
                    holder.text_leftbtn.setVisibility(View.VISIBLE);
                    holder.text_leftbtn.setText("联系客服");
                    holder.text_rightbtn.setVisibility(View.VISIBLE);
                    holder.text_rightbtn.setText("提醒发货");
                }
                break;
        }
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
