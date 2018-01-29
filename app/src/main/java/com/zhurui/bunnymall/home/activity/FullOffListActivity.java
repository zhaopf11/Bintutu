package com.zhurui.bunnymall.home.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.adapter.ProductGridAdapter;
import com.zhurui.bunnymall.home.bean.FullOffProductBean;
import com.zhurui.bunnymall.home.bean.Product;
import com.zhurui.bunnymall.utils.TimeUtils;
import com.zhurui.bunnymall.viewutils.Anticlockwise;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FullOffListActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.text_name)
    TextView text_name;
    @Bind(R.id.anticlockwise)
    Anticlockwise anticlockwise;
    @Bind(R.id.grid_product)
    GridView grid_product;

    private int state;
    private List<FullOffProductBean> fullOffProductBeen = null;
    private String theme;
    private List<Product> products = null;
    private ProductGridAdapter productGridAdapter;
    private long startTime;
    private long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_off_list);
        ButterKnife.bind(this);
        initView();
        initDate();
    }

    private void initView() {
        theme = getIntent().getStringExtra("theme");
        text_title.setText(theme);
        text_right.setVisibility(View.GONE);
        text_name.setText("距离"+theme+"满减活动结束还有");

    }

    private void initDate() {
        state = getIntent().getIntExtra("state", 0);
        long time = 0;
        long currentTime = 0;
        try {
            currentTime = TimeUtils.getCurrentTimeMillis();
            startTime = getIntent().getLongExtra("startTime", 0);
            endTime = getIntent().getLongExtra("endTime", 0);
            if (currentTime > startTime && currentTime < endTime) {
                time = (endTime - currentTime) / 1000;
            }
            long day = time / ( 3600 * 24 );
            long hour = time % ( 24 * 3600 ) / 3600;
            long minute = time % 3600 / 60;
            anticlockwise.setText(day + "天" + hour +"时" + minute + "分");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        long currentTime = System.currentTimeMillis();
//        long time = 0;
//        startTime = getIntent().getLongExtra("startTime", 0);
//        endTime = getIntent().getLongExtra("endTime", 0);
//        if (currentTime > startTime && currentTime < endTime) {
//            time = endTime - currentTime;
//        }
//        anticlockwise.initTime(time/1000);
//        anticlockwise.setTimeFormat("dd天HH时mm分");
//        anticlockwise.reStart();
//        anticlockwise.setOnTimeCompleteListener(new Anticlockwise.OnTimeCompleteListener() {
//            @Override
//            public void onTimeComplete() {
//
//            }
//        });
        fullOffProductBeen = (List<FullOffProductBean>) getIntent().getSerializableExtra("productlist");
        products = new ArrayList<>();
        for (FullOffProductBean fullOffProductBean : fullOffProductBeen) {
            Product product = new Product();
            product.setPrice(fullOffProductBean.getPrice());
            product.setMainImage(fullOffProductBean.getMainImage());
            product.setName(fullOffProductBean.getName());
            product.setNormalPrice(fullOffProductBean.getProductPrice());
            products.add(product);
        }

        if (null != products && products.size() > 0) {
            productGridAdapter = new ProductGridAdapter(BaseApplication.getInstance(), state, products);
            grid_product.setAdapter(productGridAdapter);
            grid_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (1 == state) {
                        startActivity(new Intent(BaseApplication.getInstance(), ProductNewDetailActivity.class), false);
                    } else {
                        startActivity(new Intent(BaseApplication.getInstance(), ProductNewDetailActivity.class).putExtra("productType",4).putExtra("productId",Long.parseLong(fullOffProductBeen.get(position).getProductID())), false);

                    }
                }
            });
        }


    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
