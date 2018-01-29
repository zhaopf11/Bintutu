package com.zhurui.bunnymall.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.home.adapter.BuyLimitAdapter;
import com.zhurui.bunnymall.home.adapter.CarouselAdapter;
import com.zhurui.bunnymall.home.bean.CarouselBean;
import com.zhurui.bunnymall.home.bean.PreferentialBannerBean;
import com.zhurui.bunnymall.home.msg.PreferentialRespMsg;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.RegexUtil;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.HotItemDecoration;
import com.zhurui.bunnymall.viewutils.MyHorizontalScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class BuyLimitActivity extends BaseActivity{

    @Bind(R.id.imgbtn_back)
    ImageButton imgbtn_back;
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.img_banner)
    RollPagerView img_banner;
    @Bind(R.id.list_product)
    RecyclerView list_product;
    private BuyLimitAdapter buyLimitAdapter;
    List<String> strings = new ArrayList<String>();
    List<String> strings2 = new ArrayList<String>();
    List<String> querydata = new ArrayList<>();

    @Bind(R.id.horizontal)
    MyHorizontalScrollView horizontal;
    //2、即将开始3、正在进行4、已经结束
    private int grouppurstatus =-1;

    private CarouselAdapter carouselAdapter = null;
    private List<CarouselBean> carouselBeens = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_limit);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView(){
        text_title.setText(R.string.limit_shop);
        text_right.setVisibility(View.GONE);
        //设置轮播
        carouselAdapter = new CarouselAdapter();
        img_banner.setPlayDelay(3000);
        //设置透明度
        img_banner.setAnimationDurtion(500);
        img_banner.setHintView(new ColorPointHintView(BaseApplication.getInstance(), getResources().getColor(R.color.font_orange), getResources().getColor(R.color.gray)));
        img_banner.setAdapter(carouselAdapter);


        buyLimitAdapter = new BuyLimitAdapter(BaseApplication.getInstance(),false);
        buyLimitAdapter.setOnItemClickLitener(new BuyLimitAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(BaseApplication.getInstance(),ProductNewDetailActivity.class).putExtra("productType",2).putExtra("productId",Long.parseLong(buyLimitAdapter.preferentialBeanList.get(position).getProductID())),false);
            }

            @Override
            public void onItemClickPosition(int position) {
                startActivity(new Intent(BaseApplication.getInstance(),ProductNewDetailActivity.class).putExtra("productType",2).putExtra("productId",Long.parseLong(buyLimitAdapter.preferentialBeanList.get(position).getProductID())),false);

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        list_product.setLayoutManager(linearLayoutManager);
        list_product.addItemDecoration(new HotItemDecoration(2));

        list_product.setAdapter(buyLimitAdapter);
    }

    private void initData(){
        grouppurstatus = 3;
        strings = RegexUtil.getDateList(-3,4);
        querydata = RegexUtil.getQueryDateList(-3,4);
        strings2.add("已结束");
        strings2.add("已结束");
        strings2.add("已结束");
        strings2.add("抢购中");
        strings2.add("即将开抢");
        strings2.add("即将开抢");
        strings2.add("即将开抢");
        horizontal.setData(strings,strings2);
        horizontal.setRadioButtonClick(new MyHorizontalScrollView.RadioButtonClick() {
            @Override
            public void radioButtonClick(int position) {
                String timequery = querydata.get(position);
                if(position<3){
                    grouppurstatus = 4;
                }else if(3== position){
                    grouppurstatus = 3;
                }else {
                    grouppurstatus = 2;
                }

                getData(grouppurstatus,timequery);

            }
        });
//        getData(grouppurstatus, TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_DATE));
        getData(grouppurstatus,querydata.get(3));

    }

    private void getData(int grouppurstatus,String timequery){
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "25");
            jsonObject.put("grouppurstatus", grouppurstatus);
            jsonObject.put("timequery",timequery);
            jsonObject.put("pageno", "1");
            jsonObject.put("pagesize","5");
            jsonObject.put("groupbuytypeid","1");
            params.put("sendmsg", jsonObject.toString());
            getLimitData(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    private void getLimitData(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<PreferentialRespMsg>(this) {
            @Override
            public void onSuccess(Response response, PreferentialRespMsg preferentialRespMsg) {
                if (preferentialRespMsg.getResult() > 0) {
                    if (null != preferentialRespMsg.getList().get(0).getBannerList() && preferentialRespMsg.getList().get(0).getBannerList().size() > 0) {
                        carouselBeens.clear();
                        for (PreferentialBannerBean bannerInfo : preferentialRespMsg.getList().get(0).getBannerList()) {
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
                    buyLimitAdapter.state=grouppurstatus;
                    buyLimitAdapter.preferentialBeanList=preferentialRespMsg.getList().get(0).getGroupPurchaseList();
                    buyLimitAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.show(BuyLimitActivity.this, preferentialRespMsg.getRetmsg());
                }
            }
            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(BuyLimitActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(BuyLimitActivity.this, "请求失败，请稍后重试");

            }
        });
    }


    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view){
        finish();
    }
}
