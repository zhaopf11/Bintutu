package com.zhurui.bunnymall.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.home.adapter.HomeMessageAdapter;
import com.zhurui.bunnymall.home.bean.MessageBean;
import com.zhurui.bunnymall.home.bean.MessageListBean;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.HotItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaoneng.coreapi.ChatParamsBody;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.uiapi.XNSDKListener;
import cn.xiaoneng.utils.CoreData;
import okhttp3.Response;

public class MessageListActivity extends BaseActivity implements XNSDKListener {

    @Bind(R.id.imgbtn_back)
    ImageButton imgbtn_back;
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.lin_logistical)
    LinearLayout lin_logistical;
    @Bind(R.id.text_logisticalmessage)
    TextView text_logisticalmessage;
    @Bind(R.id.text_logisticaltime)
    TextView text_logisticaltime;
    @Bind(R.id.lin_notice)
    LinearLayout lin_notice;
    @Bind(R.id.text_noticetime)
    TextView text_noticetime;
    @Bind(R.id.text_noticemessage)
    TextView text_noticemessage;
    @Bind(R.id.recycler_message)
    RecyclerView recycler_message;
    private HomeMessageAdapter homeMessageAdapter;
    private List<MessageBean> mList;
    List<Map<String, Object>> settinginfolist = new ArrayList<Map<String,Object>>();
    private boolean isin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        ButterKnife.bind(this);
        initView();;
        initData();
//        getData();
    }

    private void initView(){
        text_title.setText(R.string.message);
        text_right.setVisibility(View.GONE);

    }

    private void initData(){
        Ntalker.getInstance().setSDKListener(this);// 小能监听接口
        String headUrl = Contants.IMAGE_HEADURL + BaseApplication.getInstance().getUser().getMainImage();
        if(Ntalker.getExtendInstance().conversation().getList()!= null){
            settinginfolist.addAll(Ntalker.getExtendInstance().conversation().getList());
        }
        homeMessageAdapter = new HomeMessageAdapter(BaseApplication.getInstance(),settinginfolist);
        homeMessageAdapter.setOnItemClickLitener(new HomeMessageAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                ChatParamsBody chatparams = new ChatParamsBody();
                chatparams.startPageTitle = "1111111女装/女士精品 - 服饰 - 搜索店铺 - ECMall演示站";
                chatparams.startPageUrl = "http://img.meicicdn.com/p/51/050010/h-050010-1.jpg";
                // erp参数
                chatparams.erpParam = "";

                // 此参数不传就默认在sdk外部打开，即在onClickUrlorEmailorNumber方法中打开
                chatparams.clickurltoshow_type = CoreData.CLICK_TO_SDK_EXPLORER;

                // 商品展示（专有参数）
                chatparams.itemparams.appgoodsinfo_type = CoreData.SHOW_GOODS_BY_WIDGET;
                chatparams.itemparams.clientgoodsinfo_type = CoreData.SHOW_GOODS_BY_WIDGET;
                chatparams.itemparams.clicktoshow_type = CoreData.CLICK_TO_SDK_EXPLORER;
                chatparams.itemparams.itemparam = "";
                chatparams.headurl = headUrl;  //必须以http:开头 访客头像设置
                //使用id方式，（SHOW_GOODS_BY_ID）
                chatparams.itemparams.goods_id = "ntalker_test";//ntalker_test
                int startChat = Ntalker.getBaseInstance().startChat(BaseApplication.getInstance(),(String) settinginfolist.get(position).get("settingid"), null, chatparams);
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        recycler_message.setLayoutManager(linearLayoutManager);
        recycler_message.addItemDecoration(new HotItemDecoration(2));

        recycler_message.setAdapter(homeMessageAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isin) {
            refreshList();
        }
        isin = false;
    }

    @Override
    protected void onPause() {
        isin = true;
        super.onPause();
    }

    public void refreshList(){
        settinginfolist.clear();
        if(Ntalker.getExtendInstance().conversation() != null && Ntalker.getExtendInstance().conversation().getList()!= null){
            settinginfolist.addAll(Ntalker.getExtendInstance().conversation().getList());
        }
        homeMessageAdapter.notifyDataSetChanged();
    }


    @OnClick(R.id.lin_logistical)
    public  void toLogisticalList(){
        //物流信息
        Intent intent = new Intent(BaseApplication.getInstance(),NoticeListActivity.class);
        intent.putExtra("isLogistical",true);
        intent.putExtra("typeId","1");
        startActivity(intent,false);

    }

    @OnClick(R.id.lin_notice)
    public void toNoticeList(){
        startActivity(new Intent(BaseApplication.getInstance(),NoticeListActivity.class).putExtra("typeId","0"),false);
    }

    private void getData() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "53");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            params.put("sendmsg", jsonObject.toString());
            getFootListMessage(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getFootListMessage(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<MessageListBean>(this) {
            @Override
            public void onSuccess(Response response, MessageListBean mMessageListBean) {
                if(mMessageListBean != null){
                    if (mMessageListBean.getResult() > 0) {
                        mList = mMessageListBean.getList();
                        upDataToUi();
                    } else {
                        ToastUtils.show(BaseApplication.getInstance(), mMessageListBean.getRetmsg());
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

    private  void upDataToUi(){
        if(mList !=null && mList.size()>0 && mList.size() == 2) {
            if (mList.get(0) != null) {
                if ("1".equals(mList.get(0).getTypeID())) {//物流消息
                    text_logisticaltime.setText(mList.get(0).getContent() == null ? "" : mList.get(0).getAddTime());
                    text_logisticalmessage.setText(mList.get(0).getContent() == null ? "" : mList.get(0).getContent());
                } else if ("0".equals(mList.get(0).getTypeID())) {//通知消息
                    text_noticemessage.setText(mList.get(0).getContent() == null ? "" : mList.get(0).getContent());
                    text_noticetime.setText(mList.get(0).getContent() == null ? "" : mList.get(0).getAddTime());
                }
            }
            if (mList.get(1) != null) {
                if ("1".equals(mList.get(1).getTypeID())) {//物流消息
                    text_logisticaltime.setText(mList.get(1).getContent() == null ? "" : mList.get(1).getAddTime());
                    text_logisticalmessage.setText(mList.get(1).getContent() == null ? "" : mList.get(1).getContent());
                } else if ("0".equals(mList.get(1).getTypeID())) {//通知消息
                    text_noticemessage.setText(mList.get(1).getContent() == null ? "" : mList.get(1).getContent());
                    text_noticetime.setText(mList.get(1).getContent() == null ? "" : mList.get(1).getAddTime());
                }
            }
        }else if(mList !=null && mList.size()>0 && mList.size() == 1){
            if (mList.get(0) != null) {
                if ("1".equals(mList.get(0).getTypeID())) {//物流消息
                    text_logisticaltime.setText(mList.get(0).getContent() != null ? "" : mList.get(0).getAddTime());
                    text_logisticalmessage.setText(mList.get(0).getContent() != null ? "" : mList.get(0).getContent());
                } else if ("0".equals(mList.get(0).getTypeID())) {//通知消息
                    text_noticemessage.setText(mList.get(0).getContent() != null ? "" : mList.get(0).getContent());
                    text_noticetime.setText(mList.get(0).getContent() != null ? "" : mList.get(0).getAddTime());
                }
            }
        }else{
            text_logisticaltime.setText("");
            text_logisticalmessage.setText("无");
            text_noticemessage.setText("无");
            text_noticetime.setText("");
        }
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view){
        finish();
    }

    @Override
    public void onClickMatchedStr(String s, String s1) {

    }

    @Override
    public void onClickShowGoods(int i, int i1, String s, String s1, String s2, String s3, String s4, String s5) {

    }

    @Override
    public void onError(int i) {

    }

    @Override
    public void onChatMsg(boolean b, String s, String s1, String s2, long l, boolean b1, int i, String s3) {

    }


    @Override
    public void onClickUrlorEmailorNumber(int i, String s) {

    }

    @Override
    public void onUnReadMsg(String s, String s1, String s2, int i) {

    }
}
