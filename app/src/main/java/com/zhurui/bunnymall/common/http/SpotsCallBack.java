package com.zhurui.bunnymall.common.http;

import android.content.Context;
import android.text.TextUtils;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.utils.ToastUtils;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;


public abstract class SpotsCallBack<T> extends SimpleCallback<T> {
    private static SpotsDialog mDialog;
    private boolean isShowDialog = true;
    private Context context;
    public SpotsCallBack(Context context,String...first){
        super(context);
        this.context = context;
        if(first != null && first.length > 0){
            //暂无逻辑
            isShowDialog = false;
        }else{
            initSpotsDialog();
            isShowDialog = true;
        }
    }

    private  void initSpotsDialog(){
        mDialog = new SpotsDialog(context, R.style.mySpotsDialogDefault);
    }

    public  void showDialog(){
        if(mDialog != null){
            mDialog.show();
        }
    }

    public static void dismissDialog(){
        if(mDialog != null){
            mDialog.dismiss();
        }
    }


    public void setLoadMessage(int resId){
        mDialog.setMessage(context.getString(resId));
    }

    @Override
    public void onBeforeRequest(Request request) {
        if(isShowDialog){
            showDialog();
        }
    }

    @Override
    public void onResponse(Response response) {
        if(isShowDialog){
            dismissDialog();
        }
    }

    @Override
    public void onFailure(Request request, Exception e) {
        super.onFailure(request, e);
        if(isShowDialog){
            dismissDialog();
        }
        ToastUtils.show(context,"服务器忙，请稍后重试");
    }
}
