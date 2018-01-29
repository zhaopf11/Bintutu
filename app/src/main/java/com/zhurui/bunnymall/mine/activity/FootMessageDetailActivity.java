package com.zhurui.bunnymall.mine.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.adapter.CardAdapter;
import com.zhurui.bunnymall.mine.adapter.FootMessageDetailAdapter;
import com.zhurui.bunnymall.mine.bean.MyFootDetailListMessageBean;
import com.zhurui.bunnymall.mine.bean.UserFootDataBean;
import com.zhurui.bunnymall.mine.bean.UserFootDataDetailBean;
import com.zhurui.bunnymall.mine.bean.UserFootDataListBean;
import com.zhurui.bunnymall.utils.BlurBitmapUtils;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.utils.ViewSwitchUtils;
import com.zhurui.bunnymall.viewutils.CardScaleHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class FootMessageDetailActivity extends BaseActivity {
    @Bind(R.id.text_sex)
    TextView text_sex;
    @Bind(R.id.text_height)
    TextView text_height;
    @Bind(R.id.text_birthday)
    TextView text_birthday;
    @Bind(R.id.text_shoesize)
    TextView text_shoesize;
    @Bind(R.id.text_weight)
    TextView text_weight;
    @Bind(R.id.text_desc)
    TextView text_desc;

    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.blurView)
    ImageView blurView;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<Integer> mList = new ArrayList<>();
    private CardScaleHelper mCardScaleHelper = null;
    private Runnable mBlurRunnable;
    private int mLastPos = -1;
    @Bind(R.id.recycler_footdetail)
    RecyclerView recycler_footdetail;
    private FootMessageDetailAdapter footMessageDetailAdapter;
    @Bind(R.id.lin_edit)
    LinearLayout lin_edit;
    private boolean isEdit = false;
    private String userFootTypeDataID;
    UserFootDataDetailBean userFootDataDetailBean;
    @Bind(R.id.lin_footmessage)
    LinearLayout lin_footmessage;

    @Bind(R.id.edit_name)
    EditText edit_name;

    @Bind(R.id.edit_height)
    EditText edit_height;
    @Bind(R.id.edit_weight)
    EditText edit_weight;
    @Bind(R.id.edit_birthday)
    EditText edit_birthday;
    @Bind(R.id.edit_shoesize)
    EditText edit_shoesize;
    @Bind(R.id.edit_footdesc)
    EditText edit_footdesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_message_detail);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        String name = getIntent().getStringExtra("name");
        String userid = getIntent().getStringExtra("userid");
        userFootTypeDataID = getIntent().getStringExtra("userFootTypeDataID");
        if(userid.equals(BaseApplication.getInstance().getUser().getUserID() + "")){
            text_right.setVisibility(View.VISIBLE);
            text_right.setText(R.string.edit);
        }else{
            text_right.setVisibility(View.GONE);
       }
    }

    private void initData() {

        for (int i = 0; i < 10; i++) {
            mList.add(R.drawable.foot1);
            mList.add(R.drawable.foot1);
            mList.add(R.drawable.foot1);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new CardAdapter(mList));
        // mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(1);
        mCardScaleHelper.attachToRecyclerView(recyclerView);
        initBlurBackground();
        footMessageDetailAdapter = new FootMessageDetailAdapter(BaseApplication.getInstance());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_footdetail.setLayoutManager(linearLayoutManager1);
        recycler_footdetail.setAdapter(footMessageDetailAdapter);
    }

    private void initBlurBackground() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    notifyBackgroundChange();
                }
            }
        });

        notifyBackgroundChange();
    }

    private void notifyBackgroundChange() {
        if (mLastPos == mCardScaleHelper.getCurrentItemPos())
            return;
        mLastPos = mCardScaleHelper.getCurrentItemPos();
        final int resId = mList.get(mCardScaleHelper.getCurrentItemPos());
        blurView.removeCallbacks(mBlurRunnable);
        mBlurRunnable = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
                ViewSwitchUtils.startSwitchBackgroundAnim(blurView, BlurBitmapUtils.getBlurBitmap(blurView.getContext(), bitmap, 15));
            }
        };
        blurView.postDelayed(mBlurRunnable, 500);
    }

    @OnClick(R.id.text_right)
    public void editDetail() {
        Intent intent = new Intent(FootMessageDetailActivity.this,AddFootMessageActivity.class);
        intent.putExtra("editType","1");
        intent.putExtra("userFootDataDetailBean",userFootDataDetailBean);
        startActivity(intent);
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }


    private void getData() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "42");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("userfoottypedataid", userFootTypeDataID);
            params.put("sendmsg", jsonObject.toString());
            getFootTypeDetailMessage(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getFootTypeDetailMessage(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<MyFootDetailListMessageBean>(this) {
            @Override
            public void onSuccess(Response response, MyFootDetailListMessageBean myFootDetailListMessageBean) {
                if (myFootDetailListMessageBean != null) {
                    if (myFootDetailListMessageBean.getResult() > 0) {
                        List<UserFootDataBean> mList = myFootDetailListMessageBean.getList();
                        if (mList != null && mList.size() > 0) {
                            List<UserFootDataDetailBean> useInfoList = mList.get(0).getUserFootDataDetail();
                            List<UserFootDataListBean> useFootDataList = mList.get(0).getUserFootDataList();
                            //更新足型详情界面
                            if(useFootDataList != null && useFootDataList.size() >0){
                                lin_footmessage.setVisibility(View.VISIBLE);
                            }else{
                                lin_footmessage.setVisibility(View.GONE);
                            }
                            updateToUi(useInfoList);
                            footMessageDetailAdapter.useFootDataList = useFootDataList;
                            footMessageDetailAdapter.notifyDataSetChanged();
                        }
                    } else {
                        ToastUtils.show(BaseApplication.getInstance(), myFootDetailListMessageBean.getRetmsg());
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

    private void updateToUi(List<UserFootDataDetailBean> useInfoList) {
        if (useInfoList != null && useInfoList.size() > 0) {
            userFootDataDetailBean = useInfoList.get(0);
            String sexStr = "";
            if (!TextUtils.isEmpty(userFootDataDetailBean.getSex())) {
                if ("1".equals(userFootDataDetailBean.getSex())) {
                    sexStr = "男";
                } else if ("2".equals(userFootDataDetailBean.getSex())) {
                    sexStr = "女";
                }
            }
            text_sex.setText(sexStr);
            text_height.setText(userFootDataDetailBean == null ? "" : userFootDataDetailBean.getStature());
            text_birthday.setText(userFootDataDetailBean == null ? "" : userFootDataDetailBean.getBirthday());
            text_shoesize.setText(userFootDataDetailBean == null ? "" : userFootDataDetailBean.getShoeSize());
            text_weight.setText(userFootDataDetailBean == null ? "" : userFootDataDetailBean.getWeight());
            text_desc.setText(userFootDataDetailBean == null ? "" : userFootDataDetailBean.getFootDesc());
            text_title.setText(userFootDataDetailBean == null ? "" :userFootDataDetailBean.getName() + "的足型数据");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
