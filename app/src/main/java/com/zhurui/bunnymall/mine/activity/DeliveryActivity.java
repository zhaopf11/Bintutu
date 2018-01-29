package com.zhurui.bunnymall.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.bean.CancleReasonBaen;
import com.zhurui.bunnymall.mine.bean.LogisticsCompanyBean;
import com.zhurui.bunnymall.mine.bean.LogisticsCompanyListBean;
import com.zhurui.bunnymall.mine.bean.OrderBean;
import com.zhurui.bunnymall.mine.bean.OrderProductDetaiListlBean;
import com.zhurui.bunnymall.mine.bean.OrderProductDetail;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.CancleReasonDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class DeliveryActivity extends BaseActivity {

    @Bind(R.id.logistics_company)
    TextView logistics_company;
    @Bind(R.id.logistics_number)
    EditText logistics_number;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.text_title)
    TextView text_title;

    @Bind(R.id.imgbtn_back)
    ImageView imgbtn_back;

    private CancleReasonDialog cancleReasonDialog;
    private String logisticsCompanyId;
    private String productid;
    private String shoporderid;
    List<LogisticsCompanyBean> logisticsCompanyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        ButterKnife.bind(this);
        initView();
        initInfo();
    }

    private void initView() {
        text_right.setText("确定");
        text_title.setText("退货发货");
        shoporderid = getIntent().getStringExtra("shoporderid");
        productid = getIntent().getStringExtra("productid");
    }

    @OnClick(R.id.logistics_company)
    public void toChooseCompany() {
        cancleReasonDialog = new CancleReasonDialog(DeliveryActivity.this, R.style.CustomDialog,false,true,logisticsCompanyList);
        cancleReasonDialog.setBtnOnClice(new CancleReasonDialog.BtnOnClick(){
            @Override
            public void btnOnClick(List<CancleReasonBaen> info) {
                if(info != null && info.size() > 0){
                    for(int i =0;i < info.size();i++){
                        if(info.get(i).getSelect()){
                            logisticsCompanyId = info.get(i).getReansonId();
                            logistics_company.setText(info.get(i).getReansonStr());
                        }
                    }
                }
                cancleReasonDialog.dismiss();
            }
        });
        cancleReasonDialog.show();
    }

    private void initInfo(){
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "73");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            params.put("sendmsg", jsonObject.toString());
            getLogisticsCompanyData(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getLogisticsCompanyData(Map<String, Object> params){
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<LogisticsCompanyListBean>(this) {
            @Override
            public void onSuccess(Response response, LogisticsCompanyListBean logisticsCompanyListBean) {
                if (logisticsCompanyListBean.getResult() > 0) {
                   if(logisticsCompanyListBean != null) {
                       logisticsCompanyList = logisticsCompanyListBean.getList();
                   }
                } else {
                    ToastUtils.show(DeliveryActivity.this, logisticsCompanyListBean.getRetmsg());
                }
            }
            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(DeliveryActivity.this, "请求失败，请稍后重试");
            }
            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(DeliveryActivity.this, "请求失败，请稍后重试");

            }
        });
    }
    @OnClick(R.id.text_right)
    public void sendGood(){
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        String deliverycode = logistics_number.getText().toString().trim();
        if(TextUtils.isEmpty(logisticsCompanyId)){
            ToastUtils.show(this,"请选择物流公司");
        }else if(TextUtils.isEmpty(deliverycode)){
            ToastUtils.show(this,"请输入物流单号");
        }else{
            try {
                jsonObject.put("cmd", "74");
                jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                jsonObject.put("token", BaseApplication.getInstance().getToken());
                jsonObject.put("shoporderid", shoporderid);
                jsonObject.put("productid", productid);
                jsonObject.put("logisticscompanyid", logisticsCompanyId);
                jsonObject.put("deliverycode", deliverycode);
                params.put("sendmsg", jsonObject.toString());
                sendGood(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendGood(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    ToastUtils.show(DeliveryActivity.this, "发送成功");
                    finish();
                } else {
                    ToastUtils.show(DeliveryActivity.this, baseRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(DeliveryActivity.this, "处理失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(DeliveryActivity.this, "处理失败，请稍后重试");

            }
        });
    }
    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
