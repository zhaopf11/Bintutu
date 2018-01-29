package com.zhurui.bunnymall.viewutils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.mine.activity.EnterMallActivity;

/**
 * Created by zhoux on 2017/8/7.
 */

public class DialogUtil {

    private OnClick onClick;
    public  Dialog dialog;
    private Dialog dialogLoading;

    public void setOnClick(OnClick onClick){
        this.onClick  = onClick;
    }

    public interface OnClick{
        void leftClick();
        void rightClick();
    }
    public void infoDialog(Context context, String info, boolean leftIsShow, boolean rightIsShow) {
        dialog = new Dialog(context, R.style.CustomDialog);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dv = inflater.inflate(R.layout.layout_infomind, null);
        TextView text_info = (TextView) dv.findViewById(R.id.text_info);
        TextView text_sure = (TextView) dv.findViewById(R.id.text_sure);
        TextView text_dismiss = (TextView) dv.findViewById(R.id.text_dismiss);
        text_info.setText(info);
        if (leftIsShow) {
            text_sure.setVisibility(View.VISIBLE);
        } else {
            text_sure.setVisibility(View.GONE);
        }
        if (rightIsShow) {
            text_dismiss.setVisibility(View.VISIBLE);
        } else {
            text_dismiss.setVisibility(View.GONE);
        }
        text_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.leftClick();
            }
        });
        text_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onClick.rightClick();
            }
        });
        dialog.setContentView(dv);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.show();
    }

    /**
     * 显示loading弹窗
     * @param context
     * @param msg
     * @return
     */
    public void showLoadingDialog(Context context, String msg) {
        dialogLoading = new Dialog(context, R.style.CustomDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.rotating);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息
        dialogLoading.setContentView(v);
        dialogLoading.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialogLoading.show();
    }

    /**
     * 更新版本dialog
     * @param context
     * @param title
     * @param content
     * @param leftIsShow
     * @param rightIsShow
     */
    public void updateDialog(Context context, String title, String content, boolean leftIsShow, boolean rightIsShow) {
        dialog = new Dialog(context, R.style.CustomDialog);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dv = inflater.inflate(R.layout.layout_update_infomind, null);
        TextView text_info = (TextView) dv.findViewById(R.id.text_info);
        TextView text_sure = (TextView) dv.findViewById(R.id.text_sure);
        TextView text_dismiss = (TextView) dv.findViewById(R.id.text_dismiss);
        TextView text_content = (TextView) dv.findViewById(R.id.text_content);
        text_info.setText(title);
        if(!TextUtils.isEmpty(content)){
            text_content.setVisibility(View.VISIBLE);
            text_content.setText(content);
        }
        if (leftIsShow) {
            text_sure.setVisibility(View.VISIBLE);
        } else {
            text_sure.setVisibility(View.GONE);
        }
        if (rightIsShow) {
            text_dismiss.setVisibility(View.VISIBLE);
        } else {
            text_dismiss.setVisibility(View.GONE);
        }
        text_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.leftClick();
            }
        });
        text_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.rightClick();
            }
        });
        dialog.setContentView(dv);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.show();
    }

    public void dismiss(){
        if(dialogLoading != null){
            dialogLoading.dismiss();
        }
    }
}
