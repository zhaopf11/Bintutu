package com.zhurui.bunnymall.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MakeInvoiceActivity extends BaseActivity {
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.radiogroup_type)
    RadioGroup radiogroup_type;
    @Bind(R.id.radio_personal)
    RadioButton radio_personal;
    @Bind(R.id.radio_company)
    RadioButton radio_company;
    @Bind(R.id.edit_name)
    EditText edit_name;
    @Bind(R.id.edit_num)
    EditText edit_num;
    @Bind(R.id.checkbox_invoice)
    CheckBox checkbox_invoice;

    private int invoiceflag = 0;
    private int invoicetypeid = 1;
    private String invoicetitle = "";
    private String invoicenum = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_invoice);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        text_title.setText(R.string.invoice_info);
        text_right.setText(R.string.salve);
        invoiceflag = getIntent().getIntExtra("invoiceflag", 0);
        invoicetypeid = getIntent().getIntExtra("invoicetypeid", 0);
        invoicetitle = getIntent().getStringExtra("invoicetitle");
        invoicenum = getIntent().getStringExtra("invoicenum");
        //当点击该控件时显示光标
        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_name.setCursorVisible(true);
            }
        });
        if (invoiceflag == 0) {
            checkbox_invoice.setChecked(false);
            edit_name.setEnabled(false);
            edit_num.setEnabled(false);
        } else {
            checkbox_invoice.setChecked(true);
            edit_name.setEnabled(true);
            edit_num.setEnabled(true);
        }
        if (invoicetypeid == 1) {
            if (!TextUtils.isEmpty(invoicetitle)) {
                edit_name.setText(invoicetitle);
            } else {
                edit_name.setHint("请输入个人姓名");
            }
            edit_num.setVisibility(View.GONE);
        } else if (invoicetypeid == 2) {
            radio_company.setChecked(true);
            if (!TextUtils.isEmpty(invoicetitle)) {
                edit_name.setText(invoicetitle);
                edit_num.setText(invoicenum);
                edit_num.setVisibility(View.VISIBLE);
            } else {
                edit_name.setHint("请输入公司名称");
                edit_num.setVisibility(View.VISIBLE);
            }
        }
        radiogroup_type.setOnCheckedChangeListener(onCheckedChangeListener);
        checkbox_invoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    invoiceflag = 1;
                    edit_name.setEnabled(true);
                    edit_num.setEnabled(true);
                } else {
                    invoiceflag = 0;
                    edit_name.setEnabled(false);
                    edit_num.setEnabled(false);
                }
            }
        });
    }


    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                case R.id.radio_personal:
                    edit_name.setHint("请输入个人姓名");
                    edit_num.setVisibility(View.GONE);
                    invoicetypeid = 1;
                    break;
                case R.id.radio_company:
                    edit_name.setHint("请输入公司名称");
                    edit_num.setVisibility(View.VISIBLE);
                    invoicetypeid = 2;
                    break;
            }
        }
    };

    @OnClick(R.id.text_right)
    public void salve() {
        String name = "";
        Intent intent = new Intent();
        if (1 == invoiceflag) {
            name = edit_name.getText().toString().trim();
            if (2 == invoicetypeid) {
                String num = edit_num.getText().toString().trim();
                intent.putExtra("invoicenum", num);
            }
        }

        intent.putExtra("invoiceflag", invoiceflag);
        intent.putExtra("invoicetypeid", invoicetypeid);
        intent.putExtra("invoicetitle", name);
        setResult(RESULT_OK, intent);
        finish();

    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
