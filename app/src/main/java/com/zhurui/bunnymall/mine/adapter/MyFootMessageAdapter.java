package com.zhurui.bunnymall.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.mine.bean.MyFootMessageBean;
import com.zhurui.bunnymall.mine.bean.RelateUserBean;

import java.util.List;

/**
 * Created by zhoux on 2017/8/4.
 */

public class MyFootMessageAdapter extends BaseExpandableListAdapter {
    private LayoutInflater mInflater;
    private Context context;
    public boolean isEdit = false;
    public boolean isSelected = false;
    private OnItemClickLitener mOnItemClickLitener;
    public  List<MyFootMessageBean> mGroupList;
    public List<RelateUserBean> mchildList;
    private String relateUserID;
    public MyFootMessageAdapter(Context context){
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void isSelected(boolean isSelected){
        this.isSelected = isSelected;
        notifyDataSetChanged();
    }

    public interface OnItemClickLitener
    {
        void onItemClick(int groupposition, int childposition);
        void isChecked(boolean isChecked, int position);
        void isDelete(String relateUserID);
    }



    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public int getGroupCount() {
        return mGroupList == null ? 0 : mGroupList.size();
//        return 3;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        int count = (0 == mchildList.size() ? 0 : mchildList.size());
        if(isEdit){
            count=0;
        }
        return count;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupList.get(groupPosition);
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
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
         GroupViewHolder groupViewHolder= null;
        if(null == convertView){
            convertView = mInflater.inflate(R.layout.adapter_foot_message,null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        }else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        if(mGroupList != null && mGroupList.size() > 0){
            MyFootMessageBean myFootMessageBean = mGroupList.get(groupPosition);
            if(myFootMessageBean != null){
                String sexStr = "";
                if("1".equals(myFootMessageBean.getSex().trim())){
                    sexStr = "男";
                }else if("2".equals(myFootMessageBean.getSex().trim())){
                    sexStr = "女";
                }
                groupViewHolder.text_name.setText(myFootMessageBean.getName()+"的足型数据");
                groupViewHolder.txt_num.setText( myFootMessageBean.getMeasureCode());
                groupViewHolder.text_sex.setText("性别： "+ sexStr);
                groupViewHolder.txt_shoesize.setText("鞋码： "+ myFootMessageBean.getShoeSize());
            }
        }

        if(isEdit){
            groupViewHolder.checkbox_foot.setVisibility(View.VISIBLE);
            groupViewHolder.checkbox_foot.setChecked(false);
            groupViewHolder.checkbox_foot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mOnItemClickLitener.isChecked(isChecked,groupPosition);
                }
            });
            groupViewHolder.checkbox_foot.setChecked(isSelected);
        }else {
            groupViewHolder.checkbox_foot.setVisibility(View.GONE);

        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.onItemClick(groupPosition,-1);
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder= null;
        if(null == convertView){
            convertView = mInflater.inflate(R.layout.adapter_child_footmessage,null);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        }else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        if(mchildList != null && mchildList.size() > 0){
            if(mchildList.get(childPosition) != null){
                String name = mchildList.get(childPosition).getNick();
//                relateUserID = mchildList.get(childPosition).getRelateUserID();
                childViewHolder.text_peoplename.setText(name + "的足型数据");
            }
        }

        if(0==groupPosition && (mchildList.size()-1) == childPosition){
            childViewHolder.view_down.setVisibility(View.GONE);
        }else {
            childViewHolder.view_down.setVisibility(View.VISIBLE);
        }

        childViewHolder.text_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.isDelete(mchildList.get(childPosition).getUserID());
            }
        });


        childViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mOnItemClickLitener.onItemClick(groupPosition,childPosition);
            }
        });


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

   class GroupViewHolder extends RecyclerView.ViewHolder {
       CheckBox checkbox_foot;
       TextView text_name;
       TextView txt_num;
       TextView text_sex;
       TextView txt_shoesize;

       public GroupViewHolder(View view) {
           super(view);
           checkbox_foot =(CheckBox) view.findViewById(R.id.checkbox_foot);
           text_name =(TextView) view.findViewById(R.id.text_name);
           txt_num =(TextView) view.findViewById(R.id.txt_num);
           text_sex =(TextView) view.findViewById(R.id.text_sex);
           txt_shoesize =(TextView) view.findViewById(R.id.txt_shoesize);
       }
   }

    class ChildViewHolder extends RecyclerView.ViewHolder {

        View view_down;
        TextView text_delete;
        TextView text_peoplename;
        public ChildViewHolder(View view) {
            super(view);
            view_down = view.findViewById(R.id.view_down);
            text_delete = (TextView) view.findViewById(R.id.text_delete);
            text_peoplename = (TextView) view.findViewById(R.id.text_peoplename);
        }
    }
}
