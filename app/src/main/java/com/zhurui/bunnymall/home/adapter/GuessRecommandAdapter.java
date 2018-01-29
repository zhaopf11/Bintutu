package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.bean.GuessProductInfo;
import com.zhurui.bunnymall.home.bean.StoreListProductBean;
import com.zhurui.bunnymall.utils.Contants;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by zhoux on 2017/7/13.
 */

public class GuessRecommandAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private Context context;
    private List<GuessProductInfo> guessProductInfos;
    private ImageLoader imageLoader;
    DecimalFormat decimalFormat=new DecimalFormat("0.00");
    private boolean isStore;
    public List<StoreListProductBean> storeListProductBeen;
    public GuessRecommandAdapter(Context context,List<GuessProductInfo> guessProductInfos,boolean isStore) {
        mInflater = LayoutInflater.from(context);
        this.guessProductInfos = guessProductInfos;
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
        this.isStore = isStore;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView img_newproduct;
//        TextView text_shortintroduct;
        TextView text_price;
        TextView text_beforeprice;
        public ViewHolder(View view) {
            super(view);
            img_newproduct = (NetworkImageView) view.findViewById(R.id.img_newproduct);
//            text_shortintroduct =(TextView) view.findViewById(R.id.text_shortintroduct);
            text_price = (TextView)view.findViewById(R.id.text_price);
            text_beforeprice=(TextView)view.findViewById(R.id.text_beforeprice);
            text_beforeprice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public int getCount() {
        if(isStore){
            return null == storeListProductBeen?0:storeListProductBeen.size();
        }else {
            return null ==guessProductInfos?0:guessProductInfos.size();

        }
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
            convertView = mInflater.inflate(R.layout.adapter_guessrecommand,
                    parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.img_newproduct.setDefaultImageResId(R.drawable.list_normal);
        viewHolder.img_newproduct.setErrorImageResId(R.drawable.list_normal);
        if(isStore){
            StoreListProductBean storeListProductBean = storeListProductBeen.get(position);
            viewHolder.img_newproduct.setTag(Contants.BASE_IMGURL+storeListProductBean.getMainImage());
            viewHolder.img_newproduct.setImageUrl(Contants.BASE_IMGURL+storeListProductBean.getMainImage(),imageLoader);
//            viewHolder.text_shortintroduct.setText(storeListProductBean.getName());
            viewHolder.text_price.setText(storeListProductBean.getPrice());
//            viewHolder.text_beforeprice.setText("¥"+storeListProductBean.getNormalPrice());
            viewHolder.text_beforeprice.setText("");
        }else {
            GuessProductInfo guessProductInfo = guessProductInfos.get(position);
            viewHolder.img_newproduct.setTag(Contants.BASE_IMGURL+guessProductInfo.getMainImage());
            viewHolder.img_newproduct.setImageUrl(Contants.BASE_IMGURL+guessProductInfo.getMainImage(),imageLoader);
//            viewHolder.text_shortintroduct.setText(guessProductInfo.getName());
            viewHolder.text_price.setText(guessProductInfo.getPrice());
//            viewHolder.text_beforeprice.setText("¥"+guessProductInfo.getNormalPrice());
            viewHolder.text_beforeprice.setText("");
        }

        ViewHolder finalViewHolder = viewHolder;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.onItemClick(finalViewHolder.itemView, position);
            }
        });

        return convertView;
    }
}
