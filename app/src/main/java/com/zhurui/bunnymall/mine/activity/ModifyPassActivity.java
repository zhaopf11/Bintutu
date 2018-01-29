package com.zhurui.bunnymall.mine.activity;

import android.app.Activity;
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
import com.zhurui.bunnymall.common.model.BaseBean;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.bean.User;
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

public class ModifyPassActivity extends BaseActivity {
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.text_phonenum)
    EditText text_phonenum;
    @Bind(R.id.text_sendcode)
    TextView text_sendcode;
    @Bind(R.id.edit_code)
    EditText edit_code;
    @Bind(R.id.btn_next)
    Button btn_next;
    @Bind(R.id.text_left)
    TextView text_left;
    @Bind(R.id.text_rightprogress)
    TextView text_rightprogress;
    private int state = -1;
    @Bind(R.id.lin_top)
    LinearLayout lin_top;

    private static final int TASK_TIMER_MESSAGE = 0;
    private static final int TASK_DENIED_MESSAGE = 1;
    private static final int TASK_TIMER_RESET_MESSAGE = 2;
    private Timer mTaskTimer;
    private boolean isTimerStop = false;
    private boolean isPhone = true;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pass);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //1:修改登录密码2：修改手机号码3:修改手机号码设置新密码界面
        state = getIntent().getIntExtra("state", 1);
        if (1 == state) {
            text_title.setText(R.string.modify_pass);
            text_rightprogress.setText("2 输入新密码");
            if (!"".equals(BaseApplication.getInstance().getUser().getMobile())) {
                text_phonenum.setText(RegexUtil.toparsephonenum(BaseApplication.getInstance().getUser().getMobile()));
            } else if (!"".equals(BaseApplication.getInstance().getUser().getEmail())) {
                isPhone = false;
                text_left.setText("1 验证邮箱");
                text_phonenum.setText(BaseApplication.getInstance().getUser().getEmail());
            }

        } else if (2 == state) {
            if (!"".equals(BaseApplication.getInstance().getUser().getMobile())) {
                text_title.setText(R.string.modify_phonenum);
                text_rightprogress.setText("2 输入新手机号");
                text_phonenum.setText(RegexUtil.toparsephonenum(BaseApplication.getInstance().getUser().getMobile()));

            } else if (!"".equals(BaseApplication.getInstance().getUser().getEmail())) {
                isPhone = false;
                text_title.setText(R.string.modify_email);
                text_left.setText("1 验证邮箱");
                text_rightprogress.setText("2 输入新邮箱");
                text_phonenum.setText(BaseApplication.getInstance().getUser().getEmail());

            }

        }
        text_right.setVisibility(View.GONE);
        btn_next.setEnabled(true);
    }

    @OnClick(R.id.text_sendcode)
    public void getCode() {
        String phonenum = text_phonenum.getText().toString().trim();
        Long time = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        //修改登录密码 获取验证码
        if (1 == state) {
            if (isPhone) {//修改手机登录密码，获取验证码
                if (TextUtils.isEmpty(phonenum)) {
                    ToastUtils.show(ModifyPassActivity.this, "请输入手机号码");
                } else {
                    try {
                        jsonObject.put("smstype", "4");
                        jsonObject.put("cmd", "8");
                        jsonObject.put("mobile", BaseApplication.getInstance().getUser().getMobile());
                        jsonObject.put("timeStamp", time);
                        jsonObject.put("sign", MD5Util.encode("84"+ BaseApplication.getInstance().getUser().getMobile() + Contants.sinkey + time).toLowerCase());
                        params.put("sendmsg", jsonObject.toString());
                        getCode(params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //修改邮箱登录密码，获取验证码
                try {
                    jsonObject.put("cmd", "2");
                    jsonObject.put("email", BaseApplication.getInstance().getUser().getEmail());
                    jsonObject.put("type", "modify");
                    params.put("sendmsg", jsonObject.toString());
                    getCode(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (2 == state) {
            //修改手机号 确认手机号 获取验证码
            if (isPhone) {
                if (TextUtils.isEmpty(phonenum)) {
                    ToastUtils.show(ModifyPassActivity.this, "请输入手机号码");
                } else {
                    try {
                        jsonObject.put("smstype", "10");
                        jsonObject.put("cmd", "8");
                        jsonObject.put("mobile", BaseApplication.getInstance().getUser().getMobile());
                        jsonObject.put("timeStamp", time);
                        jsonObject.put("sign", MD5Util.encode("810"+ BaseApplication.getInstance().getUser().getMobile() + Contants.sinkey + time).toLowerCase());
                        params.put("sendmsg", jsonObject.toString());
                        getCode(params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
//                老的，type=change 新的 type=reg
                try {
                    jsonObject.put("cmd", "2");
                    jsonObject.put("email", BaseApplication.getInstance().getUser().getEmail());
                    jsonObject.put("type", "change");
                    params.put("sendmsg", jsonObject.toString());
                    getCode(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (3 == state) {
            //修改手机号 验证新的手机号  获取验证码
            if (isPhone) {
                if (TextUtils.isEmpty(phonenum)) {
                    ToastUtils.show(ModifyPassActivity.this, "请输入手机号码");
                } else {
                    try {
                        jsonObject.put("smstype", "1");
                        jsonObject.put("cmd", "8");
                        jsonObject.put("mobile", phonenum);
                        jsonObject.put("timeStamp", time);
                        jsonObject.put("sign", MD5Util.encode("81"+ phonenum + Contants.sinkey + time).toLowerCase());
                        params.put("sendmsg", jsonObject.toString());
                        getCode(params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //修改邮箱 验证新的邮箱  获取验证码
                if (TextUtils.isEmpty(phonenum)) {
                    ToastUtils.show(ModifyPassActivity.this, "请输入邮箱");
                } else {
                    try {
                        jsonObject.put("cmd", "2");
                        jsonObject.put("email", phonenum);
                        jsonObject.put("type", "reg");
                        params.put("sendmsg", jsonObject.toString());
                        getCode(params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void getCode(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    ToastUtils.show(ModifyPassActivity.this, baseRespMsg.getRetmsg());
                    mTaskTimer = new Timer();
                    mTaskTimer.scheduleAtFixedRate(new ConfirmButtonTimerTask(), 0,
                            1000);
                } else {
                    ToastUtils.show(ModifyPassActivity.this, getResources().getString(R.string.send_identifyingcode_err));
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(ModifyPassActivity.this, "验证码发送失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(ModifyPassActivity.this, "验证码发送失败，请稍后重试");

            }
        });
    }

    private void checkAccount(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    if (1 == state) {
                        ToastUtils.show(ModifyPassActivity.this, "验证成功");
                        startActivity(new Intent(BaseApplication.getInstance(), ModifySetPassActivity.class).putExtra("code",code), false);
                        finish();
                    } else if (2 == state) {
                        ToastUtils.show(ModifyPassActivity.this, "验证成功");
                        timerHandler.sendEmptyMessage(TASK_DENIED_MESSAGE);
                        text_left.setTextColor(getResources().getColor(R.color.gray));
                        text_left.setBackgroundResource(R.drawable.left_gray);
                        text_rightprogress.setTextColor(getResources().getColor(R.color.color_d2ba91));
                        lin_top.setBackgroundColor(getResources().getColor(R.color.navigation_bar_bg));
                        text_phonenum.setEnabled(true);
                        if(isPhone){
                            text_phonenum.setHint("请输入新手机号码");
                        }else{
                            text_phonenum.setHint("请输入新邮箱");
                        }
                        text_phonenum.setText("");
                        edit_code.setText("");
                        state = 3;
                    } else {
                        ToastUtils.show(ModifyPassActivity.this, "修改成功");
                        User user = BaseApplication.getInstance().getUser();
                        user.setMobile(text_phonenum.getText().toString().trim());
                        BaseApplication.getInstance().putUser(user, BaseApplication.getInstance().getToken());
                        finish();
                    }

                } else {
                    ToastUtils.show(ModifyPassActivity.this, baseRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(ModifyPassActivity.this, "修改失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(ModifyPassActivity.this, "修改失败，请稍后重试");

            }
        });
    }

    @OnClick(R.id.btn_next)
    public void toNext() {
        code = edit_code.getText().toString().trim();
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        if (1 == state) {
            if (TextUtils.isEmpty(code)) {
                ToastUtils.show(ModifyPassActivity.this, "请输入验证码");

            } else {
                try {
                    jsonObject.put("cmd", "20");
                    jsonObject.put("state", "0");
                    jsonObject.put("randomCode", code);
                    jsonObject.put("token", BaseApplication.getInstance().getToken() + "");
                    jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                    jsonObject.put("newpassword", "");
                    if (isPhone) {
                        jsonObject.put("value", BaseApplication.getInstance().getUser().getMobile());
                        jsonObject.put("type", "1");

                    } else {
                        jsonObject.put("value", BaseApplication.getInstance().getUser().getEmail());
                        jsonObject.put("type", "2");

                    }
                    params.put("sendmsg", jsonObject.toString());
                    checkAccount(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else {
            if (2 == state) {
                if (TextUtils.isEmpty(code)) {
                    ToastUtils.show(ModifyPassActivity.this, "请输入验证码");
                } else {
                    if (isPhone) {
                        //验证老的手机号码
                        try {
                            jsonObject.put("cmd", "15");
                            jsonObject.put("account", BaseApplication.getInstance().getUser().getMobile());
                            jsonObject.put("state", "0");
                            jsonObject.put("randomcode", code);
                            jsonObject.put("typeid", "0");
                            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                            params.put("sendmsg", jsonObject.toString());
                            checkAccount(params);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //验证老的邮箱
                        try {
                            jsonObject.put("cmd", "15");
                            jsonObject.put("account", BaseApplication.getInstance().getUser().getEmail());
                            jsonObject.put("state", "0");
                            jsonObject.put("randomcode", code);
                            jsonObject.put("typeid", "1");
                            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                            params.put("sendmsg", jsonObject.toString());
                            checkAccount(params);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } else if (3 == state) {
                String account = text_phonenum.getText().toString().trim();
                if (isPhone) {
                    if (TextUtils.isEmpty(account)) {
                        ToastUtils.show(ModifyPassActivity.this, "请输入新的手机号码");
                    } else if (!RegexUtil.isMobileNO(account)) {
                        ToastUtils.show(ModifyPassActivity.this, "请输入正确的手机号码");
                    } else if (TextUtils.isEmpty(code)) {
                        ToastUtils.show(ModifyPassActivity.this, "请输入验证码");
                    } else {
                        try {
                            jsonObject.put("cmd", "15");
                            jsonObject.put("account", account);
                            jsonObject.put("state", "1");
                            jsonObject.put("randomcode", code);
                            jsonObject.put("typeid", "0");
                            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                            params.put("sendmsg", jsonObject.toString());
                            checkAccount(params);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (TextUtils.isEmpty(account)) {
                        ToastUtils.show(ModifyPassActivity.this, "请输入新的邮箱账号");
                    } else if (!RegexUtil.isEmail2(account)) {
                        ToastUtils.show(ModifyPassActivity.this, "请输入正确的邮箱账号");
                    } else if (TextUtils.isEmpty(code)) {
                        ToastUtils.show(ModifyPassActivity.this, "请输入验证码");
                    } else {
                        try {
                            jsonObject.put("cmd", "15");
                            jsonObject.put("account", account);
                            jsonObject.put("state", "1");
                            jsonObject.put("randomcode", code);
                            jsonObject.put("typeid", "1");
                            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                            params.put("sendmsg", jsonObject.toString());
                            checkAccount(params);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
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
                mTaskTimer.cancel();
                counter = 61;
                isTimerStop = true;
                text_sendcode.setEnabled(true);
                text_sendcode.setText(getString(R.string.send_code));
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


    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        if (3 == state) {
            state = 2;
            if (isPhone) {
                text_phonenum.setText(RegexUtil.toparsephonenum(BaseApplication.getInstance().getUser().getMobile()));
                text_left.setText("1 验证手机号");
                text_left.setTextColor(getResources().getColor(R.color.color_d2ba91));
                text_left.setBackgroundResource(R.drawable.left_white);
                text_rightprogress.setTextColor(getColor(R.color.gray));
                lin_top.setBackgroundColor(getColor(R.color.gray_bg));
                text_rightprogress.setText("2 输入新手机号");
                edit_code.setText("");
                text_phonenum.setEnabled(false);

            } else {
                text_phonenum.setText(BaseApplication.getInstance().getUser().getEmail());
                text_left.setText("1 验证邮箱");
                text_left.setTextColor(getResources().getColor(R.color.color_d2ba91));
                text_left.setBackgroundResource(R.drawable.left_white);
                text_rightprogress.setTextColor(getColor(R.color.gray));
                lin_top.setBackgroundColor(getColor(R.color.gray_bg));
                text_rightprogress.setText("2 输入新邮箱");
                edit_code.setText("");
                text_phonenum.setEnabled(false);
            }
        } else {
            finish();

        }
    }
}
