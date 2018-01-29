package com.zhurui.bunnymall.mine.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class ModifySetPassActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.edit_newpass)
    EditText edit_newpass;
    @Bind(R.id.edit_passsure)
    EditText edit_passsure;
    @Bind(R.id.btn_next)
    Button btn_next;
    @Bind(R.id.text_left)
    TextView text_left;
    private boolean isPhone = true;
    private String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_set_pass);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        text_title.setText(R.string.modify_pass);
        text_right.setVisibility(View.GONE);
        if(!"".equals(BaseApplication.getInstance().getUser().getEmail())){
            isPhone = false;
            text_left.setText("1 验证邮箱");
        }
        code = getIntent().getStringExtra("code");

    }

    @OnClick(R.id.btn_next)
    public void modifyPass(){
        String pass = edit_newpass.getText().toString().trim();
        String passsure = edit_passsure.getText().toString().trim();
        if(TextUtils.isEmpty(pass)){
            ToastUtils.show(ModifySetPassActivity.this, "请输入新密码");
        }else if(pass.length()<6 || pass.length()>16){
            ToastUtils.show(ModifySetPassActivity.this, "密码长度为6-16个字符");
        } else if(TextUtils.isEmpty(passsure)){
            ToastUtils.show(ModifySetPassActivity.this, "请输入确认密码");
        }else if(!passsure.equals(pass)){
            ToastUtils.show(ModifySetPassActivity.this, "两次密码输入不一样哦");
        }else {
            Map<String, Object> params = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("cmd", "20");
                jsonObject.put("state", "1");
                jsonObject.put("token", BaseApplication.getInstance().getToken() + "");
                jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                jsonObject.put("newpassword", pass  );
                if (isPhone) {
                    jsonObject.put("randomCode", "");
                    jsonObject.put("value", BaseApplication.getInstance().getUser().getMobile());
                    jsonObject.put("type", "1");
                } else {
                    jsonObject.put("randomCode",code);
                    jsonObject.put("value", BaseApplication.getInstance().getUser().getEmail());
                    jsonObject.put("type", "2");
                }
                params.put("sendmsg", jsonObject.toString());
                modifyPass(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void modifyPass(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    ToastUtils.show(ModifySetPassActivity.this, "修改成功");
                    finish();
                } else {
                    ToastUtils.show(ModifySetPassActivity.this, "修改失败");
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(ModifySetPassActivity.this, "修改失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(ModifySetPassActivity.this, "修改失败，请稍后重试");

            }
        });
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view){
        finish();
    }
}
