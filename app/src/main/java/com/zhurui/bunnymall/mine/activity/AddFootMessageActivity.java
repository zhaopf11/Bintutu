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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.adapter.CardAdapter;
import com.zhurui.bunnymall.mine.bean.UserFootDataDetailBean;
import com.zhurui.bunnymall.utils.BlurBitmapUtils;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.utils.Utils;
import com.zhurui.bunnymall.utils.ViewSwitchUtils;
import com.zhurui.bunnymall.viewutils.CardScaleHelper;
import com.zhurui.bunnymall.viewutils.DateTimePickDialog;
import com.zhurui.bunnymall.viewutils.timepickerview.TimePickerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class AddFootMessageActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
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
    private String sex = "1";
    private UserFootDataDetailBean userFootDataDetailBean;
    private String editType;
    private TimePickerView pvTime;

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
    @Bind(R.id.radiobtn_man)
    RadioButton radiobtn_man;
    @Bind(R.id.radiobtn_woman)
    RadioButton radiobtn_woman;
    @Bind(R.id.radiogroup_sex)
    RadioGroup radiogroup_sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_foot_message);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView(){
        text_right.setText("保存");
        editType = getIntent().getStringExtra("editType");
        userFootDataDetailBean = (UserFootDataDetailBean) getIntent().getSerializableExtra("userFootDataDetailBean");
        if("1".equals(editType)){
            text_title.setText(userFootDataDetailBean.getName() +"的足型数据");
            refreshUi(userFootDataDetailBean);
        }else{
            text_title.setText(R.string.add_footmessage);
        }
    }

    /**
     * 足型数据编辑状态下更新界面
     * @param userFootDataDetailBean
     */
    private void refreshUi(UserFootDataDetailBean userFootDataDetailBean) {
        if(userFootDataDetailBean != null){
            edit_name.setText(userFootDataDetailBean.getName());
            Utils.cursorToEnd(edit_name, edit_name.getText().toString());
            if("1".equals(userFootDataDetailBean.getSex())){
                radiobtn_man.setChecked(true);
                radiobtn_woman.setChecked(false);
            }else{
                radiobtn_man.setChecked(false);
                radiobtn_woman.setChecked(true);
            }
            edit_height.setText(userFootDataDetailBean.getStature());
            edit_weight.setText(userFootDataDetailBean.getWeight());
            edit_birthday.setText(userFootDataDetailBean.getBirthday());
            edit_shoesize.setText(userFootDataDetailBean.getShoeSize());
            edit_footdesc.setText(userFootDataDetailBean.getFootDesc());
        }
    }

    private void initData(){
        radiogroup_sex.setOnCheckedChangeListener(this);
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
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
        edit_footdesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                String desc = edit_footdesc.getText().toString();
                if(desc.length() >= 20){
                    ToastUtils.show(mContext, "特殊脚型描述不能超过20个字");
                }
            }
        });
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
        if (mLastPos == mCardScaleHelper.getCurrentItemPos()) return;
        mLastPos = mCardScaleHelper.getCurrentItemPos();
        if(mCardScaleHelper.getCurrentItemPos() >=0 && mCardScaleHelper.getCurrentItemPos()<mList.size()){
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
    }
    @OnClick(R.id.text_right)
    public void saveInfo(){
        String heightstr =edit_height.getText().toString().trim();
        String namestr = edit_name.getText().toString().trim();
        String weightstr = edit_weight.getText().toString().trim();
        String birthday = edit_birthday.getText().toString().trim();
        String shoeSize = edit_shoesize.getText().toString().trim();
        String editfootDesc = edit_footdesc.getText().toString().trim();

        if(TextUtils.isEmpty(namestr)){
            ToastUtils.show(AddFootMessageActivity.this, "请输入姓名");
        }else if(TextUtils.isEmpty(sex)){
            ToastUtils.show(AddFootMessageActivity.this, "请输入性别");
        }else if(TextUtils.isEmpty(heightstr)){
            ToastUtils.show(AddFootMessageActivity.this, "请输入身高");

        }else if(TextUtils.isEmpty(weightstr)){
            ToastUtils.show(AddFootMessageActivity.this, "请输入体重");

        }else if(TextUtils.isEmpty(birthday)){
            ToastUtils.show(AddFootMessageActivity.this, "请选择生日");

        }else if(TextUtils.isEmpty(shoeSize)){
            ToastUtils.show(AddFootMessageActivity.this, "请输入鞋码");
        }else {
            Map<String, Object> params = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("cmd", "41");
                jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                jsonObject.put("token", BaseApplication.getInstance().getToken());
                if("1".equals(editType)){
                    jsonObject.put("typeid","2");
                    jsonObject.put("userfoottypedataid", userFootDataDetailBean.getUserFootTypeDataID());
                }else{
                    jsonObject.put("typeid","1");
                    jsonObject.put("userfoottypedataid","0");
                }
                jsonObject.put("sex",sex);
                jsonObject.put("name",namestr);
                jsonObject.put("birthday",birthday);
                jsonObject.put("stature",heightstr);
                jsonObject.put("weight",weightstr);
                jsonObject.put("age",0);
                jsonObject.put("shoesize",shoeSize);
                jsonObject.put("measurecode","");
                jsonObject.put("footvarusvalgusid","0");
                jsonObject.put("footarchid","0");
                jsonObject.put("footDesc",editfootDesc);
                params.put("sendmsg", jsonObject.toString());
                saveFootMessage(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view){
        finish();
    }

    private void saveFootMessage(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if(baseRespMsg != null){
                    if (baseRespMsg.getResult() > 0) {
                        ToastUtils.show(BaseApplication.getInstance(), "保存成功");
                        finish();
                    } else {
                        ToastUtils.show(BaseApplication.getInstance(), baseRespMsg.getRetmsg());
                    }
                }else{
                    ToastUtils.show(BaseApplication.getInstance(), "操作失败，请稍后重试");
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


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radiobtn_man:
                sex = "1";
                break;
            case R.id.radiobtn_woman:
                sex = "2";
                break;
        }
    }

    /**
     * 选择生日
     */
    @OnClick(R.id.edit_birthday)
    public void chooseTime(){
//        DateTimePickDialog dateTimePicKDialog1 = new DateTimePickDialog(this, edit_birthday.getText().toString().trim());
//        dateTimePicKDialog1.dateTimePicKDialog(edit_birthday);
        if(!TextUtils.isEmpty(edit_birthday.getText())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null; //初始化date
            try {
                date = sdf.parse(edit_birthday.getText() +""); //Mon Jan 14 00:00:00 CST 2013
            } catch (ParseException e) {
                e.printStackTrace();
            }
            pvTime.setTime(date);
        }
        pvTime.show();

        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(String date) {
                edit_birthday.setText(date);
            }
        });
    }
}
