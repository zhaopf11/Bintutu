package com.zhurui.bunnymall.mine.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.mine.adapter.CouponsAdapter;
import com.zhurui.bunnymall.mine.adapter.OrderAdapter;
import com.zhurui.bunnymall.mine.msg.CouponRespMsg;
import com.zhurui.bunnymall.mine.msg.OrderRespMsg;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.HotItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class MyCouponsActivity extends BaseActivity {
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.radiogroup_coupon)
    RadioGroup radiogroup_coupon;
    @Bind(R.id.recycler_coupons)
    RecyclerView recycler_coupons;
    private CouponsAdapter couponsAdapter;
    private int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupons);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        text_title.setText(R.string.coupon);
        text_right.setVisibility(View.GONE);
    }

    private void initData() {
        radiogroup_coupon.setOnCheckedChangeListener(onCheckedChangeListener);
        couponsAdapter = new CouponsAdapter(BaseApplication.getInstance());
        couponsAdapter.COUPON_STATE = 0;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        recycler_coupons.setLayoutManager(linearLayoutManager);
        recycler_coupons.addItemDecoration(new HotItemDecoration(20));
        recycler_coupons.setAdapter(couponsAdapter);
        couponsAdapter.setOnItemClickLitener(new CouponsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
        initRequestInfo();
    }


    private void initRequestInfo() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "50");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("pageno", "1");
            jsonObject.put("pagesize", "5");
            jsonObject.put("status", status + "");
            params.put("sendmsg", jsonObject.toString());
            getCouponList(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getCouponList(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<CouponRespMsg>(this) {
            @Override
            public void onSuccess(Response response, CouponRespMsg couponRespMsg) {
                if (couponRespMsg.getResult() > 0) {
                        couponsAdapter.couponBeans = couponRespMsg.getList();
                        couponsAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.show(MyCouponsActivity.this, couponRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(MyCouponsActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(MyCouponsActivity.this, "请求失败，请稍后重试");

            }
        });
    }

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                case R.id.radio_noused:
                    couponsAdapter.COUPON_STATE = 0;
                    status = 0;
                    initRequestInfo();
                    break;
                case R.id.radio_hasused:
                    couponsAdapter.COUPON_STATE = 1;
                    status = 1;
                    initRequestInfo();
                    break;
                case R.id.radio_haspassed:
                    couponsAdapter.COUPON_STATE = 2;
                    status = 2;
                    initRequestInfo();
                    break;
            }
        }
    };


    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
