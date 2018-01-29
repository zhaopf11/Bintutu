package com.zhurui.bunnymall.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.bean.NewProduct;
import com.zhurui.bunnymall.mine.bean.FootPrintBean;
import com.zhurui.bunnymall.utils.Contants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoux on 2017/7/24.
 */

public class FootPrintAdapter extends BaseExpandableListAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private OnItem onItem;
    public Map<String,List<FootPrintBean>> footPrintMap = new HashMap<>();
    public List<String> keylist = new ArrayList<>();
    private ImageLoader imageLoader;
    DecimalFormat decimalFormat=new DecimalFormat("0.00");

    public FootPrintAdapter(Context context){
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.imageLoader = BaseApplication.getInstance().getImageLoader();

    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView text_findsimilarity;
        NetworkImageView img_product;
        TextView text_productintroduce;
        TextView text_productprice;
        public ViewHolder(View view)
        {
            super(view);
            text_findsimilarity =(TextView)view.findViewById(R.id.text_findsimilarity);
            img_product = (NetworkImageView) view.findViewById(R.id.img_product);
            text_productintroduce = (TextView)view.findViewById(R.id.text_productintroduce);
            text_productprice=(TextView)view.findViewById(R.id.text_productprice);
        }


    }

    public void setOnItem(OnItem onItem){
        this.onItem = onItem;
    }

    @Override
    public int getGroupCount() {
        return null == footPrintMap?0:footPrintMap.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int count = 0;
        if(null!= footPrintMap.get(keylist.get(groupPosition)) &&  footPrintMap.get(keylist.get(groupPosition)).size()>0){
            count = footPrintMap.get(keylist.get(groupPosition)).size();
        }
        return count;
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
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(null ==convertView){
            convertView = mInflater.inflate(R.layout.adapter_title_footprint,parent,false);
        }
        TextView text_time = (TextView) convertView.findViewById(R.id.text_time);
        text_time.setText(keylist.get(groupPosition));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,20,0,0);
        if(groupPosition>0){
            text_time.setLayoutParams(lp);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        FootPrintBean footPrintBean = footPrintMap.get(keylist.get(groupPosition)).get(childPosition);
        if(null ==convertView){
            convertView = mInflater.inflate(R.layout.adapter_collection_product,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.img_product.setImageUrl(Contants.BASE_IMGURL+footPrintBean.getMainImage(),imageLoader);
        viewHolder.img_product.setErrorImageResId(R.drawable.new_recommand_normal);
        viewHolder.img_product.setDefaultImageResId(R.drawable.new_recommand_normal);

        viewHolder.text_productintroduce.setText(footPrintBean.getName());
        viewHolder.text_productprice.setText("¥"+decimalFormat.format(footPrintBean.getPrice()));
        viewHolder.text_findsimilarity = (TextView) convertView.findViewById(R.id.text_findsimilarity);
        viewHolder.text_findsimilarity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItem.similarityClick(groupPosition,childPosition);
            }
        });
        return convertView;
    }

    public interface OnItem{
        void similarityClick(int groupPosition ,int position);
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
