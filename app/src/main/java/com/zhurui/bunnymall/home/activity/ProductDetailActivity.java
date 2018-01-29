package com.zhurui.bunnymall.home.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhurui.bunnymall.MainActivity;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.adapter.CarouselAdapter;
import com.zhurui.bunnymall.home.bean.CarouselBean;
import com.zhurui.bunnymall.home.bean.CommentBean;
import com.zhurui.bunnymall.home.bean.ProductDetailBean;
import com.zhurui.bunnymall.home.bean.PropertyChildBean;
import com.zhurui.bunnymall.home.bean.PropertyGroupBean;
import com.zhurui.bunnymall.home.msg.AddCartRespMsg;
import com.zhurui.bunnymall.home.msg.BaseResp;
import com.zhurui.bunnymall.home.msg.BuyTeamDetailResp;
import com.zhurui.bunnymall.home.msg.CustomizedDeatilResp;
import com.zhurui.bunnymall.mine.activity.LoginActivity;
import com.zhurui.bunnymall.mine.bean.User;
import com.zhurui.bunnymall.utils.BintutuUtils;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.RegexUtil;
import com.zhurui.bunnymall.utils.ShareUtils;
import com.zhurui.bunnymall.utils.TimeUtils;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.utils.Utils;
import com.zhurui.bunnymall.viewutils.Anticlockwise;
import com.zhurui.bunnymall.viewutils.CircleImageView;
import com.zhurui.bunnymall.viewutils.ProductInfoDialog;
import com.zhurui.bunnymall.viewutils.ProductStandardDialog;
import com.zhurui.bunnymall.viewutils.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaoneng.coreapi.ChatParamsBody;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.utils.CoreData;
import okhttp3.Response;

public class ProductDetailActivity extends BaseActivity {
    @Bind(R.id.edit_search)
    EditText edit_search;
    @Bind(R.id.imgbtn_right)
    ImageButton imgbtn_right;
    @Bind(R.id.img_product)
    RollPagerView img_product;
    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.text_unit)
    TextView text_unit;
    @Bind(R.id.text_nowprice)
    TextView text_nowprice;
    @Bind(R.id.text_passprice)
    TextView text_passprice;
    @Bind(R.id.anticlockwise)
    Anticlockwise anticlockwise;
    @Bind(R.id.text_rightstr)
    TextView text_rightstr;
    @Bind(R.id.text_introduce)
    TextView text_introduce;
    @Bind(R.id.text_logisticalfare)
    TextView text_logisticalfare;
    @Bind(R.id.text_sellcount)
    TextView text_sellcount;
    @Bind(R.id.text_address)
    TextView text_address;
    @Bind(R.id.lin_info)
    LinearLayout lin_info;
    @Bind(R.id.text_fulloff)
    TextView text_fulloff;
    @Bind(R.id.rel_property)
    RelativeLayout rel_property;
    @Bind(R.id.rel_productinfo)
    RelativeLayout rel_productinfo;
    @Bind(R.id.rel_choosematerial)
    RelativeLayout rel_choosematerial;
    @Bind(R.id.text_judgementcount)
    TextView text_judgementcount;
    @Bind(R.id.lin_judgement)
    LinearLayout lin_judgement;
    @Bind(R.id.circle_head)
    CircleImageView circle_head;
    @Bind(R.id.text_nickname)
    TextView text_nickname;
    @Bind(R.id.img_heart1)
    ImageView img_heart1;
    @Bind(R.id.img_heart2)
    ImageView img_heart2;
    @Bind(R.id.img_heart3)
    ImageView img_heart3;
    @Bind(R.id.img_heart4)
    ImageView img_heart4;
    @Bind(R.id.img_heart5)
    ImageView img_heart5;
    @Bind(R.id.text_judgement)
    TextView text_judgement;
    @Bind(R.id.text_productcolor)
    TextView text_productcolor;
    @Bind(R.id.text_productsize)
    TextView text_productsize;
    @Bind(R.id.img_store)
    NetworkImageView img_store;
    @Bind(R.id.text_storename)
    TextView text_storename;
    @Bind(R.id.img_diamond1)
    ImageView img_diamond1;
    @Bind(R.id.img_diamond2)
    ImageView img_diamond2;
    @Bind(R.id.img_diamond3)
    ImageView img_diamond3;
    @Bind(R.id.img_diamond4)
    ImageView img_diamond4;
    @Bind(R.id.img_diamond5)
    ImageView img_diamond5;
    @Bind(R.id.text_productnum)
    TextView text_productnum;
    @Bind(R.id.text_newproduct)
    TextView text_newproduct;
    @Bind(R.id.text_followcount)
    TextView text_followcount;
    @Bind(R.id.text_stock)
    TextView text_stock;
    @Bind(R.id.text_bandnum)
    TextView text_bandnum;
    @Bind(R.id.text_productno)
    TextView text_productno;
    @Bind(R.id.text_description)
    TextView text_description;
    @Bind(R.id.lin_customservice)
    LinearLayout lin_customservice;
    @Bind(R.id.lin_cart)
    LinearLayout lin_cart;
    @Bind(R.id.lin_collection)
    LinearLayout lin_collection;
    @Bind(R.id.checkbox_collection)
    CheckBox checkbox_collection;
    @Bind(R.id.text_incart)
    TextView text_incart;
    @Bind(R.id.btn_buynow)
    Button btn_buynow;
    @Bind(R.id.recycler_productimg)
    RecyclerView recycler_productimg;
    @Bind(R.id.lin_diamond)
    LinearLayout lin_diamond;
    @Bind(R.id.lin_price)
    LinearLayout lin_price;
    @Bind(R.id.rel_leftbottom)
    RelativeLayout rel_leftbottom;
    @Bind(R.id.web_detail)
    WebView web_detail;
    @Bind(R.id.text_price)
    TextView text_price;
    @Bind(R.id.text_colorsize)
    TextView text_colorsize;
    @Bind(R.id.transverse_line)
    TextView transverse_line;
    private CarouselAdapter carouselAdapter = null;
    //    private ProductDetailAdapter productDetailAdapter;
    private ShareDialog shareDialog;
    private ProductInfoDialog productInfoDialog;
    private ProductStandardDialog productStandardDialog;
    //1:定制2:满减3:抢购4:团购0：普通

    //0:普通 1：定制 2：抢购 3：团购 4：满减
    private int productType;
    private CustomizedDeatilResp customizedDeatilResp;
    private List<CarouselBean> carouselBeens = new ArrayList<>();
    private List<String> urlList = new ArrayList<>();

    private BuyTeamDetailResp teamDetailResp;
    private ProductDetailBean productDetailBean;

    private long productId;
    private ImageLoader imageLoader;

    private List<PropertyGroupBean> propertyGroupBeen;
    private Map<String, List<PropertyChildBean>> propertyChildMap;
    private int number = 1;
    private String colorId = "";
    private String sizeId = "";
    private String colorNames = "";
    private String sizeNames = "";
    private String foottypeid = "";

    private String personalPropertyInfo = "";
    private String personalPropertyName = "";
    private String personalImage = "";
    private static final int TO_CHOOSEMATERIAL = 2;
    private static final int TO_LOGIN = 3;
    private String maxBuyNumber = "";
    private boolean requestAgain = false;
    private String groupBuyID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        edit_search.setVisibility(View.GONE);
//        text_right.setCompoundDrawables(getResources().getDrawable(R.drawable.message),null,null,null);
        imgbtn_right.setImageResource(R.drawable.message);
        //设置轮播
        carouselAdapter = new CarouselAdapter();
        img_product.setPlayDelay(3000);
        //设置透明度
        img_product.setAnimationDurtion(500);
        img_product.setHintView(new ColorPointHintView(BaseApplication.getInstance(), getResources().getColor(R.color.font_orange), getResources().getColor(R.color.gray)));
        img_product.setAdapter(carouselAdapter);
        text_passprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        imageLoader = BaseApplication.getInstance().getImageLoader();
        checkbox_collection.setClickable(false);
        checkbox_collection.setEnabled(false);
        initData();
    }

    private void initData() {
        productType = getIntent().getIntExtra("productType", 0);
        productId = getIntent().getLongExtra("productId", 0);
        if(productType == 3 || productType == 2){
            transverse_line.setVisibility(View.GONE);
        }
        initRequestInfo();
    }


    private void initRequestInfo() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "32");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("productid", productId);
            params.put("sendmsg", jsonObject.toString());
            getDetail(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDetail(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BuyTeamDetailResp>(this) {
            @Override
            public void onSuccess(Response response, BuyTeamDetailResp buyTeamDetailResp) {
                if (buyTeamDetailResp.getResult() > 0) {
                    if (null != buyTeamDetailResp.getList() && buyTeamDetailResp.getList().size() > 0) {
                        teamDetailResp = buyTeamDetailResp;
                        if (null != teamDetailResp.getList().get(0).getProductDetail() && teamDetailResp.getList().get(0).getProductDetail().size() > 0) {
                            productDetailBean = teamDetailResp.getList().get(0).getProductDetail().get(0);
                        }
                        if (!requestAgain) {
                            if (0 == productType && null != teamDetailResp.getList().get(0).getProductDetail() && teamDetailResp.getList().get(0).getProductDetail().size() > 0) {
                                productType = Integer.parseInt(teamDetailResp.getList().get(0).getProductDetail().get(0).getFlag());
                            }
                            uiControl(productType);
                        }
                        requestAgain = false;
                    } else {
                        ToastUtils.show(ProductDetailActivity.this, "暂无商品详情");
                    }
                } else {
                    ToastUtils.show(ProductDetailActivity.this, buyTeamDetailResp.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(ProductDetailActivity.this, "查询失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(ProductDetailActivity.this, "查询失败，请稍后重试");

            }
        });
    }

    private void setData(int productType) {
        dataToUI();
    }

    private void dataToUI() {
        if (null != teamDetailResp && teamDetailResp.getList().get(0).getProductDetail().size() > 0) {
//            productDetailBean = teamDetailResp.getList().get(0).getProductDetail().get(0);
            getThirdInfo(productDetailBean.getProductID());
            String propertyInfo = productDetailBean.getPropertiesInfo();
            String[] propertyInfoList = propertyInfo.split(",");
            propertyGroupBeen = RegexUtil.getPropertyGroup(ProductDetailActivity.this, propertyInfoList);
            propertyChildMap = RegexUtil.getPropertyChild(ProductDetailActivity.this, propertyInfoList);
            if (1 != productType) {
                text_nowprice.setText(productDetailBean.getPrice() + "");
                text_passprice.setText("¥" + productDetailBean.getNormalPrice() + "");
                text_stock.setText(productDetailBean.getStock() + "");
            }
            if (3 == productType) {
                text_rightstr.setText("剩余名额" + productDetailBean.getStock() + "人");
                if (null != teamDetailResp.getList().get(0).getAttachInfo() && teamDetailResp.getList().get(0).getAttachInfo().size() > 0) {
                    maxBuyNumber = teamDetailResp.getList().get(0).getAttachInfo().get(0).getMaxBuyNumber();
                    groupBuyID = teamDetailResp.getList().get(0).getAttachInfo().get(0).getGroupBuyID();
                }

            }
            //抢购的倒计时
            if (2 == productType) {
                if (null != teamDetailResp.getList().get(0).getAttachInfo() && teamDetailResp.getList().get(0).getAttachInfo().size() > 0) {
                    maxBuyNumber = teamDetailResp.getList().get(0).getAttachInfo().get(0).getMaxBuyNumber();
                    groupBuyID = teamDetailResp.getList().get(0).getAttachInfo().get(0).getGroupBuyID();
                    try {
                        long currentTime = TimeUtils.getCurrentTimeMillis();
                        long endTime = TimeUtils.dateToLong(teamDetailResp.getList().get(0).getAttachInfo().get(0).getEndTime());

                        if (endTime > currentTime) {
                            anticlockwise.initTime((endTime - currentTime) / 1000);
                            anticlockwise.reStart();
                            anticlockwise.setOnTimeCompleteListener(new Anticlockwise.OnTimeCompleteListener() {
                                @Override
                                public void onTimeComplete() {

                                    btn_buynow.setEnabled(false);
                                    btn_buynow.setBackgroundResource(R.color.gray);

                                }
                            });
                        } else {
                            anticlockwise.setText("00:00");
                            btn_buynow.setEnabled(false);
                            btn_buynow.setBackgroundResource(R.color.gray);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    anticlockwise.setText("00:00");
                    btn_buynow.setEnabled(false);
                    btn_buynow.setBackgroundResource(R.color.gray);
                }

            }

            if (4 == productType && null != teamDetailResp.getList().get(0).getAttachInfo() && teamDetailResp.getList().get(0).getAttachInfo().size() > 0) {
                if (null != teamDetailResp.getList().get(0).getAttachInfo().get(0).getPromotionActive() && !"".equals(teamDetailResp.getList().get(0).getAttachInfo().get(0).getPromotionActive())) {
                    text_fulloff.setText(teamDetailResp.getList().get(0).getAttachInfo().get(0).getPromotionActive());
                } else {
                    text_fulloff.setVisibility(View.GONE);
                }
            }
            text_price.setText(productDetailBean.getPrice());
            if (2 == productType) {
                productDetailBean.setTag("限时抢购");
            }
            if (null != productDetailBean.getTag() && !"".equals(productDetailBean.getTag())) {

                String str1 = " " + productDetailBean.getTag() + "   " + productDetailBean.getName();
                int bstart1 = str1.indexOf(" " + productDetailBean.getTag() + " ");
                int bend1 = bstart1 + (" " + productDetailBean.getTag() + " ").length();
                Spannable style1 = new SpannableString(str1);
                style1.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.font_orange)), bstart1, bend1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style1.setSpan(new ForegroundColorSpan(Color.WHITE), bstart1, bend1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                text_introduce.setText(style1);
            } else {
                text_introduce.setText(productDetailBean.getName());
            }

            String freestr = productDetailBean.getDeliveryFee();
            if ("0".equals(freestr)) {
                text_logisticalfare.setText("邮费:" + productDetailBean.getDeliveryFeeName());
            } else if (!"".equals(freestr) && null != freestr && !"null".equals(freestr)) {
                text_logisticalfare.setText("邮费:" + freestr);
            }
            if (null != productDetailBean.getSellNumber() && !"".equals(productDetailBean.getSellNumber()) && !"null".equals(productDetailBean.getSellNumber())) {
                text_sellcount.setText("总销：" + productDetailBean.getSellNumber() + "笔");
            } else {
                text_sellcount.setText("总销：0笔");

            }
            String addresstr = "";
            if (RegexUtil.isNull(productDetailBean.getCityname())) {
                addresstr = productDetailBean.getCityname();
            }
            if (RegexUtil.isNull(productDetailBean.getTownname())) {
                addresstr = addresstr + productDetailBean.getTownname();
            }
            text_address.setText("地址:" + addresstr);
            //邮费和地址字段没有
            text_judgementcount.setText("评价(" + productDetailBean.getCommentTimes() + ")");
            if (null != teamDetailResp.getList().get(0).getComment() && teamDetailResp.getList().get(0).getComment().size() > 0) {
                CommentBean commentBean = teamDetailResp.getList().get(0).getComment().get(0);
                text_nickname.setText(RegexUtil.nickParse(commentBean.getShowUserName()));
                text_judgement.setText(commentBean.getCommentContent());

                if(!TextUtils.isEmpty(commentBean.getPropName())){
                    try {
                        JSONArray jsonArray = new JSONArray(commentBean.getPropName().toString());
                        if(null !=jsonArray && jsonArray.length()>0){
                            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                            String info = jsonObject.getString("info");
                            String[] infos = info.split(",");
                            text_productcolor.setText(infos[0]);
                            text_productsize.setText(infos[1]);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (null != commentBean.getCommentScore() && !"".equals(commentBean.getCommentScore())) {
                    int score = Integer.parseInt(commentBean.getCommentScore());
                    if (score > 4) {
                        img_heart1.setVisibility(View.VISIBLE);
                        img_heart2.setVisibility(View.VISIBLE);
                        img_heart3.setVisibility(View.VISIBLE);
                        img_heart4.setVisibility(View.VISIBLE);
                        img_heart5.setVisibility(View.VISIBLE);
                    } else {
                        switch (score) {
                            case 1:
                                img_heart1.setVisibility(View.VISIBLE);
                                img_heart2.setVisibility(View.GONE);
                                img_heart3.setVisibility(View.GONE);
                                img_heart4.setVisibility(View.GONE);
                                img_heart5.setVisibility(View.GONE);
                                break;
                            case 2:
                                img_heart1.setVisibility(View.VISIBLE);
                                img_heart2.setVisibility(View.VISIBLE);
                                img_heart3.setVisibility(View.GONE);
                                img_heart4.setVisibility(View.GONE);
                                img_heart5.setVisibility(View.GONE);
                                break;
                            case 3:
                                img_heart1.setVisibility(View.VISIBLE);
                                img_heart2.setVisibility(View.VISIBLE);
                                img_heart3.setVisibility(View.VISIBLE);
                                img_heart4.setVisibility(View.GONE);
                                img_heart5.setVisibility(View.GONE);
                                break;
                            case 4:
                                img_heart1.setVisibility(View.VISIBLE);
                                img_heart2.setVisibility(View.VISIBLE);
                                img_heart3.setVisibility(View.VISIBLE);
                                img_heart4.setVisibility(View.VISIBLE);
                                img_heart5.setVisibility(View.GONE);
                                break;
                        }
                    }
                } else {
                    img_heart1.setVisibility(View.GONE);
                    img_heart2.setVisibility(View.GONE);
                    img_heart3.setVisibility(View.GONE);
                    img_heart4.setVisibility(View.GONE);
                    img_heart5.setVisibility(View.GONE);

                }
                new DownloadImageTask().execute(Contants.BASE_IMGURL + commentBean.getMainImage());
            } else {
                lin_judgement.setVisibility(View.GONE);
            }
            img_store.setImageUrl(Contants.BASE_IMGURL + productDetailBean.getLogo(), imageLoader);
            text_storename.setText(productDetailBean.getSupplierName());
            text_productno.setText(productDetailBean.getProductID());
            text_description.setText(productDetailBean.getInfo());
            text_bandnum.setText(productDetailBean.getBrandID());
            if (null != productDetailBean.getSupplierLevel()) {
                setSupplierLevel(productDetailBean.getSupplierLevel());
            }
            if (null != productDetailBean.getSuppliercount() && !"".equals(productDetailBean.getSuppliercount())) {
                String[] supplierCount = productDetailBean.getSuppliercount().split(",");
                text_productnum.setText(supplierCount[0]);
                text_newproduct.setText(supplierCount[1]);
                text_followcount.setText(supplierCount[2]);
            }
            if ("1".equals(productDetailBean.getIsFav())) {
                checkbox_collection.setChecked(true);
            } else {
                checkbox_collection.setChecked(false);

            }

        }

    }


    private class DownloadImageTask extends AsyncTask<String, Void, Drawable> {
        protected Drawable doInBackground(String... urls) {
//            Drawable drawable = loadImageFromNetwork(Contants.IMAGE_HEADURL + BaseApplication.getInstance().getUser().getMainImage());
            Drawable drawable = loadImageFromNetwork((String) urls[0]);
            return drawable;
        }

        protected void onPostExecute(Drawable result) {
            if (null != result) {
                circle_head.setImageDrawable(result);
            } else {
                circle_head.setImageResource(R.drawable.head_normal);
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

    private void getThirdInfo(String productId) {
        mHttpHelper.get(Contants.THIRD_URL + productId, new SpotsCallBack<BaseResp>(this) {
            @Override
            public void onSuccess(Response response, BaseResp baseResp) {
                if (1 == baseResp.getResult()) {
                    webview.setVisibility(View.VISIBLE);
                    img_product.setVisibility(View.GONE);
                    webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                    webview.getSettings().setLoadWithOverviewMode(true);
                    webview.getSettings().setUseWideViewPort(true);
                    webview.getSettings().setJavaScriptEnabled(true);
                    webview.getSettings().setLoadsImagesAutomatically(true);
                    webview.loadUrl(Contants.THIRD_REQUEST_URL + productId);
                    if (null != productDetailBean.getProductimageList() && !"".equals(productDetailBean.getProductimageList())) {
                        String[] imgstrs = productDetailBean.getProductimageList().split(",");
                        for (String str : imgstrs) {
                            urlList.add(str);
                        }
                    }
                } else {
                    webview.setVisibility(View.GONE);
                    img_product.setVisibility(View.VISIBLE);
                    if (null != productDetailBean.getProductimageList() && !"".equals(productDetailBean.getProductimageList())) {
                        String[] imgstrs = productDetailBean.getProductimageList().split(",");
                        for (String str : imgstrs) {
                            CarouselBean carouselBean = new CarouselBean();
                            carouselBean.setBannerImage(str);
                            carouselBeens.add(carouselBean);
                            urlList.add(str);
                        }
                        if (null != carouselBeens && carouselBeens.size() > 0) {
                            carouselAdapter.carouselBeen = carouselBeens;
                            carouselAdapter.notifyDataSetChanged();
                            if (carouselBeens.size() < 2) {
                                img_product.setHintView(null);
                            }
                        } else {
                            img_product.setBackgroundResource(R.drawable.detail_normal);
                        }
                    } else {
                        img_product.setBackgroundResource(R.drawable.detail_normal);
                    }

                }
                web_detail.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                web_detail.getSettings().setLoadWithOverviewMode(true);
                web_detail.getSettings().setJavaScriptEnabled(true);
                web_detail.getSettings().setLoadsImagesAutomatically(true);
                web_detail.getSettings().setUseWideViewPort(true);
                web_detail.loadUrl(Contants.PRODUCT_DETAIL_URL + productDetailBean.getUrl());
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {

            }
        });
    }

    private void setSupplierLevel(String level) {
        if ("".equals(level)) {
            lin_diamond.setVisibility(View.INVISIBLE);
        } else {
            int supplierLevel = Integer.parseInt(level);
            switch (supplierLevel) {
                case 1:
                    img_diamond1.setVisibility(View.VISIBLE);
                    img_diamond2.setVisibility(View.GONE);
                    img_diamond3.setVisibility(View.GONE);
                    img_diamond4.setVisibility(View.GONE);
                    img_diamond5.setVisibility(View.GONE);
                    break;
                case 2:
                    img_diamond1.setVisibility(View.VISIBLE);
                    img_diamond2.setVisibility(View.VISIBLE);
                    img_diamond3.setVisibility(View.GONE);
                    img_diamond4.setVisibility(View.GONE);
                    img_diamond5.setVisibility(View.GONE);
                    break;
                case 3:
                    img_diamond1.setVisibility(View.VISIBLE);
                    img_diamond2.setVisibility(View.VISIBLE);
                    img_diamond3.setVisibility(View.VISIBLE);
                    img_diamond4.setVisibility(View.GONE);
                    img_diamond5.setVisibility(View.GONE);
                    break;
                case 4:
                    img_diamond1.setVisibility(View.VISIBLE);
                    img_diamond2.setVisibility(View.VISIBLE);
                    img_diamond3.setVisibility(View.VISIBLE);
                    img_diamond4.setVisibility(View.VISIBLE);
                    img_diamond5.setVisibility(View.GONE);
                    break;
                case 5:
                    img_diamond1.setVisibility(View.VISIBLE);
                    img_diamond2.setVisibility(View.VISIBLE);
                    img_diamond3.setVisibility(View.VISIBLE);
                    img_diamond4.setVisibility(View.VISIBLE);
                    img_diamond5.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private void uiControl(int productType) {
        switch (productType) {
            case 1:
                text_unit.setVisibility(View.GONE);
                text_nowprice.setText("议价");
                text_passprice.setVisibility(View.GONE);
                anticlockwise.setVisibility(View.GONE);
                text_rightstr.setVisibility(View.GONE);
                text_sellcount.setVisibility(View.GONE);
                lin_info.setVisibility(View.GONE);
                rel_choosematerial.setVisibility(View.VISIBLE);
                rel_leftbottom.setVisibility(View.VISIBLE);
                lin_price.setVisibility(View.GONE);
                break;

            case 2:
                text_rightstr.setText("距结束");
                anticlockwise.setVisibility(View.VISIBLE);
                String str = " 限时抢购   明星同款一字扣简约凉爽舒适的 镶钻 明星同款一字扣简约凉爽舒适的 镶钻 ";
                int bstart = str.indexOf(" 限时抢购 ");
                int bend = bstart + " 限时抢购 ".length();
                SpannableStringBuilder style = new SpannableStringBuilder(str);
                style.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.font_orange)), bstart, bend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.navigation_bar_bg)), bstart, bend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                text_introduce.setText(style);
                text_sellcount.setVisibility(View.GONE);
                text_fulloff.setVisibility(View.GONE);
                rel_choosematerial.setVisibility(View.GONE);
                rel_leftbottom.setVisibility(View.GONE);
                lin_price.setVisibility(View.VISIBLE);
                break;
            case 3:
                text_rightstr.setText("剩余2人");
                anticlockwise.setVisibility(View.GONE);
                text_sellcount.setVisibility(View.GONE);
                text_fulloff.setVisibility(View.GONE);
                rel_choosematerial.setVisibility(View.GONE);
                rel_leftbottom.setVisibility(View.GONE);
                lin_price.setVisibility(View.VISIBLE);
                break;
            case 4:
                anticlockwise.setVisibility(View.GONE);
                text_rightstr.setVisibility(View.GONE);
                text_sellcount.setVisibility(View.GONE);
                rel_choosematerial.setVisibility(View.GONE);
                rel_leftbottom.setVisibility(View.VISIBLE);
                text_fulloff.setVisibility(View.VISIBLE);
                lin_price.setVisibility(View.GONE);
                break;
            case 0:
                anticlockwise.setVisibility(View.GONE);
                text_rightstr.setVisibility(View.GONE);
                String str1 = " 现货   明星同款一字扣简约凉爽舒适的 镶钻 明星同款一字扣简约凉爽舒适的 镶钻 ";
                int bstart1 = str1.indexOf(" 现货 ");
                int bend1 = bstart1 + " 现货 ".length();
                Spannable style1 = new SpannableString(str1);
                style1.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.font_orange)), bstart1, bend1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style1.setSpan(new ForegroundColorSpan(Color.WHITE), bstart1, bend1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                text_introduce.setText(style1);
                text_fulloff.setVisibility(View.GONE);
                rel_choosematerial.setVisibility(View.GONE);
                rel_leftbottom.setVisibility(View.VISIBLE);
                lin_price.setVisibility(View.GONE);
                break;

        }
        setData(productType);
    }

    @OnClick(R.id.imgbtn_right)
    public void toMessageList() {
        startActivity(new Intent(BaseApplication.getInstance(), MessageListActivity.class), true);
    }

    @OnClick(R.id.img_share)
    public void toShare() {
        ShareUtils.openShare(this,productDetailBean.getName(),productDetailBean.getProductID(),productDetailBean.getMainImage(),productDetailBean.getInfo());
    }

    @OnClick(R.id.rel_productinfo)
    public void productInfo() {
        productInfoDialog = new ProductInfoDialog(ProductDetailActivity.this, R.style.CustomDialog, propertyChildMap, propertyGroupBeen);
        productInfoDialog.show();
    }

    @OnClick(R.id.rel_judgement)
    public void toJudgeMentList() {
        if (null == productDetailBean.getCommentTimes() || "".equals(productDetailBean.getCommentTimes()) || "0".equals(productDetailBean.getCommentTimes())) {
            ToastUtils.show(ProductDetailActivity.this, "暂无更多评价");
        } else {
            startActivity(new Intent(BaseApplication.getInstance(), JudgementsActivity.class).putExtra("productId", productDetailBean.getProductID()), false);
        }

    }

    @OnClick(R.id.rel_store)
    public void toStoreHome() {
        startActivity(new Intent(BaseApplication.getInstance(), StoreHomeActivity.class).putExtra("supplierid", productDetailBean.getSupplierID()), false);

    }

    @OnClick(R.id.btn_buynow)
    public void toMakeOrder() {
        if (Utils.isLogin()) {
            if (1 == productType) {
                if (null == personalPropertyInfo || "".equals(personalPropertyInfo) || null == foottypeid || "".equals(foottypeid)) {
                    if (null == teamDetailResp.getList().get(0).getUserfootdata() || teamDetailResp.getList().get(0).getUserfootdata().size() < 1) {
                        ToastUtils.show(ProductDetailActivity.this, "请先到我的添加足型数据");
                    } else {
                        ToastUtils.show(ProductDetailActivity.this, "请选择足型数据和定制材料");
                        Intent intent = new Intent(ProductDetailActivity.this, ChooseMaterialActivity.class);
                        intent.putExtra("isNeedBack", false);
                        intent.putExtra("toCart",false);
                        intent.putExtra("product", productDetailBean);
                        intent.putExtra("foottypeid", (Serializable) teamDetailResp.getList().get(0).getUserfootdata());
                        startActivity(intent, true);
                    }
                } else {
                    Intent intent = new Intent(BaseApplication.getInstance(), MakeOrdersActivity.class);
                    intent.putExtra("propertyInfo", personalPropertyInfo);
                    intent.putExtra("propertyName", personalPropertyName);
                    intent.putExtra("product", productDetailBean);
                    intent.putExtra("actionid", "0");
                    intent.putExtra("buyNow", true);
                    intent.putExtra("isCustomized", true);
                    intent.putExtra("foottypeid", foottypeid);
                    intent.putExtra("imageName", personalImage);
                    startActivity(intent, true);
                }
            } else {
                if (null == sizeId || "".equals(sizeId) || null == colorId || "".equals(colorId) || null == foottypeid || "".equals(foottypeid) || number < 1) {
                    if (null == teamDetailResp.getList().get(0).getUserfootdata() || teamDetailResp.getList().get(0).getUserfootdata().size() < 1) {
                        ToastUtils.show(ProductDetailActivity.this, "请先到我的添加足型数据");
                    } else {
                        productStandardDialog = new ProductStandardDialog(ProductDetailActivity.this, R.style.CustomDialog, propertyChildMap, teamDetailResp.getList().get(0).getProductDetail().get(0).getPrice(), teamDetailResp.getList().get(0).getProductDetail().get(0).getMainImage(), teamDetailResp.getList().get(0).getUserfootdata(), maxBuyNumber, true);
                        productStandardDialog.setOnClickDialog(new ProductStandardDialog.OnClickDialog() {
                            @Override
                            public void subMit(int amount, String colorID, String sizeID, String colorName, String sizeName, String userfoottypeid) {
                                number = amount;
                                colorId = colorID;
                                sizeId = sizeID;
                                colorNames = colorName;
                                sizeNames = sizeName;
                                foottypeid = userfoottypeid;
                                text_price.setText(Float.parseFloat(productDetailBean.getPrice()) * number + "");
                                if(null !=colorNames && !"".equals(colorNames) && null !=sizeNames && !"".equals(sizeNames)){
                                    text_colorsize.setText("已选择 颜色:"+colorNames+",尺码:"+sizeNames);
                                }else {
                                    text_colorsize.setText("选择尺码颜色分类");
                                }
                                Intent intent = new Intent(BaseApplication.getInstance(), MakeOrdersActivity.class);
                                intent.putExtra("product", productDetailBean);
                                intent.putExtra("number", number);
                                intent.putExtra("colorId", colorId);
                                intent.putExtra("sizeId", sizeId);
                                intent.putExtra("colorName", colorNames);
                                intent.putExtra("sizeName", sizeNames);
                                intent.putExtra("buyNow", true);
                                intent.putExtra("foottypeid", foottypeid);
                                intent.putExtra("typeid", productDetailBean.getFlag());
                                intent.putExtra("actionid", "0");
                                startActivity(intent, true);
                            }
                        });
                        productStandardDialog.show();

                    }
                } else {
                    Intent intent = new Intent(BaseApplication.getInstance(), MakeOrdersActivity.class);
                    intent.putExtra("product", productDetailBean);
                    intent.putExtra("number", number);
                    intent.putExtra("colorId", colorId);
                    intent.putExtra("sizeId", sizeId);
                    intent.putExtra("colorName", colorNames);
                    intent.putExtra("sizeName", sizeNames);
                    intent.putExtra("buyNow", true);
                    intent.putExtra("typeid", productDetailBean.getFlag());
                    intent.putExtra("foottypeid", foottypeid);
                    intent.putExtra("actionid", "0");
                    startActivity(intent, true);
                }
            }

        } else {
            startActivityForResult(new Intent(BaseApplication.getInstance(), LoginActivity.class), TO_LOGIN);
        }
    }

    @OnClick(R.id.rel_property)
    public void productStandard() {
        if(Utils.isLogin()){
            if (null == teamDetailResp.getList().get(0).getUserfootdata() || teamDetailResp.getList().get(0).getUserfootdata().size() < 1) {
                ToastUtils.show(ProductDetailActivity.this, "请先到我的添加足型数据");
            } else {
                productStandardDialog = new ProductStandardDialog(ProductDetailActivity.this, R.style.CustomDialog, propertyChildMap, teamDetailResp.getList().get(0).getProductDetail().get(0).getPrice(), teamDetailResp.getList().get(0).getProductDetail().get(0).getMainImage(), teamDetailResp.getList().get(0).getUserfootdata(), maxBuyNumber, false);
                productStandardDialog.setOnClickDialog(new ProductStandardDialog.OnClickDialog() {
                    @Override
                    public void subMit(int amount, String colorID, String sizeID, String colorName, String sizeName, String userfoottypeid) {
                        number = amount;
                        colorId = colorID;
                        sizeId = sizeID;
                        colorNames = colorName;
                        sizeNames = sizeName;
                        foottypeid = userfoottypeid;
                        text_price.setText(Float.parseFloat(productDetailBean.getPrice()) * number + "");
                        if(null !=colorNames && !"".equals(colorNames) && null !=sizeNames && !"".equals(sizeNames)){
                            text_colorsize.setText("已选择 颜色:"+colorNames+",尺码:"+sizeNames);
                        }else {
                            text_colorsize.setText("选择尺码颜色分类");
                        }
                    }
                });
                productStandardDialog.show();
            }
        }else{
            startActivityForResult(new Intent(BaseApplication.getInstance(), LoginActivity.class), TO_LOGIN);
        }
    }

    @OnClick(R.id.rel_choosematerial)
    public void chooseMaterial() {
        startActivityForResult(new Intent(ProductDetailActivity.this, ChooseMaterialActivity.class)
                .putExtra("isNeedBack", true).putExtra("foottypeid", (Serializable) teamDetailResp.getList().get(0).getUserfootdata())
                .putExtra("toCart",false).putExtra("product", productDetailBean), false, TO_CHOOSEMATERIAL);

    }

    @OnClick(R.id.lin_customservice)
    public void toChat() {
        if (Utils.isLogin()) {
            BintutuUtils.connectionSeller(this,productDetailBean.getProductID(),productDetailBean.getName(),productDetailBean.getPrice(),productDetailBean.getMainImage(),productDetailBean.getXnSupplierID());
        } else {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
    }

    @OnClick(R.id.lin_collection)
    public void toCollection() {
        if (Utils.isLogin()) {
            productCollection();
        } else {
            startActivityForResult(new Intent(BaseApplication.getInstance(), LoginActivity.class), TO_LOGIN);
        }
    }


    private void productCollection() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "62");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("typeid", "1");
            jsonObject.put("id", productId + "");
            params.put("sendmsg", jsonObject.toString());
            productFollow(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void productFollow(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    if (checkbox_collection.isChecked()) {
                        checkbox_collection.setChecked(false);
                        ToastUtils.show(ProductDetailActivity.this, "取消收藏");
                    } else {
                        checkbox_collection.setChecked(true);
                        ToastUtils.show(ProductDetailActivity.this, "收藏成功");
                    }
                } else {
                    ToastUtils.show(ProductDetailActivity.this, baseRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(ProductDetailActivity.this, "收藏失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(ProductDetailActivity.this, "收藏失败，请稍后重试");

            }
        });
    }


    @OnClick(R.id.lin_cart)
    public void toCart() {
        if (Utils.isLogin()) {
            Intent intent = new Intent(BaseApplication.getInstance(), MainActivity.class);
            intent.putExtra("from", 1);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent, true);
        } else {
            startActivityForResult(new Intent(BaseApplication.getInstance(), LoginActivity.class), TO_LOGIN);
        }
    }

    @OnClick(R.id.text_incart)
    public void addToCart() {
        if (Utils.isLogin()) {
            if (1 == productType) {
                if (null == personalPropertyInfo || "".equals(personalPropertyInfo) || null == foottypeid || "".equals(foottypeid)) {
                    if (null == teamDetailResp.getList().get(0).getUserfootdata() || teamDetailResp.getList().get(0).getUserfootdata().size() < 1) {
                        ToastUtils.show(ProductDetailActivity.this, "请先到我的添加足型数据");
                    } else {
                        ToastUtils.show(ProductDetailActivity.this, "请选择足型数据和定制材料");
                        startActivityForResult(new Intent(ProductDetailActivity.this, ChooseMaterialActivity.class).putExtra("isNeedBack", true).putExtra("foottypeid", (Serializable) teamDetailResp.getList().get(0).getUserfootdata()).putExtra("toCart",true).putExtra("product", productDetailBean), false, TO_CHOOSEMATERIAL);
                    }
                } else {
                    addProductToCart();
                }
            } else {
                if (null == sizeId || "".equals(sizeId) || null == colorId || "".equals(colorId) || null == foottypeid || "".equals(foottypeid) || number < 1) {
                    if (null == teamDetailResp.getList().get(0).getUserfootdata() || teamDetailResp.getList().get(0).getUserfootdata().size() < 1) {
                        ToastUtils.show(ProductDetailActivity.this, "请先到我的添加足型数据");
                    } else {
                        productStandardDialog = new ProductStandardDialog(ProductDetailActivity.this, R.style.CustomDialog, propertyChildMap, teamDetailResp.getList().get(0).getProductDetail().get(0).getPrice(), teamDetailResp.getList().get(0).getProductDetail().get(0).getMainImage(), teamDetailResp.getList().get(0).getUserfootdata(), maxBuyNumber, true);
                        productStandardDialog.setOnClickDialog(new ProductStandardDialog.OnClickDialog() {
                            @Override
                            public void subMit(int amount, String colorID, String sizeID, String colorName, String sizeName, String userfoottypeid) {
                                number = amount;
                                colorId = colorID;
                                sizeId = sizeID;
                                colorNames = colorName;
                                sizeNames = sizeName;
                                foottypeid = userfoottypeid;
                                text_price.setText(Float.parseFloat(productDetailBean.getPrice()) * number + "");
                                if(null !=colorNames && !"".equals(colorNames) && null !=sizeNames && !"".equals(sizeNames)){
                                    text_colorsize.setText("已选择 颜色:"+colorNames+",尺码:"+sizeNames);
                                }else {
                                    text_colorsize.setText("选择尺码颜色分类");
                                }
                                addProductToCart();
                            }
                        });
                        productStandardDialog.show();
                    }
                } else {
                    addProductToCart();
                }
            }
        } else {
            startActivityForResult(new Intent(BaseApplication.getInstance(), LoginActivity.class), TO_LOGIN);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TO_CHOOSEMATERIAL:
                if (null != data) {
                    personalPropertyInfo = data.getStringExtra("propertyInfo");
                    personalPropertyName = data.getStringExtra("propertyName");
                    foottypeid = data.getStringExtra("foottypeid");
                    if (null != data.getStringExtra("imageName") && !"null".equals(data.getStringExtra("imageName"))) {
                        personalImage = data.getStringExtra("imageName");
                    }
                }
                break;
            case TO_LOGIN:
                if (Utils.isLogin()) {
                    requestAgain = true;
                    initRequestInfo();
                }
                break;
            default:
                UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    /**
     * //actionid  1 添加   2.编辑   3.删除
     */
    private void addProductToCart() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "44");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("actionid", "1");
            JSONArray jsonArray = new JSONArray();
            JSONObject productObject = new JSONObject();
            productObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            productObject.put("productid", productDetailBean.getProductID() + "");
            productObject.put("name", productDetailBean.getName());
            productObject.put("suppliername", productDetailBean.getSupplierName());
            productObject.put("colorpropertiesname", colorNames);
            productObject.put("sizepropertiesname", sizeNames);
            productObject.put("deliveryfee", productDetailBean.getDeliveryFee());
            productObject.put("deliveryname", productDetailBean.getDeliveryFeeName());
            productObject.put("supplierid", productDetailBean.getSupplierID() + "");
            productObject.put("price", productDetailBean.getPrice() + "");
            productObject.put("number", number + "");
            productObject.put("colorid", colorId);
            productObject.put("sizeid", sizeId);
            productObject.put("mainimage", productDetailBean.getMainImage());
            productObject.put("typeid", productDetailBean.getFlag() + "");
            productObject.put("groupBuyID", groupBuyID);
            productObject.put("skuID", "");

            if (null != foottypeid && !"".equals(foottypeid)) {
                productObject.put("userfoottypedataid", foottypeid);
            } else {
                productObject.put("userfoottypedataid", "0");
            }
            productObject.put("custompropertiesinfo", personalPropertyInfo);
            productObject.put("custompropertiesinfoname", personalPropertyName);
            productObject.put("orderproductattachimage", personalImage);
            jsonArray.put(productObject);
            jsonObject.put("productlist", jsonArray);
            params.put("sendmsg", jsonObject.toString());
            addInCart(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void addInCart(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<AddCartRespMsg>(this) {
            @Override
            public void onSuccess(Response response, AddCartRespMsg addCartRespMsg) {
                if (addCartRespMsg.getResult() > 0) {
                    if ("1".equals(addCartRespMsg.getList().get(0).getIssuccess())) {
                        ToastUtils.show(ProductDetailActivity.this, "成功加入购物车");
                    } else {
                        ToastUtils.show(ProductDetailActivity.this, "添加失败");
                    }
                } else {
                    ToastUtils.show(ProductDetailActivity.this, addCartRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(ProductDetailActivity.this, "添加失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(ProductDetailActivity.this, "添加失败，请稍后重试");

            }
        });
    }


    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
