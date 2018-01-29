package com.zhurui.bunnymall.mine.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.bean.AddressBean;
import com.zhurui.bunnymall.mine.bean.ProvinceModel;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.RegexUtil;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.ProvinceDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class EditAddressActivity extends BaseActivity {
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.edit_name)
    EditText edit_name;
    @Bind(R.id.edit_phone)
    EditText edit_phone;
    @Bind(R.id.edit_province)
    TextView edit_province;
    @Bind(R.id.edit_addressdetail)
    EditText edit_addressdetail;
    @Bind(R.id.checkbox_normal)
    CheckBox checkbox_normal;
    private ProvinceDialog provinceDialog = null;
    private int state;
    private String isDefaule = "0";
    private AddressBean addressBean = null;
    private String mProvince;
    private String mCity;
    private String mCounty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        state = getIntent().getIntExtra("state", 0);
        //1：新建2：编辑3：查看
        if (2 == state) {
            text_title.setText(R.string.edit_address);
            text_right.setText(R.string.salve);
            addressBean = (AddressBean) getIntent().getSerializableExtra("address");
            edit_name.setText(addressBean.getShouHuoRen());
            edit_phone.setText(addressBean.getMobile());
            edit_province.setText(addressBean.getProvinceName()+addressBean.getCityName()+addressBean.getTownName());
            mProvince = addressBean.getProvinceName();
            mCity = addressBean.getCityName();
            mCounty = addressBean.getTownName();
            edit_addressdetail.setText(addressBean.getAddress());
            isDefaule = addressBean.getDefaultFlag();
            if("1".equals(isDefaule)){
                checkbox_normal.setChecked(true);
                checkbox_normal.setEnabled(false);
            }
        } else {
            text_title.setText(R.string.manager_address);
            if (1 == state) {
                text_right.setText(R.string.salve);
            } else {
                text_right.setVisibility(View.GONE);
                checkbox_normal.setVisibility(View.GONE);
                edit_name.setEnabled(false);
                edit_phone.setEnabled(false);
                edit_province.setEnabled(false);
                edit_addressdetail.setEnabled(false);
                addressBean = (AddressBean) getIntent().getSerializableExtra("address");
                edit_name.setText(addressBean.getShouHuoRen());
                edit_phone.setText(addressBean.getMobile());
                edit_province.setText(addressBean.getProvinceName()+addressBean.getCityName()+addressBean.getTownName());
                edit_addressdetail.setText(addressBean.getAddress());
            }

        }
        checkbox_normal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isDefaule = "1";
                }else {
                    isDefaule = "0";
                }
            }
        });
    }

    @OnClick(R.id.edit_province)
    public void showProvince() {
        provinceDialog = new ProvinceDialog(EditAddressActivity.this, R.style.CustomDialog);
        provinceDialog.setBtnOnClice(new ProvinceDialog.BtnOnClick() {
            @Override
            public void btnOnClick(String province,String city,String county) {
                mProvince = province;
                mCity = city;
                mCounty = county;
                edit_province.setText(province + city + county);
            }
        });
        provinceDialog.show();

    }

    @OnClick(R.id.text_right)
    public void salveInfo() {
        String name = edit_name.getText().toString().trim();
        String phone = edit_phone.getText().toString().trim();
        String address = edit_province.getText().toString().trim();
        String addressDetail = edit_addressdetail.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.show(EditAddressActivity.this, "请输入收货人姓名");
        } else if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(EditAddressActivity.this, "请输入手机号码");

        } else if (!RegexUtil.isMobileNO(phone)) {
            ToastUtils.show(EditAddressActivity.this, "请输入正确的手机号码");
        } else if (TextUtils.isEmpty(address)) {
            ToastUtils.show(EditAddressActivity.this, "请选择所在城市");

        } else if (TextUtils.isEmpty(addressDetail)) {
            ToastUtils.show(EditAddressActivity.this, "请输入详细地址");
        } else {
            Map<String, Object> params = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            switch (state) {
                case 1:
                    try {
                        jsonObject.put("cmd", "18");
                        jsonObject.put("typeid", "1");
                        jsonObject.put("addressID", "0");
                        jsonObject.put("shouHuoRen", name);
                        jsonObject.put("mobile", phone);
                        jsonObject.put("provinceName", mProvince);
                        jsonObject.put("cityName",mCity);
                        jsonObject.put("townName", mCounty);
                        jsonObject.put("address", addressDetail);
                        jsonObject.put("uid",BaseApplication.getInstance().getUser().getUserID());
                        jsonObject.put("token",BaseApplication.getInstance().getToken());
                        jsonObject.put("defaultFlag",isDefaule);
                        params.put("sendmsg", jsonObject.toString());
                        editAddress(params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        jsonObject.put("cmd", "18");
                        jsonObject.put("typeid", "2");
                        jsonObject.put("addressID", addressBean.getAddressID()+"");
                        jsonObject.put("shouHuoRen", name);
                        jsonObject.put("mobile", phone);
                        jsonObject.put("provinceName", mProvince);
                        jsonObject.put("cityName",mCity);
                        jsonObject.put("townName", mCounty);
                        jsonObject.put("address", addressDetail);
                        jsonObject.put("uid",BaseApplication.getInstance().getUser().getUserID());
                        jsonObject.put("token",BaseApplication.getInstance().getToken());
                        jsonObject.put("defaultFlag",isDefaule);
                        params.put("sendmsg", jsonObject.toString());
                        editAddress(params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void editAddress(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    finish();
                } else {
                    ToastUtils.show(EditAddressActivity.this, baseRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(EditAddressActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(EditAddressActivity.this, "请求失败，请稍后重试");

            }
        });
    }



    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }


}
