package com.zhurui.bunnymall.home.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.bean.ProductInfoObject;
import com.zhurui.bunnymall.mine.adapter.CouponsAdapter;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.utils.Utils;
import com.zhurui.bunnymall.viewutils.HotItemDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavourableTypeActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.recycler_coupons)
    RecyclerView recycler_coupons;

    @Bind(R.id.text_points)
    TextView text_points;
    @Bind(R.id.edit_points)
    EditText edit_points;
    @Bind(R.id.text_fulloff)
    TextView text_fulloff;
    @Bind(R.id.checkbox_fulloff)
    CheckBox checkbox_fulloff;
    @Bind(R.id.rel_fulloff)
    RelativeLayout rel_fulloff;
    private CouponsAdapter couponsAdapter;
    private boolean isShow;
    @Bind(R.id.img_down)
    ImageView img_down;
    @Bind(R.id.btn_submit)
    Button btn_submit;
    private int preferential = 0;
    private int couponsposition = -1;
    private ProductInfoObject productInfoObject;
    private String value = "0";
    private float favourablemoney = 0;
    String showInfo = "";
    private int points = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourable_type);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        text_title.setText(R.string.favourable_type);
        text_right.setVisibility(View.GONE);
        recycler_coupons.setVisibility(View.GONE);
        edit_points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbox_fulloff.setChecked(false);
                isShow = false;
                recycler_coupons.setVisibility(View.GONE);
                img_down.setImageResource(R.drawable.down_normal);
                couponsposition = -1;
                preferential = 1;
                couponsAdapter.choosePosition = -1;

            }
        });

        edit_points.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(null !=edit_points.getText().toString().trim()  && !"".equals(edit_points.getText().toString().trim())){
                    int inputtext = Integer.parseInt(edit_points.getText().toString().trim());
                    if (inputtext>points) {
                        edit_points.setText(points+"");
                    }
                }
            }
        });

        checkbox_fulloff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edit_points.setText("");
                    isShow = false;
                    recycler_coupons.setVisibility(View.GONE);
                    img_down.setImageResource(R.drawable.down_normal);
                    couponsposition = -1;
                    preferential = 2;
                    couponsAdapter.choosePosition = -1;
                    if (null != productInfoObject.getPromotionactive() && productInfoObject.getPromotionactive().size() > 0) {
                        value = productInfoObject.getPromotionactive().get(0).getPromotionActiveID();

                    }
                }
            }
        });
    }

    private void initData() {
        productInfoObject = (ProductInfoObject) getIntent().getSerializableExtra("productInfoObject");
        String preferentialvalue = getIntent().getStringExtra("preferentialvalue");
        String preferen = getIntent().getStringExtra("preferential");
        if (null != productInfoObject.getPoint() && productInfoObject.getPoint().size() > 0) {
            points = Integer.parseInt(productInfoObject.getPoint().get(0).getPoint());
            text_points.setText("剩余积分：" + productInfoObject.getPoint().get(0).getPoint());
            preferential = 1;
        } else {
            edit_points.setEnabled(false);
        }
        if(preferentialvalue.equals("0")){
            edit_points.setText("");
        }else{
            edit_points.setText(preferentialvalue);
        }
        Utils.cursorToEnd(edit_points,edit_points.getText().toString());
        if (null != productInfoObject.getPromotionactive() && productInfoObject.getPromotionactive().size() > 0 && null != productInfoObject.getPromotionactive().get(0).getPromotionActiveID() && !"".equals(productInfoObject.getPromotionactive().get(0).getPromotionActiveID())) {
            text_fulloff.setText(productInfoObject.getPromotionactive().get(0).getName());
        } else {
            rel_fulloff.setVisibility(View.GONE);
        }
        couponsAdapter = new CouponsAdapter(BaseApplication.getInstance());
        couponsAdapter.COUPON_STATE = 3;
        couponsAdapter.couponBeans = productInfoObject.getCardList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        recycler_coupons.setLayoutManager(linearLayoutManager);
        recycler_coupons.addItemDecoration(new HotItemDecoration(20));
        recycler_coupons.setAdapter(couponsAdapter);
        if("2".equals(preferen)){
            checkbox_fulloff.setChecked(true);
        }
        couponsAdapter.setOnItemClickLitener(new CouponsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                couponsposition = position;
                if (null != productInfoObject.getCardList() && productInfoObject.getCardList().size() > 0) {
                    value = productInfoObject.getCardList().get(position).getCardID();
                    favourablemoney = Float.parseFloat(productInfoObject.getCardList().get(position).getMoney());
                    showInfo = productInfoObject.getCardList().get(position).getTitle();
                }

            }
        });
//        if(null !=productInfoObject.getCardList() && productInfoObject.getCardList().size()>0){
//            couponsAdapter.cardBeen = productInfoObject.getCardList();
//            couponsAdapter.notifyDataSetChanged();
//        }

    }

    @OnClick(R.id.rel_favourabletype)
    public void showCoupons() {
        if(productInfoObject.getCardList() != null && productInfoObject.getCardList().size() > 0){
            if (!isShow) {
                recycler_coupons.setVisibility(View.VISIBLE);
                img_down.setImageResource(R.drawable.up_normal);
                edit_points.setText("");
                checkbox_fulloff.setChecked(false);
                preferential = 3;
                couponsAdapter.notifyDataSetChanged();
            } else {
                recycler_coupons.setVisibility(View.GONE);
                img_down.setImageResource(R.drawable.down_normal);
            }
            isShow = !isShow;
        }else{
            ToastUtils.show(FavourableTypeActivity.this,"该商品没有优惠券");
        }
    }

    @OnClick(R.id.btn_submit)
    public void subMit() {
        Intent intent = new Intent();
        if (null == value || "".equals(value)) {
            value = "0";
        }

        switch (preferential) {
            case 0:
                intent.putExtra("value", "0");
                favourablemoney = 0;
                break;
            case 1:
                String points = edit_points.getText().toString().trim();
                if (null == points || "".equals(points)) {
                    points = "0";
                } else {
                    preferential = 1;
                }
                favourablemoney = Integer.parseInt(points) / (float) 100;
                intent.putExtra("value", points);
                showInfo = points;
                break;
            case 2:
                intent.putExtra("value", value);
                favourablemoney = Float.parseFloat(productInfoObject.getPromotionactive().get(0).getValue2());
                showInfo = productInfoObject.getPromotionactive().get(0).getName();
                break;
            case 3:
                intent.putExtra("value", value);
                break;
        }
        intent.putExtra("preferential", preferential);
        intent.putExtra("favourablemoney", favourablemoney);
        intent.putExtra("showInfo", showInfo);
        setResult(RESULT_OK, intent);
        finish();

    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
