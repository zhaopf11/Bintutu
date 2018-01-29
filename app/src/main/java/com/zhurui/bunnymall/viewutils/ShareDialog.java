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
import android.widget.Button;
import android.widget.LinearLayout;

import com.zhurui.bunnymall.R;

/**
 * Created by zhoux on 2017/7/28.
 */

public class ShareDialog extends Dialog {

    private View view;
    private Context context;
    private LinearLayout lin_qqshare;
    private LinearLayout lin_wechatshare;
    private LinearLayout lin_friendshare;
    private LinearLayout lin_sinashare;
    private Button btn_dismiss;
    private OnItemClick onItemClick;

    public ShareDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view= inflater.inflate(R.layout.dialog_share,null);
        this.context = context;
        initView();
        setView();
    }


    private void initView(){
        lin_qqshare =(LinearLayout) view.findViewById(R.id.lin_qqshare);
        lin_wechatshare =(LinearLayout) view.findViewById(R.id.lin_wechatshare);
        lin_friendshare =(LinearLayout) view.findViewById(R.id.lin_friendshare);
        lin_sinashare =(LinearLayout) view.findViewById(R.id.lin_sinashare);
        btn_dismiss = (Button)view.findViewById(R.id.btn_dismiss);
        lin_qqshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClick(1);
                dismiss();

            }
        });

        lin_wechatshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClick(2);
                dismiss();

            }
        });

        lin_friendshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClick(3);
                dismiss();

            }
        });

        lin_sinashare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClick(4);
                dismiss();

            }
        });

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public interface OnItemClick{
        void onItemClick(int position);
    }


    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
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
