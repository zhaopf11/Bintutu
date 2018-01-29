package com.zhurui.bunnymall.viewutils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.home.adapter.GirdDropDownAdapter;

import java.util.Arrays;

/**
 * Created by zhoux on 2017/8/2.
 */

public class MyDropDownMenuListDialog extends Dialog{
    private View view;
    private Context context;
    private MyListViewformesure list_gropdown;
    private LinearLayout lin_list;
    private String first[] = {"综合", "信誉", "人气高度", "新品上架"};
    private GirdDropDownAdapter firstAdapter;
    private int bottom;

    public MyDropDownMenuListDialog(@NonNull Context context, @StyleRes int themeResId,int bottom) {
        super(context, themeResId);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view= inflater.inflate(R.layout.mydropdownmenu_listdialog,null);
        this.context = context;
        this.bottom = bottom;
        initView();
        initData();
        setView();
    }

    private void initView(){
        lin_list =(LinearLayout)view.findViewById(R.id.lin_list);
        list_gropdown=(MyListViewformesure)view.findViewById(R.id.list_gropdown);
    }

    private void initData(){
        firstAdapter = new GirdDropDownAdapter(context, Arrays.asList(first));
        list_gropdown.setDividerHeight(0);
        list_gropdown.setAdapter(firstAdapter);
        list_gropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                firstAdapter.setCheckItem(position);
            }
        });
        lin_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setView(){

        view.setAlpha(1.0f);
        this.setContentView(view);
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        dialogWindow.setWindowAnimations(R.style.AnimTop);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x =0; // 新位置X坐标
        lp.y = bottom; // 新位置Y坐标
        lp.width = (int)context.getResources().getDisplayMetrics().widthPixels; // 宽度
        lp.height = (int)context.getResources().getDisplayMetrics().heightPixels-bottom;
        dialogWindow.setAttributes(lp);
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = list_gropdown.getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}
