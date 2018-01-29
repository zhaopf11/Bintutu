package com.zhurui.bunnymall.home.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.imagepipeline.datasource.ListDataSource;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.adapter.StoreProductClassifyAdapter;
import com.zhurui.bunnymall.home.bean.BannerInfo;
import com.zhurui.bunnymall.home.bean.BigStoreClassifyBean;
import com.zhurui.bunnymall.home.bean.CarouselBean;
import com.zhurui.bunnymall.home.bean.SmallStoreClassifyBean;
import com.zhurui.bunnymall.home.msg.SearchResp;
import com.zhurui.bunnymall.home.msg.StoreClassifyRespMsg;
import com.zhurui.bunnymall.home.msg.StoreRespMsg;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.FullOffDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;

public class StoreProductClassifyActivity extends BaseActivity {

    @Bind(R.id.imgbtn_back)
    ImageButton imgbtn_back;
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.rel_allproduct)
    RelativeLayout rel_allproduct;
    @Bind(R.id.recycler_classify)
    RecyclerView recycler_classify;
    private StoreProductClassifyAdapter adapter;
    private String supplierid;

    private Map<String, List<SmallStoreClassifyBean>> listMap = null;

    private String primaryid="0";
    private String fashionid="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_product_classify);
        ButterKnife.bind(this);
        initView();
        initData();
    }


    private void initView() {
        text_title.setText(R.string.product_classify);
        text_right.setVisibility(View.GONE);
    }

    private void initData() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());

        recycler_classify.setLayoutManager(linearLayoutManager);
        recycler_classify.addItemDecoration(new FullOffDecoration(2));
        adapter = new StoreProductClassifyAdapter(BaseApplication.getInstance());
        adapter.setOnItemClickLitener(new StoreProductClassifyAdapter.OnItemClickLitener() {

            @Override
            public void onItemClick(View view, String smalltypeId) {
                SharedPreferences pref = BaseApplication.getInstance().getSharedPreferences("ISSELETCED", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("isSeletceNum", 0);
                editor.commit();
                primaryid = smalltypeId;
                initSearchInfo("0", smalltypeId);
            }

            @Override
            public void onGridItemClick(View view, String smalltypeId, String fashionId) {
                SharedPreferences pref = BaseApplication.getInstance().getSharedPreferences("ISSELETCED", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("isSeletceNum", 0);
                editor.commit();
                initSearchInfo(fashionId, smalltypeId);
                primaryid = smalltypeId;
                fashionid =fashionId;

            }

        });
        recycler_classify.setAdapter(adapter);
        supplierid = getIntent().getStringExtra("supplierid");
        initRequestInfo();

    }


    private void initRequestInfo() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "70");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("supplierid", supplierid);
            params.put("sendmsg", jsonObject.toString());
            getStoreInfo(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initSearchInfo(String fashionID, String primaryID) {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "69");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("smalltypeid", "0");
            jsonObject.put("fashionid", fashionID);
            jsonObject.put("supplierid", supplierid);
            jsonObject.put("primaryid", primaryID);
            jsonObject.put("category", "0");
            jsonObject.put("sortby", "0");
            jsonObject.put("sellnumber", "0");
            jsonObject.put("priceoder", "0");
            jsonObject.put("pagesize", "200");
            jsonObject.put("pageno", "1");
            jsonObject.put("keywords", "");
            jsonObject.put("msg", new JSONArray());
            params.put("sendmsg", jsonObject.toString());
            searchClassifyInfo(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void searchClassifyInfo(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<SearchResp>(this) {
            @Override
            public void onSuccess(Response response, SearchResp searchResp) {
                if (searchResp.getResult() > 0) {
                        Intent intent = new Intent(BaseApplication.getInstance(), ProductGridNormalActivity.class);
                        intent.putExtra("productlist", (Serializable) searchResp.getList());
                        intent.putExtra("supplierid", supplierid);
                        intent.putExtra("primaryid",primaryid);
                        intent.putExtra("fashionid",fashionid);
                        intent.putExtra("from",6);
                        startActivity(intent, false);
                } else {
                    ToastUtils.show(StoreProductClassifyActivity.this, searchResp.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(StoreProductClassifyActivity.this, "搜索失败，请稍后重试");
                dismissDialog();
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(StoreProductClassifyActivity.this, "搜索失败，请稍后重试");
                dismissDialog();

            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                dismissDialog();
            }
        });
    }


    private void getStoreInfo(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<StoreClassifyRespMsg>(this) {
            @Override
            public void onSuccess(Response response, StoreClassifyRespMsg storeClassifyRespMsg) {
                if (storeClassifyRespMsg.getResult() > 0) {

                    if (null != storeClassifyRespMsg.getList().get(0).getSupplierAreaList() && storeClassifyRespMsg.getList().get(0).getSupplierAreaList().size() > 0) {
                        listMap = new ArrayMap<String, List<SmallStoreClassifyBean>>();
                        for (BigStoreClassifyBean bigStoreClassifyBean : storeClassifyRespMsg.getList().get(0).getSupplierAreaList()) {
                            if (!listMap.containsKey(bigStoreClassifyBean.getSmallTypeID())) {
                                List<SmallStoreClassifyBean> list = new ArrayList<SmallStoreClassifyBean>();
                                listMap.put(bigStoreClassifyBean.getSmallTypeID(), list);
                            }
                        }

                        if (null != storeClassifyRespMsg.getList().get(0).getSupplierFashionList() && storeClassifyRespMsg.getList().get(0).getSupplierFashionList().size() > 0) {
                            for (SmallStoreClassifyBean smallStoreClassifyBean : storeClassifyRespMsg.getList().get(0).getSupplierFashionList()) {
                                listMap.get(smallStoreClassifyBean.getSmallTypeID()).add(smallStoreClassifyBean);
                            }

                        }

                    }


                    if (null != listMap && listMap.size() > 0) {
                        adapter.bigStoreClassifyBeen = storeClassifyRespMsg.getList().get(0).getSupplierAreaList();
                        adapter.listMap = listMap;
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    ToastUtils.show(StoreProductClassifyActivity.this, storeClassifyRespMsg.getRetmsg());

                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(StoreProductClassifyActivity.this, "请求失败，请稍后重试");
                dismissDialog();
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(StoreProductClassifyActivity.this, "请求失败，请稍后重试");
                dismissDialog();
            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                dismissDialog();
            }
        });
    }

    @OnClick(R.id.rel_allproduct)
    public void getAllData() {
        SharedPreferences pref = BaseApplication.getInstance().getSharedPreferences("ISSELETCED", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("isSeletceNum", 0);
        editor.commit();
        initSearchInfo("0", "0");

    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
