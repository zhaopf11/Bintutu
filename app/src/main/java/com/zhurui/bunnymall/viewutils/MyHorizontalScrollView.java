package com.zhurui.bunnymall.viewutils;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zhurui.bunnymall.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhoux on 2017/8/14.
 */

public class MyHorizontalScrollView extends LinearLayout {

    Context context;
    private HorizontalScrollView hsv_menu;
    private View view;
    private RadioGroup rg_items ;
    private int width;//控件宽度
    private int height;
    private SecondLineRadioButton secondline6;
    private RadioButtonClick radioButtonClick;
    public MyHorizontalScrollView(Context context) {
        this(context, null);
    }

    public MyHorizontalScrollView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyHorizontalScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        this.context = context;
        view=LayoutInflater.from(context).inflate(
                R.layout.layout_horizontalscrollview, this, true);
        hsv_menu = (HorizontalScrollView) view.findViewById(R.id.hsv_menu);
        rg_items = (RadioGroup) view.findViewById(R.id.rg_items);
        rg_items.setOnCheckedChangeListener(mItemListener);
        secondline6 = (SecondLineRadioButton) view.findViewById(R.id.secondline6);
//        secondline4.setChecked(true);
//        hsv_menu.scrollTo(-(int) secondline6.getX(),0);
    }

    public void setData( List<String> strings1, List<String> strings2){
        int n = strings1.size() / 2;
        for(int i=0;i<rg_items.getChildCount();i++){
            SecondLineRadioButton secondLineRadioButton = (SecondLineRadioButton) rg_items.getChildAt(i);
            secondLineRadioButton.setFirst_text(strings1.get(i));
            secondLineRadioButton.setSecond_text(strings2.get(i));
            secondLineRadioButton.setVisibility(VISIBLE);
            secondLineRadioButton.setHeight((int) context.getResources().getDimension(R.dimen.ui_100));

            if(i == n){
                secondLineRadioButton.setChecked(true);
                secondLineRadioButton.setHeight((int) context.getResources().getDimension(R.dimen.ui_115));

            }
        }
//        hsv_menu.scrollTo(-(int) secondline6.getX(),0);
    }
    /**
     * 菜单项切换监听器
     */
    private RadioGroup.OnCheckedChangeListener mItemListener = new RadioGroup.OnCheckedChangeListener()
    {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            for(int i=0;i<group.getChildCount();i++){
                SecondLineRadioButton btn = (SecondLineRadioButton) group.getChildAt(i);
                if(btn.getId() == checkedId){
                    btn.setHeight((int) context.getResources().getDimension(R.dimen.ui_115));
                    moveItemToCenter(btn);
                    if(null !=radioButtonClick){
                        radioButtonClick.radioButtonClick(i);

                    }
                }else {
                    btn.setHeight((int) context.getResources().getDimension(R.dimen.ui_100));
                }

            }

        }

    };

    public interface RadioButtonClick{
        void radioButtonClick(int position);
    }

    public void setRadioButtonClick(RadioButtonClick radioButtonClick){
        this.radioButtonClick = radioButtonClick;
    }


    /**
     * 将菜单项尽量移至中央位置
     *
     * @param rb
     */
    private void moveItemToCenter(RadioButton rb)
    {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int[] locations = new int[2];
        rb.getLocationInWindow(locations);
        int rbWidth = rb.getWidth();
        hsv_menu.smoothScrollBy((locations[0] + rbWidth / 2 - screenWidth / 2),
                0);
    }


//    public void initView(){
//        if(null !=strings && null !=strings1 && strings1.size()>0 && strings.size()>0){
//            for(int i=0;i<strings.size();i++){
//                SecondLineRadioButton secondLineRadioButton = (SecondLineRadioButton) LayoutInflater.from(
//                        context).inflate(R.layout.layout_secondlint_radiobutton, null);
////                SecondLineRadioButton secondLineRadioButton = new SecondLineRadioButton(context);
//                secondLineRadioButton.setWidth(width/5);
//                secondLineRadioButton.setHeight(100);
//                secondLineRadioButton.setFirst_text(strings.get(i));
//                secondLineRadioButton.setSecond_text(strings1.get(i));
//                secondLineRadioButton.setFirst_size(context.getResources().getDimension(R.dimen.font_26));
//                secondLineRadioButton.setSecondTextSize(context.getResources().getDimension(R.dimen.font_24));
//                secondLineRadioButton.setTextColor(context.getResources().getColor(R.color.gray));
//                secondLineRadioButton.setSelectedColor(context.getResources().getColor(R.color.navigation_bar_bg));
//                rg_items.addView(secondLineRadioButton);
//                rb_items.add(secondLineRadioButton);
//            }
//        }
//
//    }
}
