package com.zhurui.bunnymall.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhurui.bunnymall.MainActivity;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.bean.User;
import com.zhurui.bunnymall.mine.msg.AccountLoginResp;
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

/**
 * Created by Administrator on 2017/11/23.
 */

public class RegisterWelcomeActivity extends BaseActivity {
    @Bind(R.id.edit_phone)
    EditText edit_phone;
    @Bind(R.id.edit_code)
    EditText edit_code;
    @Bind(R.id.text_sendcode)
    TextView text_sendcode;
    @Bind(R.id.btn_next)
    Button btn_next;
    @Bind(R.id.to_skip)
    Button to_skip;

    private Timer mTaskTimer;
    private static final int TASK_TIMER_MESSAGE = 0;
    private static final int TASK_DENIED_MESSAGE = 1;
    private static final int TASK_TIMER_RESET_MESSAGE = 2;
    private boolean isTimerStop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_register);
        ButterKnife.bind(this);
        to_skip.getBackground().setAlpha(100);//0~255透明度值
    }


    @OnClick(R.id.text_sendcode)
    public void getCode() {
        String accoount = edit_phone.getText().toString().trim();
        Long time = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        if (TextUtils.isEmpty(accoount)) {
            ToastUtils.show(RegisterWelcomeActivity.this, "请输入手机号码");
        } else if (!RegexUtil.isMobileNO(accoount)) {
            ToastUtils.show(RegisterWelcomeActivity.this, "请输入正确的手机号码");
        } else {
            try {
                jsonObject.put("smstype", "12");
                jsonObject.put("cmd", "8");
                jsonObject.put("mobile", accoount);
                jsonObject.put("timeStamp", time);
                jsonObject.put("sign", MD5Util.encode("812" + accoount + Contants.sinkey + time).toLowerCase());
                params.put("sendmsg", jsonObject.toString());
                getCode(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (edit_code.isFocused()) {
                //已获得焦点
            } else {
                edit_code.requestFocus();//获得焦点
            }

        }
    }

    @OnClick(R.id.btn_next)
    public void Login() {
        String account = edit_phone.getText().toString().trim();
        String code = edit_code.getText().toString().trim();
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.show(RegisterWelcomeActivity.this, "请输入手机号码");
        } else if (!RegexUtil.isMobileNO(account)) {
            ToastUtils.show(RegisterWelcomeActivity.this, "请输入正确的手机号码");
        } else if (TextUtils.isEmpty(code)) {
            ToastUtils.show(RegisterWelcomeActivity.this, "请输入验证码");
        } else {
            try {
                jsonObject.put("cmd", "7");
                jsonObject.put("mobile", account);
                jsonObject.put("verifycode", code);
                params.put("sendmsg", jsonObject.toString());
                accountLogin(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.to_skip)
    public void skip() {
       goToMain();
    }

    private void getCode(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    ToastUtils.show(RegisterWelcomeActivity.this, "验证码发送成功");
                    mTaskTimer = new Timer();
                    mTaskTimer.scheduleAtFixedRate(new RegisterWelcomeActivity.ConfirmButtonTimerTask(), 0,
                            1000);
                } else {
                    ToastUtils.show(RegisterWelcomeActivity.this, baseRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(RegisterWelcomeActivity.this, "验证码发送失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(RegisterWelcomeActivity.this, "验证码发送失败，请稍后重试");

            }
        });
    }

    private void accountLogin(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<AccountLoginResp>(this) {
            @Override
            public void onSuccess(Response response, AccountLoginResp loginResp) {
                if (loginResp != null && loginResp.getResult() > 0) {
                    User user = loginResp.getUserinfo().get(0);
                    BaseApplication application = BaseApplication.getInstance();
                    application.putUser(user, loginResp.getToken());
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
                    if (imm.isActive()) {//如果开启
                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
                    }
                   goToMain();
                } else {
                    ToastUtils.show(RegisterWelcomeActivity.this, loginResp.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(RegisterWelcomeActivity.this, getResources().getString(R.string.login_err));
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(RegisterWelcomeActivity.this, getResources().getString(R.string.login_err));

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

    void goToMain(){
        SharedPreferences pref = RegisterWelcomeActivity.this.getSharedPreferences("ISFIRSTIN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isFirstIn", false);
        editor.commit();
        startActivity(new Intent(RegisterWelcomeActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
}
