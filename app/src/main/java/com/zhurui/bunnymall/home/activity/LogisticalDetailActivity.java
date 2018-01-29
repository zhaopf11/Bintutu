package com.zhurui.bunnymall.home.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.home.adapter.LogisticalDetailAdapter;
import com.zhurui.bunnymall.home.bean.LogisticsDetailMessageBean;
import com.zhurui.bunnymall.home.bean.LogisticsListMessageBean;
import com.zhurui.bunnymall.home.bean.LogisticsMessageBean;
import com.zhurui.bunnymall.home.bean.NoticeMessageBean;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class LogisticalDetailActivity extends BaseActivity {

    @Bind(R.id.imgbtn_back)
    ImageButton imgbtn_back;
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.img_product)
    NetworkImageView img_product;
    @Bind(R.id.text_count)
    TextView text_count;
    @Bind(R.id.text_logisticalstate)
    TextView text_logisticalstate;
    @Bind(R.id.text_logisticalcompany)
    TextView text_logisticalcompany;
    @Bind(R.id.text_logisticalnum)
    TextView text_logisticalnum;
    @Bind(R.id.recycler_logistical)
    RecyclerView recycler_logistical;
    private LogisticalDetailAdapter adapter;
    private List<LogisticsDetailMessageBean> mList;
    private NoticeMessageBean noticeMessageBean;
    private ImageLoader imageLoader;
    private String shoporderid;
    private Boolean isOrders;
    private String size;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistical_detail);
        ButterKnife.bind(this);
        imageLoader = BaseApplication.getInstance().getImageLoader();
        initView();
        initData();
        getData();
    }


    private void initView(){
        text_title.setText(R.string.logistical_detail);
        text_right.setVisibility(View.GONE);
    }

    private void initData(){
        shoporderid = getIntent().getStringExtra("shoporderid");
        image = getIntent().getStringExtra("image");
        size = getIntent().getStringExtra("size");
        isOrders = getIntent().getBooleanExtra("isOrders",false);
        noticeMessageBean = (NoticeMessageBean)getIntent().getSerializableExtra("noticeMessageBean");
        if(isOrders){
            img_product.setImageUrl(Contants.BASE_IMGURL + image,imageLoader);
            text_count.setText(size + "件商品");
        }
        adapter = new LogisticalDetailAdapter(LogisticalDetailActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance(), LinearLayoutManager.VERTICAL, true);
        recycler_logistical.setLayoutManager(linearLayoutManager);
//        recycler_notice.addItemDecoration(new HotItemDecoration(2));
        recycler_logistical.setHasFixedSize(true);
        recycler_logistical.setFocusable(false);
        recycler_logistical.setAdapter(adapter);
    }
    private void getData() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            if(isOrders){
                //订单界面查看物流信息
                jsonObject.put("cmd", "64");
                jsonObject.put("shoporderid", shoporderid);
            }
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            params.put("sendmsg", jsonObject.toString());
            getListMessage(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getListMessage(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<LogisticsListMessageBean>(this) {
            @Override
            public void onSuccess(Response response, LogisticsListMessageBean mLogisticsListMessageBean) {
                if(mLogisticsListMessageBean != null){
                    if (mLogisticsListMessageBean.getResult() > 0) {

                        if(mLogisticsListMessageBean.getList() !=null && mLogisticsListMessageBean.getList().size() >0){
                            mList = mLogisticsListMessageBean.getList().get(0).getData();
                            Collections.reverse(mList);
                            adapter.mList = mList;
                            adapter.notifyDataSetChanged();
                            updataToUi(mLogisticsListMessageBean.getList().get(0));
                        }else{
                            ToastUtils.show(BaseApplication.getInstance(), "暂无物流信息");
                        }
                    } else {
                        ToastUtils.show(BaseApplication.getInstance(), mLogisticsListMessageBean.getRetmsg());
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
//    state 快递单当前的状态 ：　
//    0：在途，即货物处于运输过程中；
//    1：揽件，货物已由快递公司揽收并且产生了第一条跟踪信息；
//    2：疑难，货物寄送过程出了问题；
//    3：签收，收件人已签收；
//    4：退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收；
//    5：派件，即快递正在进行同城派件；
//    6：退回，货物正处于退回发件人的途中；
    private void updataToUi(LogisticsMessageBean logisticsMessageBean){
        if(logisticsMessageBean != null){
            String state = logisticsMessageBean.getState();
            if("0".equals(state)){
                text_logisticalstate.setText("在途中");
            }else if("1".equals(state)){
                text_logisticalstate.setText("已揽件");
            }else if("2".equals(state)){
                text_logisticalstate.setText("疑难");
            }else if("3".equals(state)){
                text_logisticalstate.setText("已签收");
            }else if("4".equals(state)){
                text_logisticalstate.setText("已退签");
            }else if("5".equals(state)){
                text_logisticalstate.setText("已派件");
            }else if("6".equals(state)){
                text_logisticalstate.setText("已退回");
            }
            text_logisticalcompany.setText("承运公司："+ logisticsMessageBean.getCompanyname());
            text_logisticalnum.setText("运单编号："+ logisticsMessageBean.getNu());
        }
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view){
        finish();
    }
}
