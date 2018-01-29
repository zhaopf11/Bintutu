package com.zhurui.bunnymall.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.mine.msg.BalanceDetailRespMsg;
import com.zhurui.bunnymall.mine.msg.WalletRespMsg;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class MyWalletActivity extends BaseActivity {
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.text_balance)
    TextView text_balance;
    @Bind(R.id.text_points)
    TextView text_points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        ButterKnife.bind(this);
        initView();
    }
    private void initView(){
        text_title.setText(R.string.my_wallet);
        text_right.setText(R.string.detail);
    }


    private void initData(){
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "58");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            params.put("sendmsg", jsonObject.toString());
            geWalletInfo(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void geWalletInfo(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<WalletRespMsg>(this) {
            @Override
            public void onSuccess(Response response, WalletRespMsg walletRespMsg) {
                if (walletRespMsg.getResult() > 0) {

                    if(null !=walletRespMsg.getList() && walletRespMsg.getList().size()>0){

                        if(null !=walletRespMsg.getList().get(0).getUserMoney()  && !"".equals(walletRespMsg.getList().get(0).getUserMoney())){
                            text_balance.setText(walletRespMsg.getList().get(0).getUserMoney());

                        }

                        if(null !=walletRespMsg.getList().get(0).getPoint() && !"".equals(walletRespMsg.getList().get(0).getPoint())){
                            text_points.setText(walletRespMsg.getList().get(0).getPoint());
                        }
                    }else {
                        text_balance.setText("0.00");
                        text_points.setText("0");
                    }


                } else {
                    ToastUtils.show(MyWalletActivity.this, walletRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(MyWalletActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(MyWalletActivity.this, "请求失败，请稍后重试");

            }
        });
    }


    @OnClick(R.id.text_right)
    public void toDetail(){
        startActivity(new Intent(BaseApplication.getInstance(), MyWalletDetailActivity.class),true);

    }

    @OnClick(R.id.rel_recharge)
    public void toRecharge(){
        startActivity(new Intent(BaseApplication.getInstance(), RechargeActivity.class),true);


    }
    @OnClick(R.id.rel_coupon)
    public void toCoupon(){
        startActivity(new Intent(BaseApplication.getInstance(), MyCouponsActivity.class),true);

    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view){
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
