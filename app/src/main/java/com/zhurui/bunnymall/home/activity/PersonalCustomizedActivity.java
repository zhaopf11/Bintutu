package com.zhurui.bunnymall.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.zhurui.bunnymall.MainActivity;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.home.adapter.PersonaCustomizedAdapter;
import com.zhurui.bunnymall.home.msg.CustomizedDeatilResp;
import com.zhurui.bunnymall.home.msg.PersonalCustomizedRsp;
import com.zhurui.bunnymall.home.bean.PersonalCustomizedbean;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.MyDropDownMenu;
import com.zhurui.bunnymall.viewutils.ScreenDialog;
import com.zhurui.bunnymall.viewutils.pulltorefresh.PullToRefreshLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 定制商品列表
 */

public class PersonalCustomizedActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener, PersonaCustomizedAdapter.OnItemClickLitener {

    @Bind(R.id.grid_customized)
    GridView grid_customized;
    @Bind(R.id.imgbtn_right)
    TextView imgbtn_right;
    @Bind(R.id.edit_search)
    TextView edit_search;
    private PersonaCustomizedAdapter adapter;
    @Bind(R.id.myDropDownMenu)
    MyDropDownMenu myDropDownMenu;
    private static final int REQUEST_CODE = 1;
    private boolean isClassify = false;
    private int pageNo = 1;
    private boolean refresh = false;
    @Bind(R.id.refresh_view)
    PullToRefreshLayout mPullToRefreshLayout;
    private String keyWord = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_customized);
        ButterKnife.bind(this);
        initView();
        initData();

    }

    private void initView() {
        imgbtn_right.setText(R.string.screen);
        isClassify = getIntent().getBooleanExtra("isClassify",false);
        adapter = new PersonaCustomizedAdapter(BaseApplication.getInstance());
        grid_customized.setAdapter(adapter);
        mPullToRefreshLayout.setOnRefreshListener(this);
        mPullToRefreshLayout.autoRefresh();
        adapter.setOnItemClickLitener(this);
    }


    private void initData() {
        String keyWord = edit_search.getText().toString();
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "16");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("pagesize", "20");
            jsonObject.put("pageno", pageNo);
            jsonObject.put("keyword", keyWord);
            params.put("sendmsg", jsonObject.toString());
            searchInfo(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void searchInfo(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<PersonalCustomizedRsp>(this,"custom") {
            @Override
            public void onSuccess(Response response, PersonalCustomizedRsp personalCustomizedRsp) {
                if (personalCustomizedRsp.getResult() > 0) {
                    if (null != personalCustomizedRsp.getList() && personalCustomizedRsp.getList().size() > 0) {
                        if(refresh){
                            pageNo = 2;
                            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                            if(adapter.list != null){
                                adapter.list.clear();
                            }
                            adapter.list = personalCustomizedRsp.getList();
                        }else{
                            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            pageNo ++ ;
                            adapter.list.addAll(personalCustomizedRsp.getList());
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        if(refresh) {
                            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        }else{
                            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        }
                        if(!TextUtils.isEmpty(keyWord)){
                            ToastUtils.show(BaseApplication.getInstance(),"没有搜索到相关商品");
                        }
                        ToastUtils.show(PersonalCustomizedActivity.this, "暂无私人定制商品");
                    }
                } else {
                    if(refresh) {
                        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    }else{
                        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                    }
                    ToastUtils.show(PersonalCustomizedActivity.this, personalCustomizedRsp.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(PersonalCustomizedActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(PersonalCustomizedActivity.this, "请求失败，请稍后重试");

            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                if(refresh) {
                    mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }else{
                    mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
            }
        });
    }

    private void searchDetail(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<CustomizedDeatilResp>(this) {
            @Override
            public void onSuccess(Response response, CustomizedDeatilResp customizedDeatilResp) {
                if (customizedDeatilResp.getResult() > 0) {
                    if (null != customizedDeatilResp.getList() && customizedDeatilResp.getList().size() > 0) {
                        Intent intent = new Intent(BaseApplication.getInstance(), ProductNewDetailActivity.class);
                        intent.putExtra("productType", 1);
                        intent.putExtra("customized", customizedDeatilResp);
                        startActivity(intent, false);

                    } else {
                        ToastUtils.show(PersonalCustomizedActivity.this, "暂无商品详情");
                    }


                } else {
                    ToastUtils.show(PersonalCustomizedActivity.this, customizedDeatilResp.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(PersonalCustomizedActivity.this, "查询失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(PersonalCustomizedActivity.this, "查询失败，请稍后重试");

            }
        });
    }


    @OnClick(R.id.edit_search)
    public void toSearch() {
        startActivityForResult(new Intent(BaseApplication.getInstance(), SearchActivity.class).putExtra("state", 1), false, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (null != data) {
                    List<PersonalCustomizedbean> personalCustomizedbeen = (List<PersonalCustomizedbean>) data.getSerializableExtra("list");
                    keyWord = data.getStringExtra("keyWord");
                    edit_search.setText(keyWord);
                    adapter.list = personalCustomizedbeen;
                    adapter.notifyDataSetChanged();
                    if(personalCustomizedbeen != null && personalCustomizedbeen.size() > 0){
//                        adapter.list = personalCustomizedbeen;
//                        adapter.notifyDataSetChanged();
                    }else{
                        ToastUtils.show(PersonalCustomizedActivity.this, "没有搜索到相关商品");
                    }
                }
                break;
        }
    }

    @OnClick(R.id.imgbtn_right)
    public void toScreen() {
        ScreenDialog screenDialog = new ScreenDialog(PersonalCustomizedActivity.this, R.style.CustomDialog, true);
        screenDialog.setOnSubMit(new ScreenDialog.OnSubMit() {
            @Override
            public void subMit(JSONArray jsonArray) {

                Map<String, Object> params = new HashMap<>();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("cmd", "40");
                    jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                    jsonObject.put("token", BaseApplication.getInstance().getToken());
                    jsonObject.put("smalltypeid", "4");
                    jsonObject.put("fashionid", "0");
                    jsonObject.put("category", "1");
                    jsonObject.put("sortby", "0");
                    jsonObject.put("sellNumber", "0");
                    jsonObject.put("priceoder", "0");
                    jsonObject.put("pagesize", "200");
                    jsonObject.put("pageno", "1");
                    jsonObject.put("keywords", "");
                    jsonObject.put("msg", jsonArray);
                    params.put("sendmsg", jsonObject.toString());
                    getListData(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        screenDialog.show();
    }


    private void getListData(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<PersonalCustomizedRsp>(this) {
            @Override
            public void onSuccess(Response response, PersonalCustomizedRsp productListRespMsg) {
                if (productListRespMsg.getResult() > 0) {
                    adapter.list = productListRespMsg.getList();
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.show(PersonalCustomizedActivity.this, productListRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(PersonalCustomizedActivity.this, "请求失败，请稍后重试");
                dismissDialog();
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(PersonalCustomizedActivity.this, "请求失败，请稍后重试");
                dismissDialog();

            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                dismissDialog();
            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        refresh = true;
        pageNo = 1;
        initData();
        mPullToRefreshLayout = pullToRefreshLayout;
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        refresh = false;
        initData();
        mPullToRefreshLayout = pullToRefreshLayout;
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }

    /**
     * 跳转详情
     * @param view
     * @param productId
     */
    @Override
    public void onItemClick(View view, long productId) {
        Intent intent = new Intent(BaseApplication.getInstance(), ProductNewDetailActivity.class);
        intent.putExtra("productType", 2);
        intent.putExtra("productId", productId);
        startActivity(intent, false);
    }
}
