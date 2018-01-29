package com.zhurui.bunnymall.classify.adapter;

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
import com.zhurui.bunnymall.classify.bean.SmallClassifyBean;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.adapter.GuessRecommandAdapter;
import com.zhurui.bunnymall.utils.Contants;

import java.util.List;

/**
 * Created by zhoux on 2017/7/13.
 */

public class ClassifyAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    public List<SmallClassifyBean> smallClassifyBeens;
    private ImageLoader imageLoader;
    private Context context;
    private OnItemClick onItemClick;
    public ClassifyAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        NetworkImageView img_classify;
        TextView text_classifyname;
        public ViewHolder(View view) {
            super(view);
            img_classify=(NetworkImageView)view.findViewById(R.id.img_classify);
            text_classifyname =(TextView)view.findViewById(R.id.text_classifyname);
        }
    }


    @Override
    public int getCount() {

        return null == smallClassifyBeens?0:smallClassifyBeens.size();
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
        SmallClassifyBean smallClassifyBean = smallClassifyBeens.get(position);
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.adapter_classify,
                    parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.img_classify.setImageUrl(Contants.BASE_IMGURL+smallClassifyBean.getImage(),imageLoader);
        viewHolder.text_classifyname.setText(smallClassifyBean.getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClick(smallClassifyBean.getFashionID()+"");
            }
        });
        return convertView;
    }


    public interface OnItemClick{
        void onItemClick(String keyword);
    }

    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }
}
