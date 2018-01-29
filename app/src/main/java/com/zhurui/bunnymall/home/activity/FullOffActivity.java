package com.zhurui.bunnymall.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.tencent.open.utils.SystemUtils;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.adapter.CarouselAdapter;
import com.zhurui.bunnymall.home.adapter.FullOffAdapter;
import com.zhurui.bunnymall.home.bean.BannerInfo;
import com.zhurui.bunnymall.home.bean.CarouselBean;
import com.zhurui.bunnymall.home.bean.ThemeBean;
import com.zhurui.bunnymall.home.msg.FullOffListRespMsg;
import com.zhurui.bunnymall.home.msg.FullOffRespMsg;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.TimeUtils;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.Anticlockwise;
import com.zhurui.bunnymall.viewutils.FullOffDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class FullOffActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtn_back;
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.img_banner)
    RollPagerView img_banner;
    @Bind(R.id.text_fulloff1)
    TextView text_fulloff1;
    @Bind(R.id.text_fulloff2)
    TextView text_fulloff2;
    @Bind(R.id.text_fulloff3)
    TextView text_fulloff3;
    @Bind(R.id.text_name)
    TextView text_name;
    @Bind(R.id.anticlockwise)
    Anticlockwise anticlockwise;
    @Bind(R.id.list_product)
    RecyclerView list_product;
    private CarouselAdapter carouselAdapter = null;
    private List<CarouselBean> carouselBeens = new ArrayList<>();

    private long startTime;
    private long endTime;

    private FullOffAdapter fullOffAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_off);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        text_title.setText(R.string.full_off);
        text_right.setVisibility(View.GONE);

        //设置轮播
        carouselAdapter = new CarouselAdapter();
        img_banner.setPlayDelay(3000);
        //设置透明度
        img_banner.setAnimationDurtion(500);
        img_banner.setHintView(new ColorPointHintView(BaseApplication.getInstance(), getResources().getColor(R.color.font_orange), getResources().getColor(R.color.gray)));
        img_banner.setAdapter(carouselAdapter);

        fullOffAdapter = new FullOffAdapter(BaseApplication.getInstance());
        fullOffAdapter.setOnItemClickLitener(new FullOffAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position, String id) {
                getList(id);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        list_product.setLayoutManager(linearLayoutManager);
        list_product.addItemDecoration(new FullOffDecoration(20));
        list_product.setHasFixedSize(true);
        list_product.setAdapter(fullOffAdapter);
    }


    private void initData() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "36");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            params.put("sendmsg", jsonObject.toString());
            getFullOff(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void getFullOff(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<FullOffRespMsg>(this) {
            @Override
            public void onSuccess(Response response, FullOffRespMsg fullOffRespMsg) {
                if (fullOffRespMsg.getResult() > 0) {
                    if (null != fullOffRespMsg.getList().get(0).getBannerList() && fullOffRespMsg.getList().get(0).getBannerList().size() > 0) {
                        for (BannerInfo bannerInfo : fullOffRespMsg.getList().get(0).getBannerList()) {
                            CarouselBean carouselBean = new CarouselBean();
                            carouselBean.setBannerImage(bannerInfo.getBannerImage());
                            carouselBean.setLink(bannerInfo.getLink());
                            carouselBeens.add(carouselBean);
                        }
                        if (null != carouselBeens && carouselBeens.size() > 0) {
                            carouselAdapter.carouselBeen = carouselBeens;
                            carouselAdapter.notifyDataSetChanged();
                        }
                    }
                    if (null != fullOffRespMsg.getList().get(0).getThemeList() && fullOffRespMsg.getList().get(0).getThemeList().size() > 0) {
                        ThemeBean themeBean = fullOffRespMsg.getList().get(0).getThemeList().get(0);
                        text_name.setText(fullOffRespMsg.getList().get(0).getThemeList().get(0).getName());
                        long time = 0;
                        try {
                            long currentTime = TimeUtils.getCurrentTimeMillis();
                            startTime = TimeUtils.dateToLong(themeBean.getStartTime());
                            endTime = TimeUtils.dateToLong(themeBean.getEndTime());
                            if (currentTime > startTime && currentTime < endTime) {
                                time = (endTime - currentTime) / 1000;
                            }
                            long day = time / ( 3600 * 24 );
                            long hour = time % ( 24 * 3600 ) / 3600;
                            long minute = time % 3600 / 60;
                            anticlockwise.setText(day + "天" + hour +"时" + minute + "分");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != fullOffRespMsg.getList().get(0).getAreaNavList() && fullOffRespMsg.getList().get(0).getAreaNavList().size() > 0) {
                        int count = fullOffRespMsg.getList().get(0).getAreaNavList().size();
                        count = (count > 3 ? 3 : count);
                        if (1 == count) {
                            text_fulloff1.setText(fullOffRespMsg.getList().get(0).getAreaNavList().get(0).getName());
                        } else if (2 == count) {
                            text_fulloff1.setText(fullOffRespMsg.getList().get(0).getAreaNavList().get(0).getName());
                            text_fulloff2.setText(fullOffRespMsg.getList().get(0).getAreaNavList().get(1).getName());
                        } else if (3 == count) {
                            text_fulloff1.setText(fullOffRespMsg.getList().get(0).getAreaNavList().get(0).getName());
                            text_fulloff2.setText(fullOffRespMsg.getList().get(0).getAreaNavList().get(1).getName());
                            text_fulloff3.setText(fullOffRespMsg.getList().get(0).getAreaNavList().get(2).getName());
                        }
                    }

                    if (null != fullOffRespMsg.getList().get(0).getShowList() && fullOffRespMsg.getList().get(0).getShowList().size() > 0) {
                        fullOffAdapter.fullOffShowBeen = fullOffRespMsg.getList().get(0).getShowList();
                        fullOffAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.show(FullOffActivity.this, fullOffRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(FullOffActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(FullOffActivity.this, "请求失败，请稍后重试");

            }
        });
    }


    private void getList(String promotionactiveid) {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "37");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("showid", promotionactiveid);
            jsonObject.put("pageno", "1");
            jsonObject.put("pagesize", "200");
            params.put("sendmsg", jsonObject.toString());
            getList(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getList(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<FullOffListRespMsg>(this) {
            @Override
            public void onSuccess(Response response, FullOffListRespMsg fullOffListRespMsg) {
                if (fullOffListRespMsg.getResult() > 0) {
                    if (null != fullOffListRespMsg.getList() && fullOffListRespMsg.getList().size() > 0) {
                        Intent intent = new Intent(BaseApplication.getInstance(), FullOffListActivity.class);
                        intent.putExtra("productlist", (Serializable) fullOffListRespMsg.getList());
                        intent.putExtra("theme", text_name.getText().toString().trim());
                        intent.putExtra("state", 2);
                        intent.putExtra("startTime", startTime);
                        intent.putExtra("endTime", endTime);
                        startActivity(intent, false);
                    } else {
                        ToastUtils.show(FullOffActivity.this, "暂无满减商品");
                    }
                } else {
                    ToastUtils.show(FullOffActivity.this, fullOffListRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(FullOffActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(FullOffActivity.this, "请求失败，请稍后重试");

            }
        });
    }


    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
