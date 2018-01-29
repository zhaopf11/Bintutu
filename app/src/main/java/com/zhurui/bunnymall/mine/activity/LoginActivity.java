package com.zhurui.bunnymall.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.utils.MD5;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zhurui.bunnymall.MainActivity;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.bean.LogisticsCompanyBean;
import com.zhurui.bunnymall.mine.bean.User;
import com.zhurui.bunnymall.mine.msg.AccountLoginResp;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.RegexUtil;
import com.zhurui.bunnymall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.utils.MD5Util;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;

    @Bind(R.id.edit_account)
    EditText edit_account;
    @Bind(R.id.edit_code)
    EditText edit_code;
    @Bind(R.id.text_sendcode)
    TextView text_sendcode;
    @Bind(R.id.text_accountlogin)
    TextView text_accountlogin;
    @Bind(R.id.text_forgetpass)
    TextView text_forgetpass;
    @Bind(R.id.btn_next)
    Button btn_next;
    @Bind(R.id.lin_wechatlogin)
    LinearLayout lin_wechatlogin;
    @Bind(R.id.lin_qqlogin)
    LinearLayout lin_qqlogin;
    @Bind(R.id.lin_sinalogin)
    LinearLayout lin_sinalogin;
    //1:手机号快捷登陆2：账号密码登录
    private int state = 1;

    private static final int TASK_TIMER_MESSAGE = 0;
    private static final int TASK_DENIED_MESSAGE = 1;
    private static final int TASK_TIMER_RESET_MESSAGE = 2;
    private Timer mTaskTimer;
    private boolean isTimerStop = false;

    private static final String APPID = "1106268932";

    private Tencent mTencent; //qq主操作对象
    private IUiListener loginListener; //授权登录监听器
    private IUiListener userInfoListener; //获取用户信息监听器
    private String scope; //获取信息的范围参数
    private UserInfo userInfo; //qq用户信息

    private static final String APP_ID = "wxxxxxxx"; //替换为申请到的app id

    private static IWXAPI wx_api; //全局的微信api对象
    private int loginCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initData();
        wx_api = WXAPIFactory.createWXAPI(LoginActivity.this, APP_ID, true);
        wx_api.registerApp(APP_ID);
    }

    private void initView() {
        text_title.setText(R.string.login);
        text_right.setText(R.string.register);
        loginCode = getIntent().getIntExtra("TO_LOGIN", 0);
        //初始化登录界面，不同登录方式，界面不同
//        User user = BaseApplication.getInstance().getUser();
//        if(null !=user && null !=user.getMobile() && !"".equals(user.getMobile())){
//
//        }
    }


    private void initData() {
        //初始化qq主操作对象
        mTencent = Tencent.createInstance(APPID, LoginActivity.this);
        //要所有权限，不然会再次申请增量权限，这里不要设置成get_user_info,add_t
        scope = "all";

        loginListener = new IUiListener() {

            @Override
            public void onError(UiError arg0) {
                // TODO Auto-generated method stub

            }

            /**
             * 返回json数据样例
             *
             * {"ret":0,"pay_token":"D3D678728DC580FBCDE15722B72E7365",
             * "pf":"desktop_m_qq-10000144-android-2002-",
             * "query_authority_cost":448,
             * "authority_cost":-136792089,
             * "openid":"015A22DED93BD15E0E6B0DDB3E59DE2D",
             * "expires_in":7776000,
             * "pfkey":"6068ea1c4a716d4141bca0ddb3df1bb9",
             * "msg":"",
             * "access_token":"A2455F491478233529D0106D2CE6EB45",
             * "login_cost":499}
             */
            @Override
            public void onComplete(Object value) {
                // TODO Auto-generated method stub

                System.out.println("有数据返回..");
                if (value == null) {
                    return;
                }

                try {
                    JSONObject jo = (JSONObject) value;

                    int ret = jo.getInt("ret");

                    System.out.println("json=" + String.valueOf(jo));

                    if (ret == 0) {
                        Toast.makeText(LoginActivity.this, "登录成功",
                                Toast.LENGTH_LONG).show();

                        String openID = jo.getString("openid");
                        String accessToken = jo.getString("access_token");
                        String expires = jo.getString("expires_in");
                        mTencent.setOpenId(openID);
                        mTencent.setAccessToken(accessToken, expires);
                        QQToken mQQToken = mTencent.getQQToken();
                        userInfo = new UserInfo(LoginActivity.this, mQQToken);
                        userInfo.getUserInfo(userInfoListener);
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub

            }
        };

        userInfoListener = new IUiListener() {

            @Override
            public void onError(UiError arg0) {
                // TODO Auto-generated method stub

            }

            /**
             * 返回用户信息样例
             *
             * {"is_yellow_year_vip":"0","ret":0,
             * "figureurl_qq_1":"http:\/\/q.qlogo.cn\/qqapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/40",
             * "figureurl_qq_2":"http:\/\/q.qlogo.cn\/qqapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/100",
             * "nickname":"攀爬←蜗牛","yellow_vip_level":"0","is_lost":0,"msg":"",
             * "city":"黄冈","
             * figureurl_1":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/50",
             * "vip":"0","level":"0",
             * "figureurl_2":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/100",
             * "province":"湖北",
             * "is_yellow_vip":"0","gender":"男",
             * "figureurl":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/30"}
             */
            @Override
            public void onComplete(Object arg0) {
                // TODO Auto-generated method stub
                if (arg0 == null) {
                    return;
                }
                try {
                    JSONObject jo = (JSONObject) arg0;
                    int ret = jo.getInt("ret");
                    System.out.println("json=" + String.valueOf(jo));
                    String nickName = jo.getString("nickname");
                    String gender = jo.getString("gender");

                    Toast.makeText(LoginActivity.this, "你好，" + nickName,
                            Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    // TODO: handle exception
                }


            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub

            }
        };
    }

    @OnClick(R.id.text_accountlogin)
    public void toAnotherLogin() {
        uiContorl(state);
    }


    private void uiContorl(int state) {
        switch (state) {
            case 1:
                text_accountlogin.setText(R.string.phont_login);
                text_sendcode.setVisibility(View.GONE);
                edit_account.setHint("请输入手机号");
                edit_code.setHint("请输入密码");
                this.state = 2;
                edit_code.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case 2:
                this.state = 1;
                text_accountlogin.setText(R.string.account_login);
                text_sendcode.setVisibility(View.VISIBLE);
                edit_account.setHint("请输入手机号");
                edit_code.setHint("请输入验证码");
                edit_code.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
        }
    }


    @OnClick(R.id.text_right)
    public void toRegister() {
        startActivity(new Intent(BaseApplication.getInstance(), RegisterActivity.class).putExtra("state", 1), false);
    }

    @OnClick(R.id.text_forgetpass)
    public void toForgetPass() {
        startActivity(new Intent(BaseApplication.getInstance(), RegisterActivity.class).putExtra("state", 2), false);
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

    @OnClick(R.id.text_sendcode)
    public void getCode() {
        String accoount = edit_account.getText().toString().trim();
        Long time = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        if (TextUtils.isEmpty(accoount)) {
            ToastUtils.show(LoginActivity.this, "请输入手机号码");
        } else if (!RegexUtil.isMobileNO(accoount)) {
            ToastUtils.show(LoginActivity.this, "请输入正确的手机号码");
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
        String account = edit_account.getText().toString().trim();
        String code = edit_code.getText().toString().trim();
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        if (1 == state) {
            if (TextUtils.isEmpty(account)) {
                ToastUtils.show(LoginActivity.this, "请输入手机号码");
            } else if (!RegexUtil.isMobileNO(account)) {
                ToastUtils.show(LoginActivity.this, "请输入正确的手机号码");
            } else if (TextUtils.isEmpty(code)) {
                ToastUtils.show(LoginActivity.this, "请输入验证码");
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
        if (2 == state) {
            if (TextUtils.isEmpty(account)) {
                ToastUtils.show(LoginActivity.this, "请输入手机号码");
            } else if (!RegexUtil.isMobileNO(account) && !RegexUtil.isEmail(account)) {
                ToastUtils.show(LoginActivity.this, "请输入正确的手机号码");
            } else if (TextUtils.isEmpty(code)) {
                ToastUtils.show(LoginActivity.this, "请输入密码");
            } else {
                try {
                    jsonObject.put("cmd", "9");
                    jsonObject.put("account", account);
                    jsonObject.put("password", code);
                    params.put("sendmsg", jsonObject.toString());
                    accountLogin(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void getCode(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    ToastUtils.show(LoginActivity.this, baseRespMsg.getRetmsg());
                    mTaskTimer = new Timer();
                    mTaskTimer.scheduleAtFixedRate(new ConfirmButtonTimerTask(), 0,
                            1000);
                } else {
                    ToastUtils.show(LoginActivity.this, baseRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(LoginActivity.this, "验证码发送失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(LoginActivity.this, "验证码发送失败，请稍后重试");

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
//                    new DownloadImageTask().execute();
                    //登录用户的id，只能输入数字、英文字母和“@”、“.”、“_”三种字符。长度小于40,并且不能重复,相同的userid会造成会话同步,切记不可重复 【必填】
                    //username: 登录用户名，长度小于32,显示于PC客服端，如未填写，系统随机会随机生成一个用户名,，如:客人9527
                    //userlevel: 登录用户的等级，普通用户“0”，VIP用户传“1”。默认写 0 【必填】
                    Ntalker.getBaseInstance().login(user.getUserID() + "", user.getNick(), 0);
                    setResult(RESULT_OK);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
                    if (imm.isActive()) {//如果开启
                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
                    }
                    finish();
                } else {
                    ToastUtils.show(LoginActivity.this, loginResp.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(LoginActivity.this, getResources().getString(R.string.login_err));
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(LoginActivity.this, getResources().getString(R.string.login_err));

            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Drawable> {
        protected Drawable doInBackground(String... urls) {
            Drawable drawable = loadImageFromNetwork(Contants.IMAGE_HEADURL + BaseApplication.getInstance().getUser().getMainImage());
            return drawable;
        }

        protected void onPostExecute(Drawable result) {
            Contants.headDrawable = result;

        }


    }


    private Drawable loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(
                    new URL(imageUrl).openStream(), "image.jpg");
        } catch (IOException e) {
            Log.d("test", e.getMessage());
        }
        if (drawable == null) {
            Log.d("test", "null drawable");
        } else {
            Log.d("test", "not null drawable");
        }

        return drawable;
    }


    @OnClick(R.id.lin_wechatlogin)
    public void weChatLogin() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        wx_api.sendReq(req);

    }

    @OnClick(R.id.lin_qqlogin)
    public void qqLogin() {

//如果session无效，就开始登录
        if (!mTencent.isSessionValid()) {
            //开始qq授权登录
            mTencent.login(LoginActivity.this, scope, loginListener);
        }


    }

    @OnClick(R.id.lin_sinalogin)
    public void sinaLogin() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTencent != null) {
            //注销登录
            mTencent.logout(LoginActivity.this);
        }
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (loginCode == -2) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("from", loginCode);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
