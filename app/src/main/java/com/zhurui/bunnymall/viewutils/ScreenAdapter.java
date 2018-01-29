package com.zhurui.bunnymall.viewutils;

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
import com.zhurui.bunnymall.home.bean.PropertyChildBean;
import com.zhurui.bunnymall.home.bean.PropertyGroupBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoux on 2017/8/11.
 */

public class ScreenAdapter extends BaseExpandableListAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private OnItemClick onItemClick;

    public List<PropertyGroupBean> propertyGroupBeen;

    public Map<String ,List<PropertyChildBean>> propertyChildMap;


    public Map<Integer, String> checkMap = new HashMap<Integer, String>();

    public ScreenAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return null == propertyGroupBeen?0:propertyGroupBeen.size();
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
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.adapter_screen_group, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text_group.setText(propertyGroupBeen.get(groupPosition).getName());
        if(null !=ScreenDialog.checkMap && ScreenDialog.checkMap.size()>0 && null !=ScreenDialog.checkMap.get(groupPosition) && !"".equals(ScreenDialog.checkMap.get(groupPosition))){
            viewHolder.checkbox_group.setText(ScreenDialog.checkMap.get(groupPosition));
        }else {
            viewHolder.checkbox_group.setText("");
        }
        viewHolder.checkbox_group.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        List<PropertyChildBean> propertyChildBeen = propertyChildMap.get(propertyGroupBeen.get(groupPosition).getCustomPropertiesID());
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.adapter_screen_child, parent, false);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        if(null !=propertyChildBeen && propertyChildBeen.size()>0){
            childViewHolder.flow_child.removeAllViews();
            for (int i = 0; i < propertyChildBeen.size(); i++) {
                RadioButton button = (RadioButton) LayoutInflater.from(context).inflate(R.layout.radiobutton_screen, null);
                button.setWidth((int) context.getResources().getDimension(R.dimen.ui_170));
                button.setHeight((int) context.getResources().getDimension(R.dimen.margin_60));
                button.setText(propertyChildBeen.get(i).getName());
                button.setTag(i);
                if (null != ScreenDialog.childCheckMap && ScreenDialog.childCheckMap.containsKey(groupPosition) && i == ScreenDialog.childCheckMap.get(groupPosition)) {
                    button.setChecked(true);
                }
                childViewHolder.flow_child.addView(button, i);

            }
            childViewHolder.flow_child.setTag(groupPosition);
            childViewHolder.flow_child.setOnCheckedChangeListener(onCheckedChangeListener);
        }



        return convertView;
    }


    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) group.getChildAt(i);
                if (radioButton.getId() == checkedId) {
                    onItemClick.childClick((Integer) group.getTag(), radioButton.getText().toString(), (Integer) radioButton.getTag());
                }

            }

            ;
        }
    };

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public interface OnItemClick {
        void groupClick(boolean isChecked, int groupposition);

        void childClick(int groupPosition, String string, int childPosition);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_group;
        CheckBox checkbox_group;

        public ViewHolder(View view) {
            super(view);
            text_group = (TextView) view.findViewById(R.id.text_group);
            checkbox_group = (CheckBox) view.findViewById(R.id.checkbox_group);
        }


    }

    class ChildViewHolder extends RecyclerView.ViewHolder {

        FlowRadioGroup flow_child;

        public ChildViewHolder(View view) {
            super(view);
            flow_child = (FlowRadioGroup) view.findViewById(R.id.flow_child);
        }

    }


}
