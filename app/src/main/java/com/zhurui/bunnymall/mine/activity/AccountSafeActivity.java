package com.zhurui.bunnymall.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountSafeActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.text_modifyaccount)
    TextView text_modifyaccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_safe);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        text_title.setText(R.string.info_safe);
        text_right.setVisibility(View.GONE);
        if(null !=BaseApplication.getInstance().getUser()){
            if(!"".equals(BaseApplication.getInstance().getUser().getMobile())){
                text_modifyaccount.setText(R.string.modify_phonenum);
            }else if(!"".equals(BaseApplication.getInstance().getUser().getEmail())){
                text_modifyaccount.setText(R.string.modify_email);
            }
        }
    }

    @OnClick(R.id.rel_modifypass)
    public void toModifyPass() {
        startActivity(new Intent(BaseApplication.getInstance(),ModifyPassActivity.class).putExtra("state",1),false);


    }

    @OnClick(R.id.rel_modifyphone)
    public void toModifyPhone() {
        startActivity(new Intent(BaseApplication.getInstance(),ModifyPassActivity.class).putExtra("state",2),true);
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
