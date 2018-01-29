package com.zhurui.bunnymall.wxapi;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.activity.PaySuccessActivity;
import com.zhurui.bunnymall.home.activity.PayTypeActivity;
import com.zhurui.bunnymall.mine.activity.MyWalletActivity;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp("wx310f2e849382569f");
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (baseResp.errCode == 0) {
                ToastUtils.show(this,"支付成功");
                Contants.paySuccess = true;
                if(Contants.isRecharge){
                    Contants.WXPAY_NUMBER=0;
                    Contants.isRecharge= false;
                    startActivity(new Intent(BaseApplication.getInstance(), MyWalletActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(BaseApplication.getInstance(), PaySuccessActivity.class).putExtra("total", Contants.WXPAY_NUMBER));
                    Contants.WXPAY_NUMBER=0;
                    finish();
                }

            }else if (baseResp.errCode == -2) {
                ToastUtils.show(this,"取消支付");
                finish();

            }

        }
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }



}