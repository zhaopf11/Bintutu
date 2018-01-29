package com.zhurui.bunnymall.viewutils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.home.adapter.ProductInfoDialogAdapter;
import com.zhurui.bunnymall.home.bean.PropertyChildBean;
import com.zhurui.bunnymall.home.bean.PropertyGroupBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoux on 2017/7/28.
 */

public class ProductInfoDialog extends Dialog {

    private View view;
    private Context context;
    private RecyclerView recycler_productinfo;
    private Button btn_dismiss;
    private ProductInfoDialogAdapter productInfoDialogAdapter;
    private Map<String,List<PropertyChildBean>> propertyChildMap;
    private List<PropertyGroupBean> propertyGroupBeen;
    private List<String> valueList = null;
    public ProductInfoDialog(@NonNull Context context, @StyleRes int themeResId, Map<String,List<PropertyChildBean>> propertyChildMap, List<PropertyGroupBean> groupBeen) {
        super(context, themeResId);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view= inflater.inflate(R.layout.dialog_productinfo,null);
        this.context = context;
        this.propertyChildMap = propertyChildMap;
        this.propertyGroupBeen = groupBeen;
        initView();
        setView();
    }

    private void initView(){
        recycler_productinfo = (RecyclerView) view.findViewById(R.id.recycler_productinfo);
        btn_dismiss = (Button)view.findViewById(R.id.btn_dismiss);
        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        productInfoDialogAdapter = new ProductInfoDialogAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recycler_productinfo.setLayoutManager(linearLayoutManager);
        recycler_productinfo.setAdapter(productInfoDialogAdapter);
        initData();
    }

    private void initData(){
        valueList = new ArrayList<>();
        for (PropertyGroupBean propertyGroupBean:propertyGroupBeen){
            String value ="";
            for (PropertyChildBean propertyChildBean:propertyChildMap.get(propertyGroupBean.getCustomPropertiesID())){
                value = value+propertyChildBean.getName()+"、";
            }
            valueList.add(value.substring(0,value.lastIndexOf("、")));
        }
        productInfoDialogAdapter.propertyGroupBeen = propertyGroupBeen;
        productInfoDialogAdapter.valueList = valueList;
        productInfoDialogAdapter.notifyDataSetChanged();

    }

    private void setView(){

        view.setAlpha(1.0f);
        this.setContentView(view);
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.AnimBottom);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int)context.getResources().getDisplayMetrics().widthPixels; // 宽度
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
}
