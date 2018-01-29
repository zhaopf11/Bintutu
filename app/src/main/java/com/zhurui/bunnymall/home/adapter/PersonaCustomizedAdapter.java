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
import com.zhurui.bunnymall.common.BaseViewHolder;
import com.zhurui.bunnymall.home.bean.PersonalCustomizedbean;
import com.zhurui.bunnymall.utils.Contants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoux on 2017/7/17.
 */

public class PersonaCustomizedAdapter  extends BaseAdapter {



    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    public List<PersonalCustomizedbean> list = new ArrayList<>();;
    private ImageLoader imageLoader;

    public PersonaCustomizedAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.imageLoader = BaseApplication.getInstance().getImageLoader();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView img_customized;
        TextView text_introduce;
        TextView text_price;
        public ViewHolder(View view) {
            super(view);
            img_customized = (NetworkImageView) view.findViewById(R.id.img_customized);
            text_introduce = (TextView)view.findViewById(R.id.text_introduce);
            text_price = (TextView)view.findViewById(R.id.text_price);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, long productId);

    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    @Override
    public int getCount() {
        return null ==list?0:list.size();
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
        ViewHolder viewHolder =null;
        PersonalCustomizedbean personalCustomizedbean = list.get(position);
        if(null == convertView){
            convertView = mInflater.inflate(R.layout.adapter_personal_customized,
                    parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.img_customized.setImageUrl(Contants.BASE_IMGURL+personalCustomizedbean.getMainImage(),imageLoader);
        viewHolder.img_customized.setDefaultImageResId(R.drawable.list_normal);
        viewHolder.img_customized.setErrorImageResId(R.drawable.list_normal);
        viewHolder.text_introduce.setText(personalCustomizedbean.getName());
        ViewHolder finalViewHolder = viewHolder;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.onItemClick(finalViewHolder.itemView, personalCustomizedbean.getProductID());
            }
        });

        return convertView;
    }


}
