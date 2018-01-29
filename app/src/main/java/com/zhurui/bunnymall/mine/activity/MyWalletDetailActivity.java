package com.zhurui.bunnymall.mine.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.mine.adapter.WalletDetailAdapter;
import com.zhurui.bunnymall.mine.msg.BalanceDetailRespMsg;
import com.zhurui.bunnymall.mine.msg.PointDetailRespMsg;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.FullOffDecoration;
import com.zhurui.bunnymall.viewutils.HotItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shihao.library.XRadioGroup;
import okhttp3.Response;

public class MyWalletDetailActivity extends BaseActivity {
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.radiogroup_detail)
    XRadioGroup radiogroup_detail;
    @Bind(R.id.recycler_detail)
    RecyclerView recycler_detail;
    private WalletDetailAdapter walletDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet_detail);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        text_title.setText(R.string.wallet_detail);
        text_right.setVisibility(View.GONE);
    }

    private void initData() {
        radiogroup_detail.setOnCheckedChangeListener(onCheckedChangeListener);
        walletDetailAdapter = new WalletDetailAdapter(BaseApplication.getInstance());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        recycler_detail.setLayoutManager(linearLayoutManager);
        recycler_detail.addItemDecoration(new FullOffDecoration(2));
        recycler_detail.setAdapter(walletDetailAdapter);
        initRequestInfo();
    }

    private void initRequestInfo() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "51");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("pageno", "1");
            jsonObject.put("pagesize", "50");
            params.put("sendmsg", jsonObject.toString());
            getBalanceList(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getBalanceList(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BalanceDetailRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BalanceDetailRespMsg balanceDetailRespMsg) {
                if (balanceDetailRespMsg.getResult() > 0) {
                    walletDetailAdapter.isBalance = true;
                    walletDetailAdapter.balanceDetailBeen = balanceDetailRespMsg.getList();
                    walletDetailAdapter.notifyDataSetChanged();


                } else {
                    ToastUtils.show(MyWalletDetailActivity.this, balanceDetailRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(MyWalletDetailActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(MyWalletDetailActivity.this, "请求失败，请稍后重试");

            }
        });
    }


    private void initPointInfo() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "57");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("pageno", "1");
            jsonObject.put("pagesize", "50");
            params.put("sendmsg", jsonObject.toString());
            getPointList(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getPointList(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<PointDetailRespMsg>(this) {
            @Override
            public void onSuccess(Response response, PointDetailRespMsg pointDetailRespMsg) {
                if (pointDetailRespMsg.getResult() > 0) {
                    walletDetailAdapter.isBalance = false;
                    walletDetailAdapter.pointDetailBeen =pointDetailRespMsg.getList();
                    walletDetailAdapter.notifyDataSetChanged();


                } else {
                    ToastUtils.show(MyWalletDetailActivity.this, pointDetailRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(MyWalletDetailActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(MyWalletDetailActivity.this, "请求失败，请稍后重试");

            }
        });
    }

    XRadioGroup.OnCheckedChangeListener onCheckedChangeListener = new XRadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(XRadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                case R.id.radio_balance:
                    initRequestInfo();
                    break;
                case R.id.radio_points:
                    initPointInfo();
                    break;
            }
        }
    };

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
