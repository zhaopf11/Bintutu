package com.zhurui.bunnymall.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.home.activity.BuyTeamActivity;
import com.zhurui.bunnymall.home.activity.ProductGridNormalActivity;
import com.zhurui.bunnymall.home.activity.ProductNewDetailActivity;
import com.zhurui.bunnymall.home.msg.TeamBuyResp;
import com.zhurui.bunnymall.mine.adapter.FootPrintAdapter;
import com.zhurui.bunnymall.mine.bean.FootPrintBean;
import com.zhurui.bunnymall.mine.msg.FootPrintRespMsg;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.TimeUtils;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.pulltorefresh.PullToRefreshLayout;

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
import okhttp3.Response;

public class MyFootPrintActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {

    @Bind(R.id.imgbtn_back)
    ImageButton imgbtn_back;
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.expandable)
    ExpandableListView expandable;
    private FootPrintAdapter footPrintAdapter;

    private Map<String, List<FootPrintBean>> footPrintMap = null;
    private List<String> keylist = null;
    private int pageNo = 1;
    private boolean refresh = false;
    @Bind(R.id.refresh_view)
    PullToRefreshLayout mPullToRefreshLayout;
    private  List<FootPrintBean> mlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_foot_print);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        text_title.setText(R.string.my_footprint);
        text_right.setVisibility(View.GONE);
        mPullToRefreshLayout.setOnRefreshListener(this);
        footPrintAdapter = new FootPrintAdapter(BaseApplication.getInstance());
        footPrintAdapter.setOnItem(new FootPrintAdapter.OnItem() {
            @Override
            public void similarityClick(int groupPosition, int position) {
                Intent intent = new Intent(BaseApplication.getInstance(), FindSimilarityActivity.class);
                intent.putExtra("isFootPrint", true);
                intent.putExtra("footprintbean", footPrintAdapter.footPrintMap.get(footPrintAdapter.keylist.get(groupPosition)).get(position));
                startActivity(intent, false);

            }
        });
        expandable.setAdapter(footPrintAdapter);

        expandable.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        expandable.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(BaseApplication.getInstance(), ProductNewDetailActivity.class);
                FootPrintBean footPrintBean = footPrintAdapter.footPrintMap.get(footPrintAdapter.keylist.get(groupPosition)).get(childPosition);
                intent.putExtra("productType", footPrintBean.getFlag());
                intent.putExtra("productId", footPrintBean.getProductID());
                startActivity(intent);
                return false;
            }
        });
    }

    private void initData() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "26");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("pageno", pageNo);
            jsonObject.put("pagesize", "20");
            jsonObject.put("keyword", "");
            jsonObject.put("browsingHistoryID", "1");
            params.put("sendmsg", jsonObject.toString());
            getTeamData(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getTeamData(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<FootPrintRespMsg>(this) {
            @Override
            public void onSuccess(Response response, FootPrintRespMsg footPrintRespMsg) {
                if (footPrintRespMsg.getResult() > 0) {
                    if(footPrintRespMsg != null){
                        if (null != footPrintRespMsg.getList() && footPrintRespMsg.getList().size() > 0) {
                            if(refresh){
                                mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                footPrintAdapter.footPrintMap.clear();
                                footPrintAdapter.keylist.clear();
                                mlist.clear();
                                dealWithData(footPrintRespMsg.getList());
                                footPrintAdapter.footPrintMap = footPrintMap;
                                footPrintAdapter.keylist = keylist;
                            }else{
                                mlist.addAll(footPrintRespMsg.getList());
                                mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                pageNo ++ ;
                                dealWithData(mlist);
                                footPrintAdapter.footPrintMap = footPrintMap;
                                footPrintAdapter.keylist = keylist;
                            }
                            footPrintAdapter.notifyDataSetChanged();
                            int groupCount = footPrintAdapter.getGroupCount();
                            for (int i = 0; i < groupCount; i++) {
                                expandable.expandGroup(i);
                            };
                        }else{
                            if(refresh) {
                                mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                            }else{
                                mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                            }
                        }
                    }
                } else {
                    if(refresh) {
                        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    }else{
                        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                    }
                    ToastUtils.show(MyFootPrintActivity.this, footPrintRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(MyFootPrintActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(MyFootPrintActivity.this, "请求失败，请稍后重试");

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

    public void dealWithData(List<FootPrintBean> footPrintList){
        footPrintMap = new HashMap<String, List<FootPrintBean>>();
        keylist = new ArrayList<String>();
        for (FootPrintBean footPrintBean : footPrintList) {
            String date = TimeUtils.dateToString(TimeUtils.stringToDate(footPrintBean.getTime()));
            List<FootPrintBean> footPrintBeen = new ArrayList<FootPrintBean>();
            if (!footPrintMap.containsKey(date)) {
                footPrintMap.put(date, footPrintBeen);
                keylist.add(date);
            }
        }
        for (FootPrintBean footPrint : footPrintList) {
            String datestr = TimeUtils.dateToString(TimeUtils.stringToDate(footPrint.getTime()));
            footPrintMap.get(datestr).add(footPrint);
        }
    }
}
