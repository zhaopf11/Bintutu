package com.zhurui.bunnymall.home.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.adapter.JudegmentAdapter;
import com.zhurui.bunnymall.home.bean.MessageListBean;
import com.zhurui.bunnymall.home.bean.ProductDetailBean;
import com.zhurui.bunnymall.home.msg.JudgeMentRespMsg;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.HotItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class JudgementsActivity extends BaseActivity {
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;

    @Bind(R.id.radiogroup_judgements)
    RadioGroup radiogroup_judgements;
    @Bind(R.id.recycler_judgement)
    RecyclerView recycler_judgement;
    @Bind(R.id.radio_alljudgement)
    RadioButton radio_alljudgement;
    @Bind(R.id.radio_judgementimg)
    RadioButton radio_judgementimg;
    private JudegmentAdapter judegmentAdapter;
    private int isCustom =0;
    private String productId;
    private ProductDetailBean productDetailBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judgements);
        ButterKnife.bind(this);
        initView();
        initData("1","");
    }

    private void initView(){
        productDetailBean = (ProductDetailBean) getIntent().getSerializableExtra("productBean");
        text_title.setText(R.string.judgement);
        text_right.setVisibility(View.GONE);
        if(productDetailBean != null){
            productId = productDetailBean.getProductID();
            radio_alljudgement.setText("全部("+productDetailBean.getCommentTimes()+")");
            radio_judgementimg.setText("有图("+productDetailBean.getCommentPicTimes()+")");
        }
        judegmentAdapter = new JudegmentAdapter(BaseApplication.getInstance());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        recycler_judgement.setLayoutManager(linearLayoutManager);
        recycler_judgement.addItemDecoration(new HotItemDecoration(2));
        recycler_judgement.setAdapter(judegmentAdapter);
        radiogroup_judgements.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId){
                case R.id.radio_alljudgement:
                    initData("1","");
                    break;
                case R.id.radio_judgementimg:
                    initData("2","");
                    break;
            }
        }
    };

    private void initData(String typeid,String dialog){
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "31");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("pagesize","200");
            jsonObject.put("pageno","1");
            jsonObject.put("productid",productId);
            jsonObject.put("typeid",typeid);
            params.put("sendmsg", jsonObject.toString());
            getList(params,typeid,dialog);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void getList(Map<String, Object> params,String typeid,String dialog) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<JudgeMentRespMsg>(this,dialog) {
            @Override
            public void onSuccess(Response response, JudgeMentRespMsg judgeMentRespMsg) {
                if(judgeMentRespMsg != null){
                    if (judgeMentRespMsg.getResult() > 0) {
                        judegmentAdapter.commentBeanList = judgeMentRespMsg.getList();
                        judegmentAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.show(BaseApplication.getInstance(), judgeMentRespMsg.getRetmsg());
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

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view){
        finish();
    }
}
