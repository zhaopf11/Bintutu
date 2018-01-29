package com.zhurui.bunnymall.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.mine.bean.OrderBean;
import com.zhurui.bunnymall.mine.bean.OrderShopBean;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.viewutils.Anticlockwise;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by zhaopf on 2017/9/9.
 */

public class EvaluateOrdersAdapter extends RecyclerView.Adapter<EvaluateOrdersAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private Context context;
    private ImageLoader imageLoader;
    private List<OrderBean> orderBeanList;
    public EvaluateOrdersAdapter(Context context,List<OrderBean> orderBeanList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
        this.orderBeanList = orderBeanList;
    }


    @Override
    public EvaluateOrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_evaluate_orders,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EvaluateOrdersAdapter.ViewHolder holder, int position) {
        if(orderBeanList != null && orderBeanList.size() > 0){
            OrderBean orderBean = orderBeanList.get(position);
            String sxImage = "";
            if(orderBean != null){
                holder.text_productintroduce.setText(orderBean.getName());
                holder.text_productcolor.setText("颜色：" + orderBean.getColorPropertiesName());
                holder.text_productsize.setText("鞋码：" + orderBean.getSizePropertiesName());
                if(orderBean.getDingZhiShuXing() != null && orderBean.getDingZhiShuXing().getYanseList() != null){
                    sxImage = orderBean.getDingZhiShuXing().getYanseList().get(0).getCustomPropertiesValueImageUrl();
                    holder.image_product.setImageUrl(Contants.BASE_IMGURL + sxImage,imageLoader);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return orderBeanList == null ? 0 : orderBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private NetworkImageView image_product;
        private TextView text_productintroduce;
        private TextView text_productsize;
        private  TextView text_productcolor;
        public ViewHolder(View view) {
            super(view);
            image_product = (NetworkImageView) view.findViewById(R.id.img_product);
            text_productintroduce = (TextView) view.findViewById(R.id.text_productintroduce);
            text_productcolor = (TextView) view.findViewById(R.id.text_productcolor);
            text_productsize = (TextView) view.findViewById(R.id.text_productsize);
        }
    }

}
