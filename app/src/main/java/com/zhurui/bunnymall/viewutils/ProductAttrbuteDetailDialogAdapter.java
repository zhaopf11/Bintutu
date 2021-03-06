package com.zhurui.bunnymall.viewutils;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.activity.ChooseMaterialActivity;
import com.zhurui.bunnymall.home.bean.CustomGroupProperty;
import com.zhurui.bunnymall.home.bean.CustomeChildPropertyBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaopf on 2017/8/22.
 */

public class ProductAttrbuteDetailDialogAdapter extends BaseExpandableListAdapter implements ProductAttrbuteChildAdapter.OnChildItemClick {
    private LayoutInflater mInflater;
    private Context context;
    private OnItemClick onItemClick;
    public Map<Integer, String> checkMap = new HashMap<Integer, String>();
    public Map<String, List<CustomeChildPropertyBean>> propertyMap;

    public List<CustomGroupProperty> propertyGroupBeen;
    private ImageLoader imageLoader;

    public ProductAttrbuteDetailDialogAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        imageLoader = BaseApplication.getInstance().getImageLoader();
    }

    @Override
    public int getGroupCount() {
        return null == propertyGroupBeen ? 0 : propertyGroupBeen.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
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
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        CustomGroupProperty propertyGroupBean = propertyGroupBeen.get(groupPosition);
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.adapter_item_attr_group, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text_typename.setText(propertyGroupBean.getName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        List<CustomeChildPropertyBean> propertyChildBeen = propertyMap.get(propertyGroupBeen.get(groupPosition).getCustomPropertiesID());
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.adapter_product_attr_child, parent, false);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        if (null != propertyChildBeen && propertyChildBeen.size() > 0) {
            //内部adapter
            ProductAttrbuteChildAdapter productAttrbuteChildAdapter = new ProductAttrbuteChildAdapter(context,propertyChildBeen,propertyGroupBeen,groupPosition);

            if("1003".equals(propertyChildBeen.get(childPosition).getCustomPropertiesID()) ||
                    "17252".equals(propertyChildBeen.get(childPosition).getCustomPropertiesID()) ){
                childViewHolder.childattr_grid.setNumColumns(2);
            }else{
                childViewHolder.childattr_grid.setNumColumns(4);
            }
            childViewHolder.childattr_grid.setAdapter(productAttrbuteChildAdapter);
            productAttrbuteChildAdapter.setOnChildItemClick(this);
        }

        return convertView;
    }

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) group.getChildAt(i);
                if (radioButton.getId() == checkedId) {
                    CustomGroupProperty customGroupProperty = propertyGroupBeen.get((Integer) group.getTag());
                    CustomeChildPropertyBean propertyChildBean = propertyMap.get(propertyGroupBeen.get((Integer) group.getTag()).getCustomPropertiesID()).get((Integer) radioButton.getTag());
//                    onItemClick.childClick((Integer) group.getTag(), radioButton.getText().toString(), (Integer) radioButton.getTag(), propertyChildBean.getCustomPropertiesID() + ":" + propertyChildBean.getCustomPropertiesValueID(), customGroupProperty.getName() + ":" + propertyChildBean.getName());
                }
            };
        }
    };

    @Override
    public void childClick(int groupPosition, int childPosition, List<CustomGroupProperty> propertyGroupList, List<CustomeChildPropertyBean> propertyChildList) {
        if(onItemClick!=null){
            onItemClick.groupClick(groupPosition,childPosition,propertyGroupList,propertyChildList);
        }
    }

    public interface OnItemClick {
        void groupClick(int groupPosition, int childPosition, List<CustomGroupProperty> propertyGroupList, List<CustomeChildPropertyBean> propertyChildList);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_typename;
        CheckBox checkbox_type;

        public ViewHolder(View view) {
            super(view);
            text_typename = (TextView) view.findViewById(R.id.text_typename);
            checkbox_type = (CheckBox) view.findViewById(R.id.checkbox_type);
        }
    }

    class ChildViewHolder extends RecyclerView.ViewHolder {
        GridViewInScrollView childattr_grid;
        public ChildViewHolder(View view) {
            super(view);
            childattr_grid = (GridViewInScrollView)view.findViewById(R.id.childattr_grid);
        }
    }
}
