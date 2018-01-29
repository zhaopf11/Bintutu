package com.zhurui.bunnymall.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.home.adapter.NoticeMessageAdapter;
import com.zhurui.bunnymall.home.bean.LogisticsBean;
import com.zhurui.bunnymall.home.bean.LogisticsListBean;
import com.zhurui.bunnymall.home.bean.NoticeListMessageBean;
import com.zhurui.bunnymall.home.bean.NoticeMessageBean;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class NoticeListActivity extends BaseActivity {

    @Bind(R.id.imgbtn_back)
    ImageButton imgbtn_back;
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.recycler_notice)
    RecyclerView recycler_notice;
    private NoticeMessageAdapter noticeMessageAdapter;
    private boolean isLogistical = false;
    private String typeId = "";
    private List<NoticeMessageBean> mNoticeList;
    private List<LogisticsBean> mLogisticsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);
        ButterKnife.bind(this);
        initView();
        initData();
        getData();
    }

    private void initView(){
        isLogistical = getIntent().getBooleanExtra("isLogistical",false);
        typeId = getIntent().getStringExtra("typeId");
        if(isLogistical){
            text_title.setText(R.string.logistical_message);
        }else{
            text_title.setText(R.string.notice_message);
        }
        text_right.setVisibility(View.GONE);

    }

    private void initData(){
        noticeMessageAdapter = new NoticeMessageAdapter(BaseApplication.getInstance(),isLogistical);
        noticeMessageAdapter.setOnItemClickLitener(new NoticeMessageAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if(isLogistical){
                    LogisticsBean logisticsBean = mLogisticsList.get(position);
                    if(logisticsBean != null){
                        Intent intent = new Intent(BaseApplication.getInstance(),LogisticalDetailActivity.class);
                        intent.putExtra("shoporderid", logisticsBean.getShopOrderID());
                        intent.putExtra("isOrders", true);
                        intent.putExtra("image", logisticsBean.getProductImage());
                        intent.putExtra("size", logisticsBean.getProductSize());
                        startActivity(intent,false);
                    }
                }else {
                    NoticeMessageBean noticeMessageBean = mNoticeList.get(position);
                    if(noticeMessageBean != null){
                        if("3".equals(noticeMessageBean.getMessagetype())){
                            Intent intent = new Intent(BaseApplication.getInstance(),ProductNewDetailActivity.class);
                            intent.putExtra("productId", Long.parseLong(noticeMessageBean.getProductid()));
                            intent.putExtra("productType", noticeMessageBean.getUrl());
                            startActivity(intent,false);
                        }else if("2".equals(noticeMessageBean.getMessagetype())){
                            Intent intent = new Intent(BaseApplication.getInstance(),NoticeWebViewActivity.class);
                            intent.putExtra("url", noticeMessageBean.getUrl());
                            intent.putExtra("messagetype", noticeMessageBean.getMessagetype());
                            startActivity(intent,false);
                        }
                    }
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        recycler_notice.setLayoutManager(linearLayoutManager);
//        recycler_notice.addItemDecoration(new HotItemDecoration(2));
        recycler_notice.setAdapter(noticeMessageAdapter);
    }

    private void getData() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "65");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("typeid",typeId);
            jsonObject.put("pageno","1");
            jsonObject.put("pagesize","100");
            params.put("sendmsg", jsonObject.toString());
            getNoticeListMessage(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getNoticeListMessage(Map<String, Object> params) {
        if("1".equals(typeId)){
            mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<LogisticsListBean>(this) {
                @Override
                public void onSuccess(Response response, LogisticsListBean mLogisticsListBean) {
                    if(mLogisticsListBean != null){
                        if (mLogisticsListBean.getResult() > 0) {
                            mLogisticsList = mLogisticsListBean.getList();
                            noticeMessageAdapter.mLogisticsList = mLogisticsList;
                            noticeMessageAdapter.typeId = typeId;
                            noticeMessageAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.show(BaseApplication.getInstance(), mLogisticsListBean.getRetmsg());
                        }
                    }
                }

                @Override
                public void onError(Response response, int code, Exception e) {
                    ToastUtils.show(BaseApplication.getInstance(), "请求失败，请稍后重试");
                }

                @Override
                public void onServerError(Response response, int code, String errmsg) {
                    ToastUtils.show(BaseApplication.getInstance(), "请求失败，请稍后重试");

                }
            });
        }else if("0".equals(typeId)){
            mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<NoticeListMessageBean>(this) {
                @Override
                public void onSuccess(Response response, NoticeListMessageBean mNoticeListMessageBean) {
                    if(mNoticeListMessageBean != null){
                        if (mNoticeListMessageBean.getResult() > 0) {
                            mNoticeList = mNoticeListMessageBean.getList();
                            noticeMessageAdapter.mNoticeList = mNoticeList;
                            noticeMessageAdapter.typeId = typeId;
                            noticeMessageAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.show(BaseApplication.getInstance(), mNoticeListMessageBean.getRetmsg());
                        }
                    }
                }

                @Override
                public void onError(Response response, int code, Exception e) {
                    ToastUtils.show(BaseApplication.getInstance(), "请求失败，请稍后重试");
                }

                @Override
                public void onServerError(Response response, int code, String errmsg) {
                    ToastUtils.show(BaseApplication.getInstance(), "请求失败，请稍后重试");

                }
            });
        }

    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view){
        finish();
    }
}
