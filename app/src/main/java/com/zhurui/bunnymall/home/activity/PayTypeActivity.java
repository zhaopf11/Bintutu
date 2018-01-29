package com.zhurui.bunnymall.home.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.cart.bean.CartProductBean;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.MakeOrderBean;
import com.zhurui.bunnymall.home.bean.ProductDetailBean;
import com.zhurui.bunnymall.home.bean.WeChatRespMsg;
import com.zhurui.bunnymall.mine.activity.MyWalletActivity;
import com.zhurui.bunnymall.mine.bean.OrderBean;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import okhttp3.Response;

public class PayTypeActivity extends BaseActivity {


    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;

    @Bind(R.id.rel_balance)
    RelativeLayout rel_balance;
    @Bind(R.id.text_balance)
    TextView text_balance;

    @Bind(R.id.checkbox_balance)
    CheckBox checkBox_balance;
    @Bind(R.id.checkbox_alipay)
    CheckBox checkbox_alipay;
    @Bind(R.id.checkbox_wechat)
    CheckBox checkbox_wechat;
    private static final int SDK_PAY_FLAG = 1;

    //是否是从充值
    boolean isRecharge = false;
    ProductDetailBean productDetailBean = null;
    MakeOrderBean result;
    private String shopOrderID;
    private String transactionid;
    private float usermoney;
    private float total;
    private int paytypeid = 3;
    private String money;
    private boolean buyNow;
    private List<CartProductBean> cartProductBeen;
    private float needPayMoney = 0;
    private boolean isOrders = false;
    private List<OrderBean> orderBeanList;
    private IWXAPI api;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private List<String> shopOrderIDList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_type);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        api = WXAPIFactory.createWXAPI(PayTypeActivity.this, null);
        api.registerApp("wx310f2e849382569f");
        text_title.setText(R.string.pay_sure);
        text_right.setVisibility(View.GONE);
        isRecharge = getIntent().getBooleanExtra("isRecharge", false);
        isOrders = getIntent().getBooleanExtra("isOrders", false);
        if (isRecharge) {
            //充值
            paytypeid = 0;
            rel_balance.setVisibility(View.GONE);
            money = getIntent().getStringExtra("money");
        } else {
            paytypeid = 0;
            buyNow = getIntent().getBooleanExtra("buyNow", false);
            result = (MakeOrderBean) getIntent().getSerializableExtra("result");
            if (result != null) {
                if (result.getNeedpaymoney() != null) {
                    total = Float.parseFloat(result.getNeedpaymoney());
                }
                if(result.getShoporderid().contains(",")){
                    String[] shIdList = result.getShoporderid().split(",");
                    for(int i=0;i<shIdList.length;i++){
                        shopOrderIDList.add(shIdList[i]);
                    }
                }else{
                    shopOrderID = result.getShoporderid();
                    shopOrderIDList.add(shopOrderID);
                }
                transactionid = result.getTransactionid();
                if (null != result.getUsermoney()) {
                    usermoney = Float.parseFloat(result.getUsermoney());
                }
            }
            if (buyNow) {
                //立即订购
                productDetailBean = (ProductDetailBean) getIntent().getSerializableExtra("product");
            } else if (isOrders) {
                //订单列表支付
                shopOrderID = getIntent().getStringExtra("shopOrderID");
                transactionid = getIntent().getStringExtra("transactionid");
                if (getIntent().getStringExtra("needPayMoney") != null) {
                    total = Float.parseFloat(getIntent().getStringExtra("needPayMoney"));
                }
                if (getIntent().getStringExtra("usermoney") != null) {
                    usermoney = Float.parseFloat(getIntent().getStringExtra("usermoney"));
                }
                orderBeanList = (List<OrderBean>) getIntent().getSerializableExtra("orderBeanList");
            } else {
                //购物车付款
                cartProductBeen = (List<CartProductBean>) getIntent().getSerializableExtra("product");
            }

            if (usermoney < 0) {
                usermoney = 0;
            }
            if (total < 0) {
                total = 0;
            }
            text_balance.setText("余额：" + decimalFormat.format(usermoney));
            checkBox_balance.setChecked(true);
            checkBox_balance.setEnabled(false);
            if (total <= usermoney) {
                checkbox_alipay.setEnabled(false);
                checkbox_wechat.setEnabled(false);
                needPayMoney = total;
            } else {
                needPayMoney = total - usermoney;
            }
        }
        Contants.shopOrderID = shopOrderID;
        Contants.shopOrderIDList = shopOrderIDList;
    }

    @OnCheckedChanged(R.id.checkbox_alipay)
    public void chooseAlipay() {
        if (checkbox_alipay.isChecked()) {
            checkbox_wechat.setChecked(false);
            paytypeid = 3;
        }else{
            paytypeid = 0;
        }
    }

    @OnCheckedChanged(R.id.checkbox_wechat)
    public void chooseWechat() {
        if (checkbox_wechat.isChecked()) {
            checkbox_alipay.setChecked(false);
            paytypeid = 15;
        }else{
            paytypeid = 0;
        }
    }

    @OnClick(R.id.btn_submit)
    public void toSuccess() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        if (isRecharge) {
            if(paytypeid == 0){
                ToastUtils.show(this,"请选择支付方式");
                return;
            }
            try {
                jsonObject.put("cmd", "49");
                jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                jsonObject.put("token", BaseApplication.getInstance().getToken());
                jsonObject.put("money", money);
                jsonObject.put("paytypeid", paytypeid);
                params.put("sendmsg", jsonObject.toString());
                if (3 == paytypeid) {
                    getAlipayInfoData(params);

                } else if (15 == paytypeid) {
                    Contants.WECHAT_PAY = true;
                    getWechatInfoData(params);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            if(paytypeid == 0 && usermoney < needPayMoney){
                ToastUtils.show(this,"余额不足，请选择其他的支付方式");
                return;
            }
            try {
                jsonObject.put("cmd", "27");
                jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                jsonObject.put("token", BaseApplication.getInstance().getToken());
                jsonObject.put("paytypeid", paytypeid);
                jsonObject.put("amount", decimalFormat.format(needPayMoney) + "");
//                        jsonObject.put("amount",  0.01+ "");
                jsonObject.put("transactionid", transactionid);
                if (buyNow) {
                    jsonObject.put("productids", productDetailBean.getProductID());
                    jsonObject.put("names", productDetailBean.getName());
                    jsonObject.put("infos", productDetailBean.getInfo());
                } else if (isOrders) {
                    if (orderBeanList != null && orderBeanList.size() > 0) {
                        String productids = "";
                        for (OrderBean orderBean : orderBeanList) {
                            productids = productids + orderBean.getProductID() + ",";
                        }
                        productids = productids.substring(0,productids.length()-1);//将字符串最后面的逗号去掉
                        jsonObject.put("productids", productids);
                        if (orderBeanList.size() == 1) {
                            jsonObject.put("names", orderBeanList.get(0).getName());
                        } else {
                            jsonObject.put("names", orderBeanList.get(0).getName() + "等");
                        }
                    }
                    jsonObject.put("infos", "");
                } else {
                    String ids = "";
                    for (CartProductBean cartProductBean : cartProductBeen) {
                        ids = ids + cartProductBean.getProductID() + ",";
                    }
                    ids = ids.substring(0,ids.length()-1);
                    jsonObject.put("productids", ids);
                    jsonObject.put("names", cartProductBeen.get(0).getProductName() + "等");
                    jsonObject.put("infos", "");
                }
                params.put("sendmsg", jsonObject.toString());
                if (0 == paytypeid || 3 == paytypeid) {
                    getAlipayInfoData(params);
                } else if (15 == paytypeid) {
                    Contants.WECHAT_PAY = true;
                    getWechatInfoData(params);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getAlipayInfoData(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (null != baseRespMsg && baseRespMsg.getResult() > 0) {
                    if (0 == paytypeid) {

                        ToastUtils.show(PayTypeActivity.this, "支付成功");
                        startActivity(new Intent(BaseApplication.getInstance(), PaySuccessActivity.class).putExtra("total", total), true);
                        finish();
                    } else if (3 == paytypeid) {
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(PayTypeActivity.this);
                                Map<String, String> result = alipay.payV2(baseRespMsg.getRetmsg(), true);
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    }

                } else {
                    ToastUtils.show(PayTypeActivity.this, baseRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(PayTypeActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(PayTypeActivity.this, "请求失败，请稍后重试");

            }
        });
    }


    private void getWechatInfoData(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<WeChatRespMsg>(this) {
            @Override
            public void onSuccess(Response response, WeChatRespMsg weChatRespMsg) {
                if (null != weChatRespMsg && weChatRespMsg.getResult() > 0) {
                    if (isRecharge) {
                        Contants.WXPAY_NUMBER = Float.parseFloat(money);

                    } else {
                        Contants.WXPAY_NUMBER = total;
                    }
                    Contants.isRecharge = isRecharge;
                    PayReq req = new PayReq();
                    req.appId = weChatRespMsg.getRetmsg().getAppid();
                    req.prepayId = weChatRespMsg.getRetmsg().getPrepayid();
                    req.nonceStr = weChatRespMsg.getRetmsg().getNoncestr();
                    req.partnerId = weChatRespMsg.getRetmsg().getPartnerid();
                    req.timeStamp = weChatRespMsg.getRetmsg().getTimestamp();
                    req.packageValue = "Sign=WXPay";
                    req.sign = weChatRespMsg.getRetmsg().getSign();
                    api.sendReq(req);
                    Contants.WECHAT_PAY = false;
                } else {
                    Contants.WECHAT_PAY = false;
                    ToastUtils.show(PayTypeActivity.this, "提交失败");
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(PayTypeActivity.this, "请求失败，请稍后重试");
                Contants.WECHAT_PAY = false;

            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(PayTypeActivity.this, "请求失败，请稍后重试");
                Contants.WECHAT_PAY = false;

            }

        });
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    Map<String, String> payResultstr = (Map<String, String>) msg.obj;
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultStatus = payResultstr.get("resultStatus");
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayTypeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();

                        if (isRecharge) {
                            startActivity(new Intent(BaseApplication.getInstance(), MyWalletActivity.class), true);
                            finish();
                        } else {
                            startActivity(new Intent(BaseApplication.getInstance(), PaySuccessActivity.class).putExtra("total", total), true);
                            finish();
                        }
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayTypeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if (Contants.paySuccess) {
            Contants.paySuccess = false;
            finish();
        }
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
