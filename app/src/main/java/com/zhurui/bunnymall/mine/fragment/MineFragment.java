package com.zhurui.bunnymall.mine.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhurui.bunnymall.MainActivity;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.BaseFragment;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.home.activity.MessageListActivity;
import com.zhurui.bunnymall.mine.activity.AccountSafeActivity;
import com.zhurui.bunnymall.mine.activity.EnterMallActivity;
import com.zhurui.bunnymall.mine.activity.FootTypeMessagesActivity;
import com.zhurui.bunnymall.mine.activity.LoginActivity;
import com.zhurui.bunnymall.mine.activity.ManagerAddressActivity;
import com.zhurui.bunnymall.mine.activity.ModifyPersonalInfoActivity;
import com.zhurui.bunnymall.mine.activity.MyCollectionsActivity;
import com.zhurui.bunnymall.mine.activity.MyFootPrintActivity;
import com.zhurui.bunnymall.mine.activity.MyWalletActivity;
import com.zhurui.bunnymall.mine.activity.OrdersActivity;
import com.zhurui.bunnymall.mine.activity.SettingActivity;
import com.zhurui.bunnymall.mine.activity.ShareActivity;
import com.zhurui.bunnymall.mine.bean.PersonaInfoBean;
import com.zhurui.bunnymall.mine.msg.PersonaInfoRespMsg;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.utils.Utils;
import com.zhurui.bunnymall.viewutils.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment {
    @Bind(R.id.circle_head)
    CircleImageView circleImageView;
    @Bind(R.id.text_nickname)
    TextView text_nickname;
    @Bind(R.id.lin_productcollection)
    LinearLayout lin_productcollection;
    @Bind(R.id.lin_footprint)
    LinearLayout lin_footprint;
    @Bind(R.id.text_product)
    TextView text_product;
    @Bind(R.id.text_store)
    TextView text_store;
    @Bind(R.id.text_mark)
    TextView text_mark;

    private static final int SETTING_REQUEST_CODE = 1;
    private static final int TO_LOGIN = 1;
    private List<PersonaInfoBean> infoBeens;
    boolean refresh = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {
        getPersonalInfo();
    }

    private void initView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        downLoadHeadImage();
        if (!TextUtils.isEmpty(BaseApplication.getInstance().getUser().getNick())) {
            text_nickname.setText(BaseApplication.getInstance().getUser().getNick());
        } else {
            text_nickname.setText("宾兔兔");
        }
        if(Utils.isLogin()){
            getPersonalInfo();
        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Drawable> {
        protected Drawable doInBackground(String... urls) {
            Drawable drawable = loadImageFromNetwork(Contants.IMAGE_HEADURL + BaseApplication.getInstance().getUser().getMainImage());
            return drawable;
        }

        protected void onPostExecute(Drawable result) {
            if (circleImageView != null) {
                if (null != result) {
                    Contants.headDrawable = result;
                    circleImageView.setImageDrawable(result);
                } else {
                    circleImageView.setImageResource(R.drawable.head_normal);
                }
            }
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

    @OnClick(R.id.circle_head)
    public void toModifyInfo() {
        startActivity(new Intent(BaseApplication.getInstance(), ModifyPersonalInfoActivity.class), true);
    }

    @OnClick(R.id.lin_productcollection)
    public void toProductCollection() {
        startActivity(new Intent(BaseApplication.getInstance(), MyCollectionsActivity.class).putExtra("isStore", false), false);

    }

    @OnClick(R.id.lin_storecollection)
    public void toStoreCollection() {
        startActivity(new Intent(BaseApplication.getInstance(), MyCollectionsActivity.class).putExtra("isStore", true), false);

    }

    @OnClick(R.id.lin_footprint)
    public void toFootPrint() {
        startActivity(new Intent(BaseApplication.getInstance(), MyFootPrintActivity.class), true);

    }

    @OnClick(R.id.rel_foot_data)
    public void toFootMeaase() {
        startActivity(new Intent(BaseApplication.getInstance(), FootTypeMessagesActivity.class), false);

    }


    @OnClick(R.id.text_judge)
    public void toJudge() {
        toOrder(4);
    }

    @OnClick(R.id.text_pay)
    public void toPay() {
        toOrder(1);
    }

    @OnClick(R.id.text_send)
    public void toSend() {
        toOrder(2);
    }

    @OnClick(R.id.text_get)
    public void toGet() {
        toOrder(3);
    }

    @OnClick(R.id.text_orderall)
    public void toAllOrder() {
        toOrder(0);
    }

    private void toOrder(int state) {
        startActivity(new Intent(BaseApplication.getInstance(), OrdersActivity.class).putExtra("orderstate", state), false);

    }

    @OnClick(R.id.rel_mywallet)
    public void toMyWallet() {
        startActivity(new Intent(BaseApplication.getInstance(), MyWalletActivity.class), false);

    }

    @OnClick(R.id.rel_manageraddress)
    public void toManagerAddress() {
        startActivity(new Intent(BaseApplication.getInstance(), ManagerAddressActivity.class), true);

    }

    @OnClick(R.id.rel_info_safe)
    public void toAccountSafe() {
        startActivity(new Intent(BaseApplication.getInstance(), AccountSafeActivity.class), true);
    }

    @OnClick(R.id.rel_share)
    public void toShare() {
        startActivity(new Intent(BaseApplication.getInstance(), ShareActivity.class), false);

    }

    @OnClick(R.id.rel_in_bintutu)
    public void toEnterMall() {
        startActivity(new Intent(BaseApplication.getInstance(), EnterMallActivity.class), false);

    }

    @OnClick(R.id.rel_setting)
    public void toSetting() {
        startActivity(new Intent(BaseApplication.getInstance(), SettingActivity.class), true);

    }

    @OnClick(R.id.img_message)
    public void toMessageList() {
        startActivity(new Intent(BaseApplication.getInstance(), MessageListActivity.class), false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TO_LOGIN:
                if (null == BaseApplication.getInstance().getUser() || 0 == BaseApplication.getInstance().getUser().getUserID() || "".equals(BaseApplication.getInstance().getToken())) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    if (-1 != Contants.beforePosition) {
                        mainActivity.mTabhost.setCurrentTab(Contants.beforePosition);
                    } else {
                        mainActivity.mTabhost.setCurrentTab(0);
                    }
                }else{
                    downLoadHeadImage();
                }
                break;
        }
    }

    /**
     * 获取我的界面的数据信息
     */
    public void getPersonalInfo() {
        if (!Utils.isLogin()) {
            startActivityForResult(new Intent(BaseApplication.getInstance(), LoginActivity.class), TO_LOGIN);
        } else {
            Map<String, Object> params = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("cmd", "29");
                jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
                jsonObject.put("token", BaseApplication.getInstance().getToken());
                params.put("sendmsg", jsonObject.toString());
                getInfo(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getInfo(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<PersonaInfoRespMsg>(getActivity(), "mine") {
            @Override
            public void onSuccess(Response response, PersonaInfoRespMsg personaInfoRespMsg) {
                if (personaInfoRespMsg.getResult() > 0) {
                    infoBeens = personaInfoRespMsg.getList();
                    if (infoBeens != null && infoBeens.size() > 0 && infoBeens.get(0) != null) {
                        text_product.setText(infoBeens.get(0).getC_fpcount());
                        text_store.setText(infoBeens.get(0).getC_shopcount());
                        text_mark.setText(infoBeens.get(0).getC_bscount());
                    }
                    downLoadHeadImage();

                } else {
                    ToastUtils.show(BaseApplication.getInstance(), personaInfoRespMsg.getRetmsg());
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

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
            }
        });
    }

    /**
     * 下载头像
     */
    public void downLoadHeadImage(){
        String uri = Contants.IMAGE_HEADURL + BaseApplication.getInstance().getUser().getMainImage();
        Glide.with(this).load(uri)
                .placeholder(R.drawable.head_normal)
                .error(R.drawable.head_normal)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//是将图片原尺寸缓存到本地。
                .into(circleImageView);
    }
}


