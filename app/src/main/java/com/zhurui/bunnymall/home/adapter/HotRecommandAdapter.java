package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.bean.NewProduct;
import com.zhurui.bunnymall.home.bean.PropertyChildBean;
import com.zhurui.bunnymall.home.bean.RecommandProduct;
import com.zhurui.bunnymall.home.bean.StoreProductBean;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.RegexUtil;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoux on 2017/7/13.
 */

public class HotRecommandAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private ImageLoader imageLoader;
    private List<RecommandProduct> hotproducts;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private Context context;

    private boolean isStore;
    public List<StoreProductBean> storeProductBeen;

    public HotRecommandAdapter(Context context, List<RecommandProduct> hotproducts, boolean isStore) {
        mInflater = LayoutInflater.from(context);
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
        this.hotproducts = hotproducts;
        this.context = context;
        this.isStore = isStore;

    }

    @Override
    public int getCount() {
        return null == storeProductBeen ? 0 : storeProductBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(null == convertView){
            convertView = mInflater.inflate(R.layout.adapter_hotrecommand,
                    parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.img_newproduct.setDefaultImageResId(R.drawable.list_normal);
        viewHolder.img_newproduct.setErrorImageResId(R.drawable.list_normal);
        if (isStore) {
            StoreProductBean storeProductBean = storeProductBeen.get(position);
            viewHolder.img_newproduct.setTag(Contants.BASE_IMGURL + storeProductBean.getMainImage());
            viewHolder.img_newproduct.setImageUrl(Contants.BASE_IMGURL + storeProductBean.getMainImage(), imageLoader);
            viewHolder.text_price.setText(storeProductBean.getPrice());
        }
        ViewHolder finalViewHolder = viewHolder;
        if (null != mOnItemClickLitener) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(finalViewHolder.itemView, position);
                }
            });
        }
        return convertView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView img_newproduct;
        TextView text_price;

        public ViewHolder(View view) {
            super(view);
            img_newproduct = (NetworkImageView) view.findViewById(R.id.img_newproduct);
            text_price = (TextView) view.findViewById(R.id.text_price);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
