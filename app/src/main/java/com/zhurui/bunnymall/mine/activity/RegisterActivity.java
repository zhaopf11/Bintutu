package com.zhurui.bunnymall.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.activity.WebViewActivity;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.RegexUtil;
import com.zhurui.bunnymall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaoneng.utils.MD5Util;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity {
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    //1:默认为注册2：忘记密码界面
    private int state = 1;
    @Bind(R.id.lin_infomind)
    LinearLayout lin_infomind;
    @Bind(R.id.edit_account)
    EditText edit_account;
    @Bind(R.id.edit_code)
    EditText edit_code;
    @Bind(R.id.edit_password)
    EditText edit_password;
    @Bind(R.id.text_sendcode)
    TextView text_sendcode;
    @Bind(R.id.btn_next)
    Button btn_next;

    private static final int TASK_TIMER_MESSAGE = 0;
    private static final int TASK_DENIED_MESSAGE = 1;
    private static final int TASK_TIMER_RESET_MESSAGE = 2;
    private Timer mTaskTimer;
    private boolean isTimerStop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        state = getIntent().getIntExtra("state", 1);
        if (1 == state) {
            text_title.setText(R.string.register);
        } else {
            text_title.setText(R.string.forget_pass);
            lin_infomind.setVisibility(View.GONE);
        }
        text_right.setVisibility(View.GONE);
    }

    @OnClick(R.id.text_sendcode)
    public void getCode() {
        String account = edit_account.getText().toString().trim();
        Long time = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            if (TextUtils.isEmpty(account)) {
                ToastUtils.show(RegisterActivity.this, "请输入手机号码");
            } else if (!RegexUtil.isMobileNO(account) && !RegexUtil.isEmail(account)) {
                ToastUtils.show(RegisterActivity.this, "请输入正确的手机号码");
            } else {
                switch (state) {
                    case 1:
                        if (account.indexOf("@") > 0) {
                            if (!RegexUtil.isEmail2(account)) {
                                ToastUtils.show(RegisterActivity.this, "请输入正确的邮箱");
                            } else {
                                jsonObject.put("cmd", "2");
                                jsonObject.put("email", account);
                                jsonObject.put("type", "reg");
                                params.put("sendmsg", jsonObject.toString());
                                sendCodeEmail(params);
                            }
                        } else {
                            if (!RegexUtil.isMobileNO(account)) {
                                ToastUtils.show(RegisterActivity.this, "请输入正确的手机号码");
                            } else {
                                jsonObject.put("cmd", "8");
                                jsonObject.put("mobile", account);
                                jsonObject.put("smstype", "1");
                                jsonObject.put("timeStamp", time);
                                jsonObject.put("sign", MD5Util.encode("81"+ account + Contants.sinkey + time).toLowerCase());
                                params.put("sendmsg", jsonObject.toString());
                                sendCodePhone(params);
                            }
                        }
                        break;
                    case 2:
                        if (account.indexOf("@") > 0) {
                            if (!RegexUtil.isEmail2(account)) {
                                ToastUtils.show(RegisterActivity.this, "请输入正确的邮箱");
                            } else {
                                jsonObject.put("cmd", "2");
                                jsonObject.put("email", account);
                                jsonObject.put("type", "findpwd");
                                params.put("sendmsg", jsonObject.toString());
                                sendCodeEmail(params);
                            }
                        } else {
                            if (!RegexUtil.isMobileNO(account)) {
                                ToastUtils.show(RegisterActivity.this, "请输入正确的手机号码");
                            } else {
                                jsonObject.put("cmd", "8");
                                jsonObject.put("mobile", account);
                                jsonObject.put("smstype", "2");
                                jsonObject.put("timeStamp", time);
                                jsonObject.put("sign", MD5Util.encode("82"+ account + Contants.sinkey + time).toLowerCase());
                                params.put("sendmsg", jsonObject.toString());
                                sendCodePhone(params);
                            }
                        }
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(edit_code.isFocused()){
            //已获得焦点
        }else{
            edit_code.requestFocus();//获得焦点
        }
    }

    private void sendCodePhone(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    ToastUtils.show(RegisterActivity.this, "验证码发送成功");
                    mTaskTimer = new Timer();
                    mTaskTimer.scheduleAtFixedRate(new ConfirmButtonTimerTask(), 0,
                            1000);
                } else if (-3 == baseRespMsg.getResult()) {
                    ToastUtils.show(RegisterActivity.this, "该手机号已注册");
                } else {
                    ToastUtils.show(RegisterActivity.this, baseRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(RegisterActivity.this, "发送失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(RegisterActivity.this, "发送失败，请稍后重试");

            }
        });
    }

    private void sendCodeEmail(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    ToastUtils.show(RegisterActivity.this, "验证码发送成功");
                    mTaskTimer = new Timer();
                    mTaskTimer.scheduleAtFixedRate(new ConfirmButtonTimerTask(), 0,
                            1000);
                } else if (-3 == baseRespMsg.getResult()) {
                    ToastUtils.show(RegisterActivity.this, "该邮箱已注册");
                } else {
                    ToastUtils.show(RegisterActivity.this, getResources().getString(R.string.send_identifyingcode_err));
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(RegisterActivity.this, "发送失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(RegisterActivity.this, "发送失败，请稍后重试");

            }
        });
    }


    private final Handler timerHandler = new Handler(new Handler.Callback() {
        private int counter = 61;

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == TASK_TIMER_MESSAGE) {
                counter--;
                if (counter == 0) {
                    mTaskTimer.cancel();
                    counter = 61;
                    isTimerStop = true;
                    text_sendcode.setEnabled(true);
                    text_sendcode.setText(getString(R.string.send_code));
                } else {
                    text_sendcode.setEnabled(false);
                    text_sendcode.setText(getString(R.string.btn_hqyzm, counter));
                }
            } else if (msg.what == TASK_DENIED_MESSAGE) {
                
            } else if (msg.what == TASK_TIMER_RESET_MESSAGE) {
            }
            return true;
        }

    });

    private class ConfirmButtonTimerTask extends TimerTask {
        public ConfirmButtonTimerTask() {
            timerHandler.sendEmptyMessage(TASK_TIMER_RESET_MESSAGE);
        }

        @Override
        public void run() {
            timerHandler.sendEmptyMessage(TASK_TIMER_MESSAGE);
        }
    }

    @OnClick(R.id.btn_next)
    public void btnSubmit() {
        String account = edit_account.getText().toString().trim();
        String code = edit_code.getText().toString().trim();
        String pass = edit_password.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.show(RegisterActivity.this, "请输入手机号码/邮箱");
        } else if (!RegexUtil.isMobileNO(account) && !RegexUtil.isEmail(account)) {
            ToastUtils.show(RegisterActivity.this, "请输入手机号码/邮箱");
        } else if (TextUtils.isEmpty(code)) {
            ToastUtils.show(RegisterActivity.this, "请输入验证码");
        } else if (TextUtils.isEmpty(pass)) {
            ToastUtils.show(RegisterActivity.this, "请输入密码");
        } else if (pass.length() < 6 && pass.length() > 16) {
            ToastUtils.show(RegisterActivity.this, "请输入6-16位数字或大小写字母");
        } else {
            Map<String, Object> params = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            try {
                switch (state) {
                    case 1:
                        if (RegexUtil.isEmail2(account)) {
                            jsonObject.put("cmd", "3");
                            jsonObject.put("email", account);
                            jsonObject.put("verifycode", code);
                            jsonObject.put("password", pass);
                            params.put("sendmsg", jsonObject.toString());
                            registerEmail(params);
                        } else if (RegexUtil.isMobileNO(account)) {
                            jsonObject.put("cmd", "4");
                            jsonObject.put("mobile", account);
                            jsonObject.put("randomCode", code);
                            jsonObject.put("password", pass);
                            params.put("sendmsg", jsonObject.toString());
                            registerPhone(params);
                        }
                        break;
                    case 2:
                        if (RegexUtil.isMobileNO(account)) {
                            jsonObject.put("cmd", "5");
                            jsonObject.put("mobile", account);
                            jsonObject.put("verifycode", code);
                            jsonObject.put("password", pass);
                            params.put("sendmsg", jsonObject.toString());
                            forgetPass(params);
                        } else if (RegexUtil.isEmail2(account)) {
                            //邮箱忘记密码接口
                            jsonObject.put("cmd", "22");
                            jsonObject.put("email", account);
                            jsonObject.put("verifycode", code);
                            jsonObject.put("password", pass);
                            params.put("sendmsg", jsonObject.toString());
                            forgetPass(params);
                        }
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerEmail(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    ToastUtils.show(RegisterActivity.this, "注册成功");
                    finish();
                } else {
                    ToastUtils.show(RegisterActivity.this, "注册失败，请稍后重试");
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(RegisterActivity.this, "注册失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(RegisterActivity.this, "注册失败，请稍后重试");

            }
        });
    }

    private void registerPhone(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    ToastUtils.show(RegisterActivity.this, "注册成功");
                    finish();
                } else {
                    ToastUtils.show(RegisterActivity.this, baseRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(RegisterActivity.this, "注册失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(RegisterActivity.this, "注册失败，请稍后重试");

            }
        });
    }

    private void forgetPass(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    ToastUtils.show(RegisterActivity.this, "修改成功");
                    finish();
                } else {
                    ToastUtils.show(RegisterActivity.this, baseRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(RegisterActivity.this, "修改失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(RegisterActivity.this, "修改失败，请稍后重试");

            }
        });
    }

    @OnClick(R.id.text_protocol)
    public void toProtocol(){

        startActivity(new Intent(BaseApplication.getInstance(), WebViewActivity.class).putExtra("url","file:///android_asset/registeragreement.html"));

    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
