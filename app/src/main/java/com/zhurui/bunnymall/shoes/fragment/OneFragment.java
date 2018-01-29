package com.zhurui.bunnymall.shoes.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.BaseFragment;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.shoes.activity.ShoesDetailActivity;
import com.zhurui.bunnymall.shoes.adapter.ShoesAdapter;
import com.zhurui.bunnymall.shoes.bean.ShoesBean;
import com.zhurui.bunnymall.shoes.bean.ShoesListBean;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.pulltorefresh.PullToRefreshLayout;
import com.zhurui.bunnymall.viewutils.pulltorefresh.pullableview.PullableScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 鞋闻趣事
 */
public class OneFragment extends BaseFragment implements ShoesAdapter.OnItemClickLitener, PullToRefreshLayout.OnRefreshListener {

    @Bind(R.id.my_listview)
    RecyclerView my_listview;
    @Bind(R.id.shoes_scrollview)
    PullableScrollView scrollview;
    @Bind(R.id.refresh_view)
    PullToRefreshLayout mPullToRefreshLayout;

    private ShoesAdapter shoesAdapter;
    private int pageNo = 1;
    private boolean refresh = false;
    private List<ShoesListBean> shoesListBean ;
    private String infoTypeID = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {
        infoTypeID = "1014";
        initView();
        initData();
    }

    private void initView() {
        mPullToRefreshLayout.setOnRefreshListener(this);
        mPullToRefreshLayout.autoRefresh();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        my_listview.setNestedScrollingEnabled(false);
        my_listview.setLayoutManager(linearLayoutManager);
        shoesAdapter = new ShoesAdapter(getActivity());
        my_listview.setAdapter(shoesAdapter);
        shoesAdapter.setOnItemClickLitener(this);
    }

    /**
     * 初始化数据
     * infoTypeID 1013是鞋匠访 infoTypeID 1014是鞋闻趣事
     */
    private void initData() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "77");
            jsonObject.put("infoTypeID", infoTypeID);
            jsonObject.put("pagesize", "20");
            jsonObject.put("pageno", pageNo);
            params.put("sendmsg", jsonObject.toString());
            getDataList(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接口
     * @param params
     */
    private void getDataList(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<ShoesBean>(getActivity(),"list") {
            @Override
            public void onSuccess(Response response, ShoesBean shoesBean) {
                if (shoesBean.getResult() > 0) {
                    shoesListBean = shoesBean.getList();
                    if(shoesListBean != null && shoesListBean.size() > 0){
                        if(refresh) {
                            pageNo = 2;
                            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                            if(shoesAdapter.shoesList != null){
                                shoesAdapter.shoesList.clear();
                            }
                            shoesAdapter.shoesList = shoesListBean;
                        }else {
                            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            pageNo ++ ;
                            if(shoesAdapter.shoesList == null ){
                                shoesAdapter.shoesList = new ArrayList<ShoesListBean>();
                            }
                            shoesAdapter.shoesList.addAll(shoesBean.getList());
                        }
                        shoesAdapter.notifyDataSetChanged();
                    }else{
                        if(refresh) {
                            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                            shoesAdapter.shoesList = shoesBean.getList();
                            shoesAdapter.notifyDataSetChanged();
                        }else{
                            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        }
                    }
                } else {
                    if(refresh) {
                        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    }else{
                        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                    }
                    ToastUtils.show(BaseApplication.getInstance(), shoesBean.getRetmsg());
                }
            }
            @Override
            public void onError(Response response, int code, Exception e) {
                if(refresh) {
                    mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }else{
                    mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
                ToastUtils.show(BaseApplication.getInstance(), "请求失败，请稍后重试");
            }
            @Override
            public void onServerError(Response response, int code, String errmsg) {
                if(refresh) {
                    mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }else{
                    mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
                ToastUtils.show(BaseApplication.getInstance(), "请求失败，请稍后重试");
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

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        refresh = true;
        pageNo = 1;
        toRefresh();
        mPullToRefreshLayout = pullToRefreshLayout;
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        refresh = false;
        toRefresh();
        mPullToRefreshLayout = pullToRefreshLayout;
    }

    private void toRefresh(){
        initData();
    }

    /**
     * 点击事件
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(BaseApplication.getInstance(), ShoesDetailActivity.class);
        intent.putExtra("infoTypeID",infoTypeID);
        intent.putExtra("shoesBean",shoesAdapter.shoesList.get(position));
        startActivity(intent);
    }

    /**
     * item 点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(BaseApplication.getInstance(), ShoesDetailActivity.class);
////        intent.putExtra("infoTypeID",infoTypeID);
//        intent.putExtra("shoesBean",shoesAdapter.shoesList.get(position));
//        startActivity(intent);
//    }
}
