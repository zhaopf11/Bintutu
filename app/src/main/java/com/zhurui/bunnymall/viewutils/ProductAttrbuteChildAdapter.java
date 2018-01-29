package com.zhurui.bunnymall.viewutils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.bean.CustomGroupProperty;
import com.zhurui.bunnymall.home.bean.CustomeChildPropertyBean;
import com.zhurui.bunnymall.utils.Contants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaopf on 2017/11/05.
 */

public class ProductAttrbuteChildAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private OnChildItemClick onItemClick;
    public Map<Integer, String> checkMap = new HashMap<Integer, String>();
    public List<CustomeChildPropertyBean> propertyChildList;
    private ImageLoader imageLoader;
    private CustomeChildPropertyBean customeChildPropertyBean;
    private int groupPosition;
    private List<CustomGroupProperty> propertyGroupList;

    public ProductAttrbuteChildAdapter(Context context, List<CustomeChildPropertyBean> propertyChildList, List<CustomGroupProperty> propertyGroupList, int groupPosition) {
        this.context = context;
        this.propertyChildList = propertyChildList;
        this.propertyGroupList = propertyGroupList;
        this.groupPosition = groupPosition;
        mInflater = LayoutInflater.from(context);
        imageLoader = BaseApplication.getInstance().getImageLoader();
    }

    @Override
    public int getCount() {
        return propertyChildList.size();
    }

    @Override
    public Object getItem(int position) {
        return propertyChildList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.adapter_child_item_attr, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (propertyChildList != null && propertyChildList.size() > 0) {
            customeChildPropertyBean = propertyChildList.get(position);
            viewHolder.product_attr_image.setImageUrl(Contants.BASE_IMGURL + customeChildPropertyBean.getCustomPropertiesValueImageUrl(), imageLoader);
            viewHolder.text_attr.setText(customeChildPropertyBean.getName());
            if (customeChildPropertyBean.isCheck()) {
                viewHolder.text_attr.setBackgroundColor(context.getResources().getColor(R.color.color_d2ba91));
                viewHolder.text_attr.setTextColor(context.getResources().getColor(R.color.gray3));
            } else {
                viewHolder.text_attr.setBackgroundColor(context.getResources().getColor(R.color.lin_bg));
                viewHolder.text_attr.setTextColor(context.getResources().getColor(R.color.navigation_bar_bg));
            }

            int width = (BaseApplication.getPhoneWidth() - 90) / 2;

            if ("1001".equals(customeChildPropertyBean.getCustomPropertiesID()) || "17242".equals(customeChildPropertyBean.getCustomPropertiesID())) {
                if (customeChildPropertyBean.isCheck()) {
                    viewHolder.rel_child_item.setBackground(context.getResources().getDrawable(R.drawable.orange_shape_bg));
                } else {
                    viewHolder.rel_child_item.setBackground(context.getResources().getDrawable(R.drawable.gray_shape_bg1));
                }
                if (TextUtils.isEmpty(customeChildPropertyBean.getName())) {
                    viewHolder.text_attr.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(customeChildPropertyBean.getCustomPropertiesValueImageUrl())) {
                    viewHolder.text_attr.setVisibility(View.GONE);
                }
            }

            if ("1003".equals(customeChildPropertyBean.getCustomPropertiesID()) || "17252".equals(customeChildPropertyBean.getCustomPropertiesID())) {
                //当定制属性为底配定制 单独设置控件的高度和宽度
                AbsListView.LayoutParams param1 = (AbsListView.LayoutParams) convertView.getLayoutParams();//获取当前控件的布局对象
                param1.height = (int) context.getResources().getDimension(R.dimen.ui_200);//设置当前控件布局的高度
                param1.width = (int) context.getResources().getDimension(R.dimen.ui_300);
                convertView.setLayoutParams(param1);//将设置好的布局参数应用到控件中

                RelativeLayout.LayoutParams param2 = (RelativeLayout.LayoutParams) viewHolder.product_attr_image.getLayoutParams();//获取当前控件的布局对象
                param2.height = (int) context.getResources().getDimension(R.dimen.ui_150);//设置当前控件布局的高度
                param2.width = (int) context.getResources().getDimension(R.dimen.ui_300);
                viewHolder.product_attr_image.setLayoutParams(param2);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.text_attr.getLayoutParams();//获取当前控件的布局对象
                params.height = (int) context.getResources().getDimension(R.dimen.ui_60);//设置当前控件布局的高度
                params.width = (int) context.getResources().getDimension(R.dimen.ui_300);
                viewHolder.text_attr.setLayoutParams(params);
            }

            if ("1010".equals(customeChildPropertyBean.getCustomPropertiesID())) {
                //个性定制 设置高度为50 imageview 隐藏，只显示textview
                AbsListView.LayoutParams params = (AbsListView.LayoutParams) convertView.getLayoutParams();//获取当前控件的布局对象
                params.height = (int) context.getResources().getDimension(R.dimen.margin_50);//设置当前控件布局的高度
                convertView.setLayoutParams(params);//将设置好的布局参数应用到控件中
                viewHolder.product_attr_image.setVisibility(View.GONE);
            }
            viewHolder.rel_child_item.setOnClickListener(new OnRadioButtonClick(viewHolder.rel_child_item, viewHolder.text_attr, propertyChildList, position));
        }

        return convertView;
    }


    class OnRadioButtonClick implements View.OnClickListener {
        private TextView text_attr;
        private List<CustomeChildPropertyBean> propertyChildBeen;
        private int i;
        private RelativeLayout rel_child_item;

        public OnRadioButtonClick(RelativeLayout rel_child_item, TextView text_attr, List<CustomeChildPropertyBean> propertyChildBeen, int i) {
            this.text_attr = text_attr;
            this.rel_child_item = rel_child_item;
            this.propertyChildBeen = propertyChildBeen;
            this.i = i;
        }

        @Override
        public void onClick(View v) {
            boolean isCheck = propertyChildBeen.get(i).isCheck();
            if (isCheck) {
                propertyChildBeen.get(i).setCheck(false);
                ProductAttrbuteDetailDialog.checkData.remove(propertyChildBeen.get(i));
                if (ProductAttrbuteDetailDialog.priceList != null && ProductAttrbuteDetailDialog.priceList.size() > 0) {
                    for (int a = 0; a < ProductAttrbuteDetailDialog.priceList.size(); a++) {
                        if ((ProductAttrbuteDetailDialog.priceList.get(a)[0] + "").equals(propertyChildBeen.get(i).getCustomPropertiesValueID())) {
                            ProductAttrbuteDetailDialog.tempPrice = ProductAttrbuteDetailDialog.tempPrice - Float.parseFloat(ProductAttrbuteDetailDialog.priceList.get(a)[1]);
                        }
                    }
                }
                text_attr.setBackgroundColor(context.getResources().getColor(R.color.lin_bg));
                text_attr.setTextColor(context.getResources().getColor(R.color.navigation_bar_bg));
                rel_child_item.setBackground(context.getResources().getDrawable(R.drawable.gray_shape_bg1));
            } else {
                ProductAttrbuteDetailDialog.checkData.add(propertyChildBeen.get(i));
                if (ProductAttrbuteDetailDialog.priceList != null && ProductAttrbuteDetailDialog.priceList.size() > 0) {
                    for (int a = 0; a < ProductAttrbuteDetailDialog.priceList.size(); a++) {
                        if ((ProductAttrbuteDetailDialog.priceList.get(a)[0] + "").equals(propertyChildBeen.get(i).getCustomPropertiesValueID())) {
                            ProductAttrbuteDetailDialog.tempPrice = ProductAttrbuteDetailDialog.tempPrice + Float.parseFloat(ProductAttrbuteDetailDialog.priceList.get(a)[1]);
                        }
                    }
                }

                propertyChildBeen.get(i).setCheck(!isCheck);
                text_attr.setBackgroundColor(context.getResources().getColor(R.color.color_d2ba91));
//                if("1001".equals(customeChildPropertyBean.getCustomPropertiesID()) || "17242".equals(customeChildPropertyBean.getCustomPropertiesID()) ) {
//                    rel_child_item.setBackground(context.getResources().getDrawable(R.drawable.orange_shape_bg));
//                }else{
//                    rel_child_item.setBackground(context.getResources().getDrawable(R.drawable.gray_shape_bg1));
//                }
                if (propertyChildBeen != null && propertyChildBeen.size() > 0) {
                    //底配定制是多选，其他定制为单选。
                    //当是底配定制的时候，当前位置设为选中，其他位置不考虑
                    // 当是其他定制的时候，设置当前位置为选中，其他位置为未选中
                    if (!"1003".equals(propertyChildBeen.get(i).getCustomPropertiesID()) && !"17252".equals(propertyChildBeen.get(i).getCustomPropertiesID())) {
                        for (int j = 0; j < propertyChildBeen.size(); j++) {
                            if (i == j) {
                                propertyChildBeen.get(j).setCheck(true);
                            } else {
                                propertyChildBeen.get(j).setCheck(false);
                            }
                        }
                    }else {
                        //底配定制
                        if ("1269".equals(propertyChildBeen.get(i).getCustomPropertiesValueID()) && propertyChildBeen.get(i).isCheck()) {
                            //当选中为无底配的时候，其他非无底配选项设置为不能选中
                            for (int a = 0; a < propertyChildBeen.size(); a++) {
                                if (!"1269".equals(propertyChildBeen.get(a).getCustomPropertiesValueID()) && propertyChildBeen.get(a).isCheck()) {
                                    propertyChildBeen.get(i).setCheck(false);
                                    ProductAttrbuteDetailDialog.checkData.remove(propertyChildBeen.get(i));
                                }
                            }
                        } else {
                            //当选中非无底配选项的时候。无底配不能选中
                            for (int b = 0; b < propertyChildBeen.size(); b++) {
                                if ("1269".equals(propertyChildBeen.get(b).getCustomPropertiesValueID()) && propertyChildBeen.get(b).isCheck()) {
                                    propertyChildBeen.get(i).setCheck(false);
                                    ProductAttrbuteDetailDialog.checkData.remove(propertyChildBeen.get(i));
                                }
                            }
                        }
                    }
                    notifyDataSetChanged();
                }
            }
            onItemClick.childClick(groupPosition, i, propertyGroupList, propertyChildList);
        }
    }

    public interface OnChildItemClick {
        void childClick(int groupPosition, int childPosition, List<CustomGroupProperty> propertyGroupList, List<CustomeChildPropertyBean> propertyChildList);
    }

    public void setOnChildItemClick(OnChildItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }


    class ViewHolder {
        NetworkImageView product_attr_image;
        TextView text_attr;
        RelativeLayout rel_child_item;

        public ViewHolder(View view) {
            product_attr_image = (NetworkImageView) view.findViewById(R.id.product_attr_image);
            text_attr = (TextView) view.findViewById(R.id.text_attr);
            rel_child_item = (RelativeLayout) view.findViewById(R.id.rel_child_item);
        }

    }
}
