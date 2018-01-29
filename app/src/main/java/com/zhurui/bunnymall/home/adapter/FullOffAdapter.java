package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.bean.FullOffShowBean;
import com.zhurui.bunnymall.utils.Contants;

import java.util.List;

/**
 * Created by zhoux on 2017/7/18.
 */

public class FullOffAdapter extends RecyclerView.Adapter<FullOffAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    public List<FullOffShowBean> fullOffShowBeen;
    private ImageLoader imageLoader;
    private Context context;

    public FullOffAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private NetworkImageView img_banner;
        private TextView text_name;
        private TextView text_gonow;

        public ViewHolder(View view) {
            super(view);
            img_banner = (NetworkImageView) view.findViewById(R.id.img_banner);
            text_name = (TextView) view.findViewById(R.id.text_name);
            text_gonow = (TextView) view.findViewById(R.id.text_gonow);

        }


    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position,String id);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_full_off,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FullOffShowBean fullOffShowBean = fullOffShowBeen.get(position);

        holder.img_banner.setImageUrl(Contants.BASE_IMGURL+fullOffShowBean.getShowImage(),imageLoader);
        holder.img_banner.setErrorImageResId(R.drawable.limit_team_normal);
        holder.img_banner.setDefaultImageResId(R.drawable.limit_team_normal);
        holder.text_name.setText(fullOffShowBean.getShowName());
        if (null != mOnItemClickLitener) {
            holder.text_gonow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position,fullOffShowBean.getShowID());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null == fullOffShowBeen ? 0 : fullOffShowBeen.size();
    }
}
