package com.zhurui.bunnymall.home.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhurui.bunnymall.MainActivity;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.mine.activity.OrderDetailActivity;
import com.zhurui.bunnymall.mine.activity.OrdersActivity;
import com.zhurui.bunnymall.utils.Contants;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaySuccessActivity extends BaseActivity {
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;

    @Bind(R.id.text_successmoney)
    TextView text_successmoney;
    @Bind(R.id.text_info)
    TextView text_info;
    @Bind(R.id.text_orders)
    TextView text_orders;
    @Bind(R.id.text_home)
    TextView text_home;
    @Bind(R.id.lin_orders)
    LinearLayout lin_orders;
    private float total;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        ButterKnife.bind(this);
        initView();
    }
    private void initView(){
        text_title.setText(R.string.pay_success);
        text_right.setVisibility(View.GONE);
        total = getIntent().getFloatExtra("total",total);
        text_successmoney.setText("您已成功支付"+decimalFormat.format(total)+"元");
    }

    @OnClick(R.id.text_home)
    public void toMain(){
        startActivity(new Intent(BaseApplication.getInstance(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),false);
        finish();
    }

    @OnClick(R.id.text_orders)
    public void toOrderDetail(){
        if(Contants.shopOrderIDList.size() > 1){
            //当是多条的时候跳转到订单列表
            startActivity(new Intent(BaseApplication.getInstance(), OrdersActivity.class).putExtra("orderstate", 2),true);
        }else{
            //否则跳转到订单详情
            startActivity(new Intent(BaseApplication.getInstance(), OrderDetailActivity.class).putExtra("shoporderid", Contants.shopOrderID),true);
        }
    }
    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view){
        finish();
    }
}
