package com.zhurui.bunnymall.viewutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhurui.bunnymall.R;

/**
 * Created by zhaopf on 2017/10/25 0025.
 */

public class SelectPicPopupWindow extends PopupWindow {
    private View view;
    private RelativeLayout rlt_cart;
    private RelativeLayout rlt_store;
    private RelativeLayout rlt_message;
    private RelativeLayout rlt_share;
    public static CheckBox menu_collection;
    public SelectPicPopupWindow(Activity context, View.OnClickListener itemsOnClick,String isFave) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_popup2, null);
        rlt_cart = (RelativeLayout) view.findViewById(R.id.rlt_cart);
        rlt_store = (RelativeLayout) view.findViewById(R.id.rlt_store);
        rlt_message = (RelativeLayout) view.findViewById(R.id.rlt_message);
        rlt_share = (RelativeLayout) view.findViewById(R.id.rlt_share);

        //为控件添加监听
        rlt_cart.setOnClickListener(itemsOnClick);
        rlt_store.setOnClickListener(itemsOnClick);
        rlt_message.setOnClickListener(itemsOnClick);
        rlt_share.setOnClickListener(itemsOnClick);

        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // view添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
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
}
