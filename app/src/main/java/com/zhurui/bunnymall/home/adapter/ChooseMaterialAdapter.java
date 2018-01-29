package com.zhurui.bunnymall.home.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.home.activity.ChooseMaterialActivity;
import com.zhurui.bunnymall.home.bean.CustomGroupProperty;
import com.zhurui.bunnymall.home.bean.CustomeChildPropertyBean;
import com.zhurui.bunnymall.home.bean.PropertyChildBean;
import com.zhurui.bunnymall.home.bean.PropertyGroupBean;
import com.zhurui.bunnymall.viewutils.FlowRadioGroup;
import com.zhurui.bunnymall.viewutils.ScreenAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoux on 2017/8/22.
 */

public class ChooseMaterialAdapter extends BaseExpandableListAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private OnItemClick onItemClick;
    public Map<Integer, String> checkMap = new HashMap<Integer, String>();
    public Map<String,List<CustomeChildPropertyBean>> propertyMap;

    public List<CustomGroupProperty> propertyGroupBeen;

    public ChooseMaterialAdapter (Context context){
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getGroupCount() {
        return null ==propertyGroupBeen?0:propertyGroupBeen.size();
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
            convertView = mInflater.inflate(R.layout.adapter_material_group, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text_typename.setText(propertyGroupBean.getName());
        viewHolder.checkbox_type.setText(checkMap.get(groupPosition));
        viewHolder.checkbox_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onItemClick.groupClick(isChecked, groupPosition);
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        List<CustomeChildPropertyBean> propertyChildBeen = propertyMap.get(propertyGroupBeen.get(groupPosition).getCustomPropertiesID());
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.adapter_material_child, parent, false);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        if(null !=propertyChildBeen && propertyChildBeen.size()>0){
            childViewHolder.flowRadioGroup.removeAllViews();
            for (int i = 0; i <propertyChildBeen.size(); i++) {
                RadioButton button = (RadioButton) LayoutInflater.from(context).inflate(R.layout.radiobutton_material, null);
                button.setWidth((int) context.getResources().getDimension(R.dimen.ui_180));
                button.setHeight((int) context.getResources().getDimension(R.dimen.margin_50));
                button.setText(propertyChildBeen.get(i).getName() + "");
                button.setTag(i);
                if (null != ChooseMaterialActivity.childCheckMap && ChooseMaterialActivity.childCheckMap.containsKey(groupPosition) && i == ChooseMaterialActivity.childCheckMap.get(groupPosition)) {
                    button.setChecked(true);
                }
                childViewHolder.flowRadioGroup.addView(button, i);

            }
            childViewHolder.flowRadioGroup.setTag(groupPosition);
            childViewHolder.flowRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
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
                    CustomeChildPropertyBean propertyChildBean = propertyMap.get(propertyGroupBeen.get((Integer)group.getTag()).getCustomPropertiesID()).get((Integer) radioButton.getTag());
                    onItemClick.childClick((Integer) group.getTag(), radioButton.getText().toString(), (Integer) radioButton.getTag(),propertyChildBean.getCustomPropertiesID()+":"+propertyChildBean.getCustomPropertiesValueID(),customGroupProperty.getName()+":"+propertyChildBean.getName());
                }

            }

            ;
        }
    };


    public interface OnItemClick {
        void groupClick(boolean isChecked, int groupposition);

        void childClick(int groupPosition, String string, int childPosition,String propertyinfo,String propertyName);
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

        FlowRadioGroup flowRadioGroup;

        public ChildViewHolder(View view) {
            super(view);
            flowRadioGroup = (FlowRadioGroup) view.findViewById(R.id.flowRadioGroup);
        }

    }
}
