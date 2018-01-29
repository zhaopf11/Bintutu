package com.zhurui.bunnymall.mine.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.adapter.MyFootMessageAdapter;
import com.zhurui.bunnymall.mine.bean.AddressBean;
import com.zhurui.bunnymall.mine.bean.MyFootListMessageBean;
import com.zhurui.bunnymall.mine.bean.MyFootMessageBean;
import com.zhurui.bunnymall.mine.bean.RelateUserBean;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.RegexUtil;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.DialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaoneng.utils.MD5Util;
import okhttp3.Response;

public class FootTypeMessagesActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.recycler_footmessages)
    ExpandableListView recycler_footmessages;
    private MyFootMessageAdapter footMessageAdapter;
    private boolean isEdit = false;
    @Bind(R.id.btn_add)
    Button btn_add;
    @Bind(R.id.btn_contact)
    Button btn_contact;
    @Bind(R.id.rel_bottom)
    RelativeLayout rel_bottom;
    private int checkNum;
    @Bind(R.id.checkbox_bottom)
    CheckBox checkbox_bottom;
    @Bind(R.id.btn_bottom)
    Button delete_btn_bottom;

    private TextView text_sendcode;

    private static final int TASK_TIMER_MESSAGE = 0;
    private static final int TASK_DENIED_MESSAGE = 1;
    private static final int TASK_TIMER_RESET_MESSAGE = 2;
    private Timer mTaskTimer;
    private boolean isTimerStop = false;
    private List<MyFootMessageBean> mList;
    private List<RelateUserBean> relateUserList;
    private List<String> deleteList;
    List<MyFootMessageBean> mFootList;

    Map<Integer,MyFootMessageBean> deleteMap = new HashMap<Integer, MyFootMessageBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_type_messages);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        text_title.setText(R.string.foot_message);
        text_right.setText(R.string.edit);
        text_right.setVisibility(View.GONE);
        btn_add.setVisibility(View.VISIBLE);
        btn_contact.setVisibility(View.VISIBLE);
        rel_bottom.setVisibility(View.GONE);
        checkbox_bottom.setText("全选");

    }

    private void initData() {
        deleteList = new ArrayList<>();
        footMessageAdapter = new MyFootMessageAdapter(BaseApplication.getInstance());
        footMessageAdapter.isEdit = false;
        recycler_footmessages.setAdapter(footMessageAdapter);
        int groupCount = footMessageAdapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            recycler_footmessages.expandGroup(i);
        }

        recycler_footmessages.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        footMessageAdapter.setOnItemClickLitener(new MyFootMessageAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int groupposition, int childposition) {
                Intent intent = new Intent(BaseApplication.getInstance(), FootMessageDetailActivity.class);
                MyFootMessageBean myFootMessageBean = (MyFootMessageBean)footMessageAdapter.getGroup(groupposition);
                intent.putExtra("name",myFootMessageBean == null ? "" : myFootMessageBean.getName());
                intent.putExtra("userid",myFootMessageBean == null ? "" : myFootMessageBean.getUserID());
                intent.putExtra("userFootTypeDataID",myFootMessageBean == null ? "" : myFootMessageBean.getUserFootTypeDataID());
                startActivity(intent, false);
            }

            @Override
            public void isChecked(boolean isChecked, int position) {
                if (isChecked) {
//                    checkNum++;
                    MyFootMessageBean checkedMyFootMessageBean = (MyFootMessageBean)footMessageAdapter.getGroup(position);
//                    deleteMap.put(position,checkedMyFootMessageBean);
                    deleteList.add(checkedMyFootMessageBean.getUserFootTypeDataID());
                } else {
//                    checkNum--;
//                    deleteMap.remove(position);
                    MyFootMessageBean checkedMyFootMessageBean = (MyFootMessageBean)footMessageAdapter.getGroup(position);
                    String userFootTypeDataID = checkedMyFootMessageBean.getUserFootTypeDataID();
                    if(deleteList.contains(userFootTypeDataID)){
                        deleteList.remove(userFootTypeDataID);
                    }
                }
//                if (checkNum > 0) {
//                    checkbox_bottom.setChecked(true);
//                    checkbox_bottom.setText("已选(" + checkNum + ")");
//                } else {
//                    checkbox_bottom.setChecked(false);
//                    checkbox_bottom.setText("已选");
//                }
            }

            @Override
            public void isDelete(String relateUserID) {
                dialogUtil.infoDialog(FootTypeMessagesActivity.this, "确认解除陈先生的足型数据？", true, true);
                dialogUtil.setOnClick(new DialogUtil.OnClick() {
                    @Override
                    public void leftClick() {
                        dialogUtil.dialog.dismiss();
                    }
                    @Override
                    public void rightClick() {
                        Map<String, Object> params = new HashMap<>();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("cmd", "72");
                            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                            jsonObject.put("token", BaseApplication.getInstance().getToken());
                            jsonObject.put("touid", relateUserID);
                            params.put("sendmsg", jsonObject.toString());
                            getCode(params,"3");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialogUtil.dialog.dismiss();
                    }
                });
            }
        });

        checkbox_bottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    footMessageAdapter.isSelected(true);
                    if(mList !=null && mList.size() > 0){
                        for (int i = 0;i < mList.size(); i++){
                           if(!deleteList.contains(mList.get(i).getUserFootTypeDataID())){
                               deleteList.add(mList.get(i).getUserFootTypeDataID());
                           }
                        }
                    }
                }else{
                    footMessageAdapter.isSelected(false);
                    if(deleteList !=null ){
                        deleteList.clear();
                    }
                }
            }
        });
    }

    @OnClick(R.id.btn_add)
    public void addFoot() {
        startActivity(new Intent(BaseApplication.getInstance(),AddFootMessageActivity.class),false);

    }

    @OnClick(R.id.btn_bottom)
    public void deleteData() {
        DialogUtil dialogUtil = new DialogUtil();
        dialogUtil.infoDialog(FootTypeMessagesActivity.this,"确定删除",true,true);
        dialogUtil.setOnClick(new DialogUtil.OnClick() {
            @Override
            public void leftClick() {
                dialogUtil.dialog.dismiss();
            }

            @Override
            public void rightClick() {
                Map<String, Object> params = new HashMap<>();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("cmd", "41");
                    jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                    jsonObject.put("token", BaseApplication.getInstance().getToken());
                    jsonObject.put("typeid","3");
                    jsonObject.put("userfoottypedataid", listToString(deleteList));
                    jsonObject.put("sex",1);
                    jsonObject.put("stature","");
                    jsonObject.put("weight","");
                    jsonObject.put("age",0);
                    jsonObject.put("name","");
                    jsonObject.put("shoesize","");
                    jsonObject.put("measurecode","");
                    jsonObject.put("birthday","");
                    jsonObject.put("footvarusvalgusid","0");
                    jsonObject.put("footarchid","0");
                    jsonObject.put("footDesc","0");
                    params.put("sendmsg", jsonObject.toString());
                    deleteFootMessage(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialogUtil.dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.btn_contact)
    public void contactFoot() {
        if(mFootList == null || mFootList.size() == 0){
            ToastUtils.show(FootTypeMessagesActivity.this,"您还没有添加足型数据，请添加");
        }else {
            alertDialog();
        }
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }

    @OnClick(R.id.text_right)
    public void isEdit() {
        isEdit = !isEdit;
        if (isEdit) {
            if(deleteList != null){
                deleteList.clear();
            }
            text_right.setText(R.string.complete);
            footMessageAdapter.isEdit = true;
            footMessageAdapter.notifyDataSetChanged();
            btn_add.setVisibility(View.GONE);
            btn_contact.setVisibility(View.GONE);
            rel_bottom.setVisibility(View.VISIBLE);
        } else {
            text_right.setText(R.string.edit);
            footMessageAdapter.isEdit = false;
            footMessageAdapter.notifyDataSetChanged();
            btn_add.setVisibility(View.VISIBLE);
            btn_contact.setVisibility(View.VISIBLE);
            rel_bottom.setVisibility(View.GONE);
            checkNum = 0;
            checkbox_bottom.setChecked(false);
        }

    }

    private void alertDialog() {

        Dialog dialog = new Dialog(FootTypeMessagesActivity.this, R.style.CustomDialog);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dv = inflater.inflate(R.layout.layout_contact_foot, null);
        EditText edit_phone = (EditText) dv.findViewById(R.id.edit_phone);
        EditText edit_code = (EditText) dv.findViewById(R.id.edit_code);
        text_sendcode = (TextView) dv.findViewById(R.id.text_sendcode);
        Button btn_next = (Button) dv.findViewById(R.id.btn_next);

        text_sendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonenum = edit_phone.getText().toString().trim();
                Long time = System.currentTimeMillis();
                Map<String, Object> params = new HashMap<>();
                JSONObject jsonObject = new JSONObject();
                if(TextUtils.isEmpty(phonenum)){
                    ToastUtils.show(FootTypeMessagesActivity.this, "请输入手机号码");
                }else if(!RegexUtil.isMobileNO(phonenum)){
                    ToastUtils.show(FootTypeMessagesActivity.this, "请输入正确的手机号码");
                }else {
                    try {
                        jsonObject.put("smstype", "13");
                        jsonObject.put("cmd", "8");
                        jsonObject.put("mobile", phonenum);
                        jsonObject.put("timeStamp", time);
                        jsonObject.put("sign", MD5Util.encode("813" + phonenum + Contants.sinkey + time).toLowerCase());
                        params.put("sendmsg", jsonObject.toString());
                        getCode(params,"1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonenum = edit_phone.getText().toString().trim();
                String code = edit_code.getText().toString().trim();
                Map<String, Object> params = new HashMap<>();
                JSONObject jsonObject = new JSONObject();
                if(TextUtils.isEmpty(phonenum)){
                    ToastUtils.show(FootTypeMessagesActivity.this, "请输入手机号码");
                }else if(!RegexUtil.isMobileNO(phonenum)){
                    ToastUtils.show(FootTypeMessagesActivity.this, "请输入正确的手机号码");
                }else if(TextUtils.isEmpty(code)){
                    ToastUtils.show(FootTypeMessagesActivity.this, "请输入验证码");
                }else {
                    try {
                        jsonObject.put("cmd", "71");
                        jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                        jsonObject.put("token", BaseApplication.getInstance().getToken());
                        jsonObject.put("mobile", phonenum);
                        jsonObject.put("verifycode", code);
                        params.put("sendmsg", jsonObject.toString());
                        getCode(params,"2");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.setContentView(dv);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.show();

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

    private void getData() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "59");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("pageno","1");
            jsonObject.put("pagesize","100");
            params.put("sendmsg", jsonObject.toString());
            getFootListMessage(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getFootListMessage(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<MyFootListMessageBean>(this) {
            @Override
            public void onSuccess(Response response, MyFootListMessageBean myFootListMessageBean) {
                if(myFootListMessageBean != null){
                    if (myFootListMessageBean.getResult() > 0) {
                        String userId = BaseApplication.getInstance().getUser().getUserID()+"";
                        mFootList = new ArrayList<MyFootMessageBean>();
                        mList = myFootListMessageBean.getList().get(0).getFoodataList();
                        relateUserList = myFootListMessageBean.getList().get(0).getRelateUserList();
                        //取出自己的足型数据，放在上面
                        if(mList != null && mList.size() >0){
                            for ( Iterator<MyFootMessageBean> iter = mList.iterator(); iter.hasNext();) {
                                MyFootMessageBean myFootMessageBean = iter.next();
                                if(userId.equals(myFootMessageBean.getUserID())){
                                    mFootList.add(myFootMessageBean);
                                    iter.remove();
                                }
                            }
                        }
                        Collections.reverse(mFootList);
                        mFootList.addAll(mList);
                        footMessageAdapter.mGroupList = mFootList;
                        footMessageAdapter.mchildList = relateUserList;
                        footMessageAdapter.notifyDataSetChanged();
                        for (int i = 0; i < mFootList.size(); i++) {
                            recycler_footmessages.expandGroup(0);
                        }
                    } else {
                        ToastUtils.show(BaseApplication.getInstance(), myFootListMessageBean.getRetmsg());
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

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void deleteFootMessage(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if(baseRespMsg != null){
                    if (baseRespMsg.getResult() > 0) {
                        ToastUtils.show(BaseApplication.getInstance(), "删除成功");
                        startActivity(new Intent(BaseApplication.getInstance(),FootTypeMessagesActivity.class),false);
                        initView();
                        initData();
                        deleteList.clear();
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

    private void getCode(Map<String, Object> params,String type) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    if("1".equals(type)){
                        ToastUtils.show(FootTypeMessagesActivity.this, baseRespMsg.getRetmsg());
                        mTaskTimer = new Timer();
                        mTaskTimer.scheduleAtFixedRate(new ConfirmButtonTimerTask(), 0, 1000);
                    }else if("2".equals(type)){
                        ToastUtils.show(FootTypeMessagesActivity.this, "关联成功");
                        getData();
                    }else if("3".equals(type)){
                        ToastUtils.show(FootTypeMessagesActivity.this, "解除关联成功");
                        getData();
                    }
                } else {
                    if("数据已存在".equals(baseRespMsg.getRetmsg())){
                        ToastUtils.show(FootTypeMessagesActivity.this, "对方还没有添加足型数据，请添加");
                    }else{
                        ToastUtils.show(FootTypeMessagesActivity.this, baseRespMsg.getRetmsg());
                    }
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(FootTypeMessagesActivity.this, "验证码发送失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(FootTypeMessagesActivity.this, "验证码发送失败，请稍后重试");

            }
        });
    }

    /**
     * 将list转成string
     * @param list
     * @return
     */
    public  String listToString(List<String> list){
        StringBuilder sb =new StringBuilder();
        boolean flag=false;
        if(list != null && list.size() > 0){
            for(int i=0;i < list.size();i++){
                if (flag) {
                    sb.append(",");
                }else {
                    flag=true;
                }
                sb.append(list.get(i));
            }
        }
        return sb.toString();
    }
}
