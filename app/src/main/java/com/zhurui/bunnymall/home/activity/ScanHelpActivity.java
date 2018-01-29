package com.zhurui.bunnymall.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanHelpActivity extends BaseActivity {

    @Bind(R.id.imgbtn_back)
    ImageButton imageButton;
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_help);
        ButterKnife.bind(this);
        text_title.setText(R.string.help);
        text_right.setVisibility(View.GONE);
    }
    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view){
        finish();
    }

}
