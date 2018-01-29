package com.zhurui.bunnymall.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.activity.PayTypeActivity;
import com.zhurui.bunnymall.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechargeActivity extends BaseActivity {
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.edit_sum)
    EditText edit_sum;
    @Bind(R.id.grid_sum)
    GridView grid_sum;
    private String[] sumstr ;
    @Bind(R.id.btn_recharge)
    Button btn_recharge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView(){
        text_title.setText(R.string.sum_recharge);
        text_right.setVisibility(View.GONE);
    }

    private void initData(){
        sumstr = new String[]{"100","300","500","1000","2000","3000"};
        grid_sum.setAdapter(new GridAdapter());
        grid_sum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edit_sum.setText(sumstr[position]);
            }
        });
    }

    @OnClick(R.id.btn_recharge)
    public void toRecharge(){
        String money = edit_sum.getText().toString().trim();
        if(null == money || "".equals(money)){
            ToastUtils.show(RechargeActivity.this,"请输入金额");
        }else {
            startActivity(new Intent(BaseApplication.getInstance(), PayTypeActivity.class).putExtra("isRecharge",true).putExtra("money",money),true);
        }

    }


    class  GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return sumstr.length;
        }

        @Override
        public Object getItem(int position) {
            return sumstr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(null == convertView){
                convertView = LayoutInflater.from(BaseApplication.getInstance()).inflate(R.layout.adapter_sum_recharge, null);
            }
            TextView text_sumcharge = (TextView) convertView.findViewById(R.id.text_sumcharge);
            text_sumcharge.setText(sumstr[position]+"元");
            return convertView;
        }
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view){
        finish();
    }
}
