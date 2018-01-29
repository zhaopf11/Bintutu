package com.zhurui.bunnymall.viewutils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.bean.FootTypeBean;
import com.zhurui.bunnymall.home.bean.PropertyChildBean;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoux on 2017/8/2.
 */

public class ProductStandardDialog extends Dialog {

    private View view;
    private Context context;
    private NetworkImageView img_product;
    private TextView text_unit;
    private TextView text_price;
    private ImageView img_close;
    private CheckBox checkbox_choose;
    private FlowRadioGroup flowRaidogroupfoot;
    private FlowRadioGroup flowRadioGroupColor;
    private FlowRadioGroup flowRadioGroupSize;
    private LinearLayout lin_foottype;
    private Button btn_submit;
    private Map<String, List<PropertyChildBean>> propertyChildMap;
    private String price;
    private String mainImage;
    private ImageLoader imageLoader;
    private String colorId;
    private String sizeId;
    private String colorName;
    private String sizeName;
    private int num=1;
    private AmountView amountView;
    private OnClickDialog onClickDialog;
    private List<FootTypeBean> footTypeBeen;
    private String userfoottypeid;
    private String maxBuyNumber;
    private boolean isNeedLogin;
    private TextView text_sizecolor;
    private LinearLayout lin_img_close;

    public ProductStandardDialog(@NonNull Context context, @StyleRes int themeResId, Map<String, List<PropertyChildBean>> propertyChildMap, String price, String mainImage, List<FootTypeBean> footTypeBeen,String maxBuyNumber,boolean isNeedLogin) {
        super(context, themeResId);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.dialog_product_standard, null);
        this.context = context;
        this.propertyChildMap = propertyChildMap;
        this.price = price;
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
        this.mainImage = mainImage;
        this.footTypeBeen = footTypeBeen;
        this.maxBuyNumber = maxBuyNumber;
        this.isNeedLogin = isNeedLogin;
        initView();
        initData();
        setView();
    }

    private void initView() {
        img_product = (NetworkImageView) view.findViewById(R.id.img_product);
        text_unit = (TextView) view.findViewById(R.id.text_unit);
        text_price = (TextView) view.findViewById(R.id.text_price);
        img_close = (ImageView) view.findViewById(R.id.img_close);
        lin_img_close = (LinearLayout)view.findViewById(R.id.lin_img_close);
        checkbox_choose = (CheckBox) view.findViewById(R.id.checkbox_choose);
        flowRaidogroupfoot = (FlowRadioGroup) view.findViewById(R.id.flowRaidogroupfoot);
        flowRadioGroupColor = (FlowRadioGroup) view.findViewById(R.id.flowRadioGroupColor);
        flowRadioGroupSize = (FlowRadioGroup) view.findViewById(R.id.flowRadioGroupSize);
        lin_foottype =(LinearLayout)view.findViewById(R.id.lin_foottype);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        text_sizecolor = (TextView)view.findViewById(R.id.text_sizecolor);
        text_unit.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.font_30));
        text_price.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.font_36));
        flowRaidogroupfoot.setVisibility(View.GONE);
        amountView =(AmountView) view.findViewById(R.id.amountView);

        if(null !=maxBuyNumber && !"".equals(maxBuyNumber)){
            amountView.setGoods_storage(Integer.parseInt(maxBuyNumber));
        }

        amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                num = amount;
            }

            @Override
            public void toMax() {
                ToastUtils.show(context,"超过最大购买量");
            }
        });
    }

    private void initData() {
        img_product.setImageUrl(Contants.BASE_IMGURL+mainImage,imageLoader);
        img_product.setErrorImageResId(R.drawable.new_recommand_normal);
        img_product.setDefaultImageResId(R.drawable.new_recommand_normal);
        text_price.setText(price);
        lin_img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
//        flowRaidogroupfoot.removeAllViews();
        if(null !=footTypeBeen && footTypeBeen.size()>0){
            lin_foottype.setVisibility(View.VISIBLE);
            for (int i = 0; i < footTypeBeen.size(); i++) {
                RadioButton footbutton = (RadioButton) LayoutInflater.from(context).inflate(R.layout.adapter_productstandard_footinfo, null);
                footbutton.setText(footTypeBeen.get(i).getName());
                footbutton.setHeight((int) context.getResources().getDimension(R.dimen.margin_60));
                footbutton.setTag(footTypeBeen.get(i).getUserFootTypeDataID());
                flowRaidogroupfoot.addView(footbutton, i);

            }
            checkbox_choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        flowRaidogroupfoot.setVisibility(View.VISIBLE);
                    } else {
                        flowRaidogroupfoot.setVisibility(View.GONE);
                    }
                }
            });
        }else {
            lin_foottype.setVisibility(View.GONE);
        }


        if (null != propertyChildMap && propertyChildMap.containsKey("17242")) {
            for (int i = 0; i < propertyChildMap.get("17242").size(); i++) {
                PropertyChildBean propertyChildBeen = propertyChildMap.get("17242").get(i);
                RadioButton colorbutton = (RadioButton) LayoutInflater.from(context).inflate(R.layout.adapter_productstandard_footinfo, null);
                colorbutton.setWidth((int) context.getResources().getDimension(R.dimen.ui_110));
                colorbutton.setHeight((int) context.getResources().getDimension(R.dimen.margin_60));
                colorbutton.setText(propertyChildBeen.getName());
                colorbutton.setTag(propertyChildBeen.getCustomPropertiesID());
                flowRadioGroupColor.addView(colorbutton, i);
            }

        }
        flowRadioGroupColor.setOnCheckedChangeListener(onCheckedChangeListener);
        if (null != propertyChildMap && propertyChildMap.containsKey("17250")) {
            for (int i = 0; i < propertyChildMap.get("17250").size(); i++) {
                PropertyChildBean propertyChildBeen = propertyChildMap.get("17250").get(i);
                RadioButton sizebutton = (RadioButton) LayoutInflater.from(context).inflate(R.layout.adapter_productstandard_footinfo, null);
                sizebutton.setWidth((int)context.getResources().getDimension(R.dimen.ui_110));
                sizebutton.setHeight((int)context.getResources().getDimension(R.dimen.margin_60));
                sizebutton.setText(propertyChildBeen.getName());
                sizebutton.setTag(propertyChildBeen.getCustomPropertiesValueID());
                flowRadioGroupSize.addView(sizebutton, i);
            }
        }
        flowRadioGroupSize.setOnCheckedChangeListener(onCheckedChangeListener);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNeedLogin){
                    if(null == footTypeBeen || footTypeBeen.size()<1){
                        ToastUtils.show(context,"请先添加足型数据");
                    }else if(null == userfoottypeid || "".equals(userfoottypeid)){
                        ToastUtils.show(context,"请先选择足型数据");
                    }else if(null ==colorId || "".equals(colorId)){
                        ToastUtils.show(context,"请选择商品属性");
                    }else if(null ==sizeId || "".equals(sizeId)){
                        ToastUtils.show(context,"请选择商品属性");

                    }else{
                        onClickDialog.subMit(num,colorId,sizeId,colorName,sizeName,userfoottypeid);
                        dismiss();
                    }
                }else {
                    onClickDialog.subMit(num,colorId,sizeId,colorName,sizeName,userfoottypeid);
                    dismiss();
                }


            }
        });

        flowRaidogroupfoot.setOnCheckedChangeListener(onCheckedChangeListener);

    }

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (group.getId()) {
                case R.id.flowRaidogroupfoot:
                    for (int i = 0; i < group.getChildCount(); i++) {
                        RadioButton radioButton = (RadioButton) group
                                .getChildAt(i);
                        if(radioButton.getId() == checkedId){
                            userfoottypeid = (String) radioButton.getTag();
                            checkbox_choose.setText(radioButton.getText());
                        }
                    }
                    break;
                case R.id.flowRadioGroupColor:
                    for (int i = 0; i < group.getChildCount(); i++) {
                        RadioButton radioButton = (RadioButton) group
                                .getChildAt(i);
                        if(radioButton.getId() == checkedId){
                            colorId = (String) radioButton.getTag();
                            colorName = radioButton.getText().toString().trim();
                            if(null ==sizeName || "".equals(sizeName)){
                                text_sizecolor.setText("请选择尺码");
                            }else {
                                text_sizecolor.setText("已选择 颜色:"+colorName+",尺码:"+sizeName);
                            }

                        }
                    }
                    break;
                case R.id.flowRadioGroupSize:
                    for (int i = 0; i < group.getChildCount(); i++) {
                        RadioButton radioButton = (RadioButton) group
                                .getChildAt(i);
                        if(radioButton.getId() == checkedId){
                            sizeId = (String) radioButton.getTag();
                            sizeName = radioButton.getText().toString().trim();
                            if(null ==colorName || "".equals(colorName)){
                                text_sizecolor.setText("请选择颜色");
                            }else {
                                text_sizecolor.setText("已选择 颜色:"+colorName+",尺码:"+sizeName);
                            }

                        }
                    }
                    break;
            }

        }
    };


    private void setView() {
        view.setAlpha(1.0f);
        this.setContentView(view);
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.AnimBottom);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) context.getResources().getDisplayMetrics().widthPixels; // 宽度
        dialogWindow.setAttributes(lp);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }


    public interface OnClickDialog{
        void subMit(int amount,String colorID,String sizeID,String colorName,String sizeName,String foottypeid);
    }

    public void setOnClickDialog(OnClickDialog onClickDialog){
        this.onClickDialog = onClickDialog;
    }

}
