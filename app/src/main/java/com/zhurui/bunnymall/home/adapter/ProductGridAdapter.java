package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.home.bean.Product;
import com.zhurui.bunnymall.utils.Contants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoux on 2017/8/2.
 */

public class ProductGridAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private int state;
    private Context context;
    public List<Product> products = new ArrayList<>();
    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public ProductGridAdapter(Context context, int state, List<Product> products) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.state = state;
        this.products = products;
    }

    @Override
    public int getCount() {

        return null == products ? 0 : products.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        Product product = products.get(position);
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.adapter_product_grid,
                    parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        if(!(Contants.BASE_IMGURL+product.getMainImage()).equals(viewHolder.img_product.getTag(R.id.imageloader_uri))){
        Glide.with(context).load(Contants.BASE_IMGURL + product.getMainImage())
                .placeholder(R.drawable.list_normal)
                .error(R.drawable.list_normal)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//是将图片原尺寸缓存到本地。
                .into(viewHolder.img_product);
        viewHolder.img_product.setTag(R.id.imageloader_uri, Contants.BASE_IMGURL + product.getMainImage());
//        }else{
//        }

        viewHolder.text_price.setText(product.getPrice());
        if (state == 3 || state == 4 || state == 2) {
            viewHolder.text_beforeprice.setText("¥" + product.getNormalPrice());
        } else {
            viewHolder.text_beforeprice.setText("");
        }
        return convertView;
    }

    class ViewHolder {

        //        ImageView img_productlogo;
        ImageView img_product;
        //        TextView text_introduct;
        TextView text_price;
        TextView text_beforeprice;

        public ViewHolder(View view) {
//            img_productlogo=(ImageView)view.findViewById(R.id.img_productlogo);
            img_product = (ImageView) view.findViewById(R.id.img_product);
//            text_introduct=(TextView)view.findViewById(R.id.text_introduct);
            text_price = (TextView) view.findViewById(R.id.text_price);
            text_beforeprice = (TextView) view.findViewById(R.id.text_beforeprice);
            text_beforeprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        }
    }

    public void setDataChanged(List<Product> productList) {
        this.products = productList;
    }

}
