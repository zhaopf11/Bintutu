package com.zhurui.bunnymall.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.activity.ProductGridNormalActivity;
import com.zhurui.bunnymall.home.activity.SearchActivity;
import com.zhurui.bunnymall.home.msg.SearchResp;
import com.zhurui.bunnymall.mine.adapter.AddressAdapter;
import com.zhurui.bunnymall.mine.bean.AddressBean;
import com.zhurui.bunnymall.mine.msg.AddressMsg;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.DialogUtil;
import com.zhurui.bunnymall.viewutils.HotItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class ManagerAddressActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.recycler_address)
    RecyclerView recycler_address;
    private AddressAdapter addressAdapter;
    private int typeid = 0;
    private List<AddressBean> addressBeanList =null;
    private boolean fromOrder= false;
    private int deletePosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_address);
        ButterKnife.bind(this);
        initView();

    }



    private void initView(){
        text_title.setText(R.string.manager_address);
        text_right.setVisibility(View.GONE);
        addressAdapter = new AddressAdapter(BaseApplication.getInstance());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        recycler_address.setLayoutManager(linearLayoutManager);
        recycler_address.addItemDecoration(new HotItemDecoration(2));
        recycler_address.setAdapter(addressAdapter);
        addressAdapter.setOnItemClickLitener(new AddressAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (fromOrder){
                    Intent intent = new Intent();
                    intent.putExtra("address",addressBeanList.get(position));
                    setResult(RESULT_OK,intent);
                    finish();
                }else {
                    Intent intent = new Intent(BaseApplication.getInstance(),EditAddressActivity.class);
                    intent.putExtra("state",3);
                    intent.putExtra("address",addressBeanList.get(position));
                    startActivity(intent,false);
                }


            }

            @Override
            public void onDelete(int position) {
                DialogUtil dialogUtil = new DialogUtil();
                dialogUtil.infoDialog(ManagerAddressActivity.this,"确定删除",true,true);
                dialogUtil.setOnClick(new DialogUtil.OnClick() {
                    @Override
                    public void leftClick() {
                        dialogUtil.dialog.dismiss();
                    }

                    @Override
                    public void rightClick() {
                        deletePosition = position;
                        AddressBean addressBean = addressBeanList.get(position);
                        deleteParam(addressBean);
                        dialogUtil.dialog.dismiss();
                    }
                });
            }

            @Override
            public void onEdit(int position) {
                startActivity(new Intent(BaseApplication.getInstance(),EditAddressActivity.class).putExtra("state",2).putExtra("address",addressBeanList.get(position)),false);

            }
        });
        fromOrder = getIntent().getBooleanExtra("fromOrder",false);
    }
    private void deleteAddress(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    ToastUtils.show(ManagerAddressActivity.this, "删除成功");
                    initData();
                } else {
                    ToastUtils.show(ManagerAddressActivity.this, baseRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(ManagerAddressActivity.this, "删除失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(ManagerAddressActivity.this, "删除失败，请稍后重试");

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData(){
            Map<String, Object> params = new HashMap<>();
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("cmd", "23");
                jsonObject.put("uid",BaseApplication.getInstance().getUser().getUserID());
                jsonObject.put("token",BaseApplication.getInstance().getToken());
                params.put("sendmsg", jsonObject.toString());
                searchInfo(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }


    private void searchInfo(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<AddressMsg>(this) {
            @Override
            public void onSuccess(Response response, AddressMsg addressMsg) {
                if (addressMsg.getResult() > 0) {
                    if (null != addressMsg.getList() && addressMsg.getList().size() > 0) {
                        addressAdapter.addressBean = addressMsg.getList();
                        addressAdapter.notifyDataSetChanged();
                        addressBeanList = addressMsg.getList();
                    } else {
                        addressAdapter.addressBean = addressMsg.getList();
                        addressAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.show(ManagerAddressActivity.this, addressMsg.getRetmsg());
                }
            }
            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(ManagerAddressActivity.this, "搜索失败，请稍后重试");
            }
            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(ManagerAddressActivity.this, "搜索失败，请稍后重试");

            }
        });
    }

    @OnClick(R.id.btn_addaddress)
    public void addNew(){

        startActivity(new Intent(BaseApplication.getInstance(),EditAddressActivity.class).putExtra("state",1),false);
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view){
        finish();
    }

    /**
     * 删除参数
     * @param addressBean
     */
    public void deleteParam(AddressBean addressBean){
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "18");
            jsonObject.put("typeid", "3");
            jsonObject.put("addressID", addressBean.getAddressID());
            jsonObject.put("shouHuoRen", addressBean.getShouHuoRen());
            jsonObject.put("mobile", addressBean.getMobile());
            jsonObject.put("provinceName", addressBean.getProvinceName());
            jsonObject.put("cityName",addressBean.getCityName());
            jsonObject.put("townName", addressBean.getTownName());
            jsonObject.put("address", addressBean.getAddress());
            jsonObject.put("uid",BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token",BaseApplication.getInstance().getToken());
            jsonObject.put("defaultFlag",addressBean.getDefaultFlag());
            params.put("sendmsg", jsonObject.toString());
            deleteAddress(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
