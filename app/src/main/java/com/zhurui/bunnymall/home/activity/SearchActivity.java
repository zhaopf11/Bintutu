package com.zhurui.bunnymall.home.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhurui.bunnymall.MainActivity;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.home.msg.PersonalCustomizedRsp;
import com.zhurui.bunnymall.home.msg.SearchResp;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.RecordSQLiteManager;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.FlowRadioGroup;
import com.zhurui.bunnymall.viewutils.MyDropDownMenu;
import com.zhurui.bunnymall.viewutils.ScreenDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class SearchActivity extends BaseActivity {


    @Bind(R.id.imgbtn_back)
    ImageButton imgbtn_back;
    @Bind(R.id.edit_search)
    EditText editText;
    @Bind(R.id.radio_group)
    FlowRadioGroup radioGroup;
    @Bind(R.id.imgbtn_right)
    TextView imgbtn_right;
    @Bind(R.id.img_delete)
    ImageView img_delete;
    @Bind(R.id.rel_history)
    RelativeLayout rel_history;

    //0、首页搜索1、私人定制2、分类搜索3、店铺内搜索
    private int state;
    private RecordSQLiteManager sqLiteManager = null;

    private String supplierid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        sqLiteManager = new RecordSQLiteManager(SearchActivity.this);
        initView();
        initData();
    }

    private void initView() {
        imgbtn_back.setVisibility(View.VISIBLE);
        imgbtn_right.setText(R.string.search);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // 搜索框的键盘搜索键点击回调
        editText.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                    boolean hasData = sqLiteManager.hasData(editText.getText().toString().trim());
                    if (!hasData) {
                        if (null != editText.getText().toString().trim() && !"".equals(editText.getText().toString().trim())) {
                            sqLiteManager.insertData(editText.getText().toString().trim());
                        }
                        queryAllData();
                    }
                    search(editText.getText().toString().trim());
                }
                return false;
            }
        });
    }

    private void initData() {
        queryAllData();
        state = getIntent().getIntExtra("state", 0);
        if (3 == state) {
            supplierid = getIntent().getStringExtra("supplierid");
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) group
                            .getChildAt(i);
                    if (radioButton.getId() == checkedId) {
                        editText.setText(radioButton.getText());
                        if(!TextUtils.isEmpty(radioButton.getText())){
                            editText.setSelection(radioButton.getText().length());
                            editText.requestFocus();  //这是关键
                        }
                        search(radioButton.getText() + "");
                    }
                }
            }
        });

    }


    private void queryAllData() {
        List<String> recordList = sqLiteManager.queryData("");
        radioGroup.removeAllViews();
        if (null != recordList && recordList.size() > 0) {
            for (String str : recordList) {
                RadioButton radioButton = (RadioButton) LayoutInflater.from(
                        getApplicationContext()).inflate(
                        R.layout.adapter_search_history, null);
                radioButton.setText(str);
                radioGroup.addView(radioButton);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        queryAllData();
        //清除列表界面选中的筛选条件
        ScreenDialog.checkMap = new HashMap<Integer, String>();
        ScreenDialog.childCheckMap = new HashMap<Integer, Integer>();
        ScreenDialog.strings = null;
        ScreenDialog.radioString = "";
        MyDropDownMenu.checkInfo = "";
        MyDropDownMenu.isPrice = false;
        MyDropDownMenu.priceoder = 0;
        MyDropDownMenu.price = -1;
        SharedPreferences pref = BaseApplication.getInstance().getSharedPreferences("ISSELETCED", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("isSeletceNum", 0);
        editor.commit();
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }

    @OnClick(R.id.img_delete)
    public void deleHistory() {
        radioGroup.setVisibility(View.GONE);
        sqLiteManager.deleteData();
        queryAllData();

    }

    @OnClick(R.id.imgbtn_right)
    public void toProductGrid() {
        String keyWord = editText.getText().toString().trim();

        boolean hasData = sqLiteManager.hasData(keyWord);
        if (!hasData) {
            if (null != keyWord && !"".equals(keyWord)) {
                sqLiteManager.insertData(keyWord);
            }
            queryAllData();
        }
        search(keyWord);
//        startActivity(new Intent(BaseApplication.getInstance(),ProductGridNormalActivity.class),false);
    }


    private void searchClassifyInfo(Map<String, Object> params,String keyWord) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<SearchResp>(this) {
            @Override
            public void onSuccess(Response response, SearchResp searchResp) {
                if (searchResp.getResult() > 0) {
                    Intent intent = new Intent(BaseApplication.getInstance(), ProductGridNormalActivity.class);
                    intent.putExtra("productlist", (Serializable) searchResp.getList());
                    intent.putExtra("keyWord",keyWord);
                    if (3 == state) {
                        intent.putExtra("supplierid", supplierid);
                        intent.putExtra("from", 5);
                    }
                    startActivity(intent, false);

                } else {
                    ToastUtils.show(SearchActivity.this, searchResp.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(SearchActivity.this, "搜索失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(SearchActivity.this, "搜索失败，请稍后重试");

            }
        });
    }


    private void searchCustomizedInfo(Map<String, Object> params,String keyWord) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<PersonalCustomizedRsp>(this) {
            @Override
            public void onSuccess(Response response, PersonalCustomizedRsp personalCustomizedRsp) {
                if (personalCustomizedRsp.getResult() > 0) {
//                    if (null != personalCustomizedRsp.getList() && personalCustomizedRsp.getList().size() > 0) {
                        Intent intent = new Intent();
                        intent.putExtra("list", (Serializable) personalCustomizedRsp.getList());
                        intent.putExtra("keyWord", keyWord);
                        setResult(RESULT_OK, intent);
                        finish();
//                    } else {
//                        Intent intent = new Intent();
//                        intent.putExtra("list", (Serializable) personalCustomizedRsp.getList());
//                        intent.putExtra("keyWord",keyWord);
//                        setResult(RESULT_OK, intent);
//                        finish();
//                        ToastUtils.show(SearchActivity.this, "暂无私人定制商品");
//                    }
                } else {
                    ToastUtils.show(SearchActivity.this, personalCustomizedRsp.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(SearchActivity.this, "搜索失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(SearchActivity.this, "搜索失败，请稍后重试");

            }
        });
    }

    private void searchInfo(Map<String, Object> params,String keyWord) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<SearchResp>(this) {
            @Override
            public void onSuccess(Response response, SearchResp searchResp) {
                if (searchResp.getResult() > 0) {
                    startActivity(new Intent(BaseApplication.getInstance(),
                            ProductGridNormalActivity.class).putExtra("productlist", (Serializable) searchResp.getList()).putExtra("keyWord",keyWord), false);
                } else {
                    ToastUtils.show(SearchActivity.this, searchResp.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(SearchActivity.this, "搜索失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(SearchActivity.this, "搜索失败，请稍后重试");

            }
        });
    }

    private void search(String keyWord){
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        switch (state) {
            case 0:
                startActivity(new Intent(BaseApplication.getInstance(),
                        ProductGridNormalActivity.class).putExtra("keyWord",keyWord), false);
                break;
            case 1:
                if (null != BaseApplication.getInstance().getUser()) {
                    try {
                        jsonObject.put("cmd", "16");
                        jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                        jsonObject.put("token", BaseApplication.getInstance().getToken());
                        jsonObject.put("pagesize", "20");
                        jsonObject.put("pageno", "1");
                        jsonObject.put("keyword", keyWord);
                        params.put("sendmsg", jsonObject.toString());
                        searchCustomizedInfo(params,keyWord);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                try {
                    jsonObject.put("cmd", "14");
                    jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                    jsonObject.put("token", BaseApplication.getInstance().getToken());
                    jsonObject.put("pagesize", "20");
                    jsonObject.put("pageno", "1");
                    jsonObject.put("keyword", keyWord);
                    params.put("sendmsg", jsonObject.toString());
                    searchClassifyInfo(params,keyWord);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    jsonObject.put("cmd", "69");
                    jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                    jsonObject.put("token", BaseApplication.getInstance().getToken());
                    jsonObject.put("smalltypeid", "0");
                    jsonObject.put("fashionid", "0");
                    jsonObject.put("supplierid", supplierid);
                    jsonObject.put("primaryid", "0");
                    jsonObject.put("category", "0");
                    jsonObject.put("sortby", "0");
                    jsonObject.put("sellnumber", "0");
                    jsonObject.put("priceoder", "0");
                    jsonObject.put("pagesize", "200");
                    jsonObject.put("pageno", "1");
                    jsonObject.put("keywords", keyWord);
                    jsonObject.put("msg", new JSONArray());
                    params.put("sendmsg", jsonObject.toString());
                    searchClassifyInfo(params,keyWord);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
