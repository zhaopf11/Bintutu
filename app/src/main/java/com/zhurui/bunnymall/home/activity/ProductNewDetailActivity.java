package com.zhurui.bunnymall.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.zhurui.bunnymall.mine.activity.AddFootMessageActivity;
import com.zhurui.bunnymall.mine.activity.LoginActivity;
import com.zhurui.bunnymall.mine.activity.OrderDetailActivity;
import com.zhurui.bunnymall.utils.BintutuUtils;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.RegexUtil;
import com.zhurui.bunnymall.utils.ShareUtils;
import com.zhurui.bunnymall.utils.TimeUtils;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.utils.Utils;
import com.zhurui.bunnymall.viewutils.Anticlockwise;
import com.zhurui.bunnymall.viewutils.CircleImageView;
import com.zhurui.bunnymall.viewutils.DialogUtil;
import com.zhurui.bunnymall.viewutils.ProductAttrbuteDetailDialog;
import com.zhurui.bunnymall.viewutils.ProductAttrbuteDetailDialog$$ViewBinder;
import com.zhurui.bunnymall.viewutils.ProductAttributeDialog;
import com.zhurui.bunnymall.viewutils.ProductInfoDialog;
import com.zhurui.bunnymall.viewutils.ProductStandardDialog;
import com.zhurui.bunnymall.viewutils.SelectPicPopupWindow;
import com.zhurui.bunnymall.viewutils.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.uiapi.OnMsgUrlClickListener;
import cn.xiaoneng.uiapi.XNClickGoodsListener;
import okhttp3.Response;

public class ProductNewDetailActivity extends BaseActivity implements View.OnTouchListener, XNClickGoodsListener {
    @Bind(R.id.img_rollpagerview)
    RollPagerView img_rollpagerview;
    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.text_unit)
    TextView text_unit;
    @Bind(R.id.text_nowprice)
    TextView text_nowprice;
    @Bind(R.id.text_passprice)
    TextView text_passprice;

    @Bind(R.id.lin_customservice)
    LinearLayout lin_customservice;
    @Bind(R.id.lin_cart)
    LinearLayout lin_cart;
    @Bind(R.id.btn_buynow)
    Button btn_buynow;

    @Bind(R.id.web_detail)
    WebView web_detail;
    @Bind(R.id.rel_suspend)
    FrameLayout rel_suspend;
//    @Bind(R.id.retly_suspend)
//    RelativeLayout retly_suspend;
    //评价
    @Bind(R.id.rel_check_all)
    RelativeLayout rel_check_all;
    @Bind(R.id.lin_judgement)
    LinearLayout lin_judgement;
    @Bind(R.id.ll_size_color)
    LinearLayout ll_size_color;
    @Bind(R.id.lin_judge_info)
    LinearLayout lin_judge_info;
    @Bind(R.id.circle_head)
    CircleImageView circle_head;
    @Bind(R.id.text_nickname)
    TextView text_nickname;
    @Bind(R.id.text_judgement)
    TextView text_judgement;
    @Bind(R.id.text_productcolor)
    TextView text_productcolor;
    @Bind(R.id.text_productsize)
    TextView text_productsize;
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

    @Bind(R.id.imgbtn_menu)
    ImageView imgbtn_menu;
    @Bind(R.id.imgbtn_judgement)
    ImageView imgbtn_judgement;
    @Bind(R.id.imgbtn_collection)
    CheckBox imgbtn_collection;
    @Bind(R.id.lin_image)
    LinearLayout lin_image;

    private CarouselAdapter carouselAdapter = null;
    //1:定制2:满减3:抢购4:团购0：普通
    private int productType;// 0 普通商品  1 普通定制  2  自设计定制
    private List<CarouselBean> carouselBeens = new ArrayList<>();
    private List<String> urlList = new ArrayList<>();

    private BuyTeamDetailResp teamDetailResp;
    private ProductDetailBean productDetailBean;
    private List<CommentBean> productCommentList;

    private long productId;
    private ImageLoader imageLoader;

    private int number = 1;
    private String foottypeid = "";
    private String pricetotal = "";

    private String personalPropertyInfo = "";
    private String personalPropertyName = "";
    private String personalImage = "";
    private static final int TO_CHOOSEMATERIAL = 2;
    private static final int TO_LOGIN = 3;
    private String maxBuyNumber = "";
    private boolean requestAgain = false;
    ProductAttributeDialog productAttributeDialog = new ProductAttributeDialog();
    private SelectPicPopupWindow popupWindow;
    private ProductAttrbuteDetailDialog productAttrbuteDetailDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_new_detail);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        //设置轮播
        carouselAdapter = new CarouselAdapter();
        img_rollpagerview.setPlayDelay(3000);
        //设置透明度
        img_rollpagerview.setAnimationDurtion(500);
        img_rollpagerview.setHintView(new ColorPointHintView(BaseApplication.getInstance(), getResources().getColor(R.color.color_d2ba91), getResources().getColor(R.color.gray)));
        img_rollpagerview.setAdapter(carouselAdapter);
        text_passprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        imageLoader = BaseApplication.getInstance().getImageLoader();
        rel_suspend.setOnTouchListener(this);
        //将布局往上提150的高度
//        FrameLayout.LayoutParams lp1 = (FrameLayout.LayoutParams) lin_image.getLayoutParams();
//        lp1.topMargin = -150;
//        lin_image.requestLayout();
        initData();
        if (ProductAttrbuteDetailDialog.checkData != null) {
            ProductAttrbuteDetailDialog.checkData.clear();
            ;
        }
    }

    private void initData() {
        productType = getIntent().getIntExtra("productType", 0);
        productId = getIntent().getLongExtra("productId", 0);
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
                            productCommentList = teamDetailResp.getList().get(0).getComment();
                        }
                        if (!requestAgain) {
                            if (null != teamDetailResp.getList().get(0).getProductDetail() && teamDetailResp.getList().get(0).getProductDetail().size() > 0) {
                                productType = Integer.parseInt(teamDetailResp.getList().get(0).getProductDetail().get(0).getCustomFlag());
                            }
                            uiControl(productType);
                        }
                        requestAgain = false;
                    } else {
                        ToastUtils.show(ProductNewDetailActivity.this, "暂无商品详情");
                    }
                } else {
                    ToastUtils.show(ProductNewDetailActivity.this, buyTeamDetailResp.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(ProductNewDetailActivity.this, "查询失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(ProductNewDetailActivity.this, "查询失败，请稍后重试");

            }
        });
    }

    private void setData(int productType) {
        dataToUI();
    }

    private void dataToUI() {
        if (null != teamDetailResp && teamDetailResp.getList().get(0).getProductDetail().size() > 0) {
            getThirdInfo(productDetailBean.getProductID());
            String propertyInfo = productDetailBean.getPropertiesInfo();
            String[] propertyInfoList = propertyInfo.split(",");
            String normalPrice = productDetailBean.getNormalPrice();
            String price = productDetailBean.getPrice();
            if(!TextUtils.isEmpty(normalPrice) && Float.parseFloat(normalPrice) > Float.parseFloat(price)){
                text_passprice.setVisibility(View.VISIBLE);
                text_passprice.setText(normalPrice);
            }
            if (2 != productType) {
                text_nowprice.setText(productDetailBean.getPrice() + "");

            }
            if("1".equals(productDetailBean.getIsFav())){
                imgbtn_collection.setChecked(true);
            }else{
                imgbtn_collection.setChecked(false);
            }
            //如果没有评价，隐藏布局,有的话展示
            if (productCommentList != null && productCommentList.size() > 0) {
                CommentBean commentBean = productCommentList.get(0);
                showJudgeInfo(commentBean);
                lin_judgement.setVisibility(View.VISIBLE);
            } else {
                lin_judgement.setVisibility(View.GONE);
            }

            if (null != productDetailBean.getTag() && !"".equals(productDetailBean.getTag())) {
                String str1 = " " + productDetailBean.getTag() + "   " + productDetailBean.getName();
                int bstart1 = str1.indexOf(" " + productDetailBean.getTag() + " ");
                int bend1 = bstart1 + (" " + productDetailBean.getTag() + " ").length();
                Spannable style1 = new SpannableString(str1);
                style1.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.color_d2ba91)), bstart1, bend1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style1.setSpan(new ForegroundColorSpan(Color.WHITE), bstart1, bend1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private void getThirdInfo(String productId) {
        mHttpHelper.get(Contants.THIRD_URL + productId, new SpotsCallBack<BaseResp>(this) {
            @Override
            public void onSuccess(Response response, BaseResp baseResp) {
                if (1 == baseResp.getResult()) {
                    webview.setVisibility(View.VISIBLE);
                    img_rollpagerview.setVisibility(View.GONE);
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
                    img_rollpagerview.setVisibility(View.VISIBLE);
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
                                img_rollpagerview.setHintView(null);
                            }
                        } else {
                            img_rollpagerview.setBackgroundResource(R.drawable.detail_normal);
                        }
                    } else {
                        img_rollpagerview.setBackgroundResource(R.drawable.detail_normal);
                    }

                }
                web_detail.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                web_detail.getSettings().setLoadWithOverviewMode(true);
                web_detail.getSettings().setJavaScriptEnabled(true);
                web_detail.getSettings().setLoadsImagesAutomatically(true);
                web_detail.getSettings().setUseWideViewPort(true);
                web_detail.getSettings().setBuiltInZoomControls(true);
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

    private void uiControl(int productType) {
        switch (productType) {
            case 2:
                text_unit.setVisibility(View.GONE);
                text_nowprice.setText("议价");
                text_passprice.setVisibility(View.GONE);
                break;
        }
        setData(productType);
    }

    /**
     * 开始定制
     */
    @OnClick(R.id.btn_buynow)
    public void toMakeOrder() {
        if (Utils.isLogin()) {
            if (teamDetailResp != null && teamDetailResp.getList().get(0).getUserfootdata() != null && teamDetailResp.getList().get(0).getUserfootdata().size() >0) {
                int stock = Integer.parseInt(productDetailBean.getStock());
                if ("0".equals(productDetailBean.getStock())) {
                    ToastUtils.show(BaseApplication.getInstance(), "宝贝已被抢光，抱歉哦");
                } else if (stock != 0 && number > stock) {
                    ToastUtils.show(BaseApplication.getInstance(), "宝贝库存不足，少买一些");
                } else {
                    if(!TextUtils.isEmpty(personalPropertyInfo) && !TextUtils.isEmpty(personalPropertyName)){
                        makeOrder();
                    }else{
                        productAttrbuteDetailDialog = new ProductAttrbuteDetailDialog(ProductNewDetailActivity.this, R.style.CustomDialog, teamDetailResp.getList().get(0).getUserfootdata(), productDetailBean, maxBuyNumber, true,"0");
                        productAttrbuteDetailDialog.setOnClickDialog(new ProductAttrbuteDetailDialog.OnClickDialog() {
                            @Override
                            public void subMit(int amount, String infoid, String infoName, String userfoottypeid,String price,String image) {
                                number = amount;
                                personalPropertyInfo = infoid;
                                personalPropertyName = infoName;
                                foottypeid = userfoottypeid;
                                pricetotal = price;
                                personalImage = image;
                                makeOrder();
                                productAttrbuteDetailDialog.dismiss();
                            }
                        });

                        productAttrbuteDetailDialog.show();
                    }
                }
            }else{
                jumpToFootData();
            }
        } else {
            startActivityForResult(new Intent(BaseApplication.getInstance(), LoginActivity.class), TO_LOGIN);
        }
    }

    //跳转到添加足型数据界面
    private void jumpToFootData(){
        DialogUtil dialogUtil = new DialogUtil();
        dialogUtil.infoDialog(this, "请添加您的足型数据", true, true);
        dialogUtil.setOnClick(new DialogUtil.OnClick() {
            @Override
            public void leftClick() {
                dialogUtil.dialog.dismiss();
            }

            @Override
            public void rightClick() {
                startActivityForResult(new Intent(BaseApplication.getInstance(), AddFootMessageActivity.class), TO_LOGIN);
                dialogUtil.dialog.dismiss();
            }
        });
    }

    private void makeOrder() {
        Intent intent = new Intent(BaseApplication.getInstance(), MakeOrdersActivity.class);
        intent.putExtra("product", productDetailBean);
        intent.putExtra("number", number);
        intent.putExtra("propertyInfo", personalPropertyInfo);
        intent.putExtra("propertyName", personalPropertyName);
        intent.putExtra("buyNow", true);
        intent.putExtra("pricetotal", pricetotal);
        intent.putExtra("foottypeid", foottypeid);
        intent.putExtra("actionid", "0");
        intent.putExtra("imageName", personalImage);
        int stock = Integer.parseInt(productDetailBean.getStock());
        if ("0".equals(productDetailBean.getStock())) {
            ToastUtils.show(BaseApplication.getInstance(), "宝贝已被抢光，抱歉哦");
        } else if (stock != 0 && number > stock) {
            ToastUtils.show(BaseApplication.getInstance(), "宝贝库存不足，少买一些");
        } else {
            startActivity(intent, true);
        }
    }

    @OnClick(R.id.lin_customservice)
    public void toChat() {
        if (Utils.isLogin()) {
            if(productDetailBean != null){
                BintutuUtils.connectionSeller(this, productDetailBean.getProductID(), productDetailBean.getName(), productDetailBean.getPrice(), productDetailBean.getMainImage(), productDetailBean.getXnSupplierID());
                //聊天界面头部商品的点击事件
                Ntalker.getExtendInstance().message().onClickShowGoods(this);
            }
        } else {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
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
                    if (!imgbtn_collection.isChecked()) {
                        imgbtn_collection.setChecked(false);
                        productDetailBean.setIsFav("0");
                        ToastUtils.show(ProductNewDetailActivity.this, "取消收藏");
                    } else {
                        imgbtn_collection.setChecked(true);
                        productDetailBean.setIsFav("1");
                        ToastUtils.show(ProductNewDetailActivity.this, "收藏成功");
                    }
                } else {
                    ToastUtils.show(ProductNewDetailActivity.this, baseRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(ProductNewDetailActivity.this, "收藏失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(ProductNewDetailActivity.this, "收藏失败，请稍后重试");

            }
        });
    }

    /**
     * 加入购物车
     */
    @OnClick(R.id.lin_cart)
    public void addToCart() {
        if (Utils.isLogin()) {
            if (teamDetailResp != null && teamDetailResp.getList().get(0).getUserfootdata() != null && teamDetailResp.getList().get(0).getUserfootdata().size() >0) {
                int stock = Integer.parseInt(productDetailBean.getStock());
                if ("0".equals(productDetailBean.getStock())) {
                    ToastUtils.show(BaseApplication.getInstance(), "宝贝已被抢光，抱歉哦");
                } else {
                    if(!TextUtils.isEmpty(personalPropertyInfo) && !TextUtils.isEmpty(personalPropertyName)){
                        addProductToCart();
                    }else{
                        productAttrbuteDetailDialog = new ProductAttrbuteDetailDialog(ProductNewDetailActivity.this, R.style.CustomDialog, teamDetailResp.getList().get(0).getUserfootdata(), productDetailBean, maxBuyNumber, true,"1");
                        productAttrbuteDetailDialog.setOnClickDialog(new ProductAttrbuteDetailDialog.OnClickDialog() {
                            @Override
                            public void subMit(int amount, String infoid, String infoName, String userfoottypeid,String price,String image) {
                                number = amount;
                                personalPropertyInfo = infoid;
                                personalPropertyName = infoName;
                                foottypeid = userfoottypeid;
                                pricetotal = price;
                                personalImage = image;
                                addProductToCart();
                                productAttrbuteDetailDialog.dismiss();
                            }
                        });
                        productAttrbuteDetailDialog.show();
                    }
                }
            }else{
                jumpToFootData();
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
        int stock = Integer.parseInt(productDetailBean.getStock());
        if ("0".equals(productDetailBean.getStock())) {
            ToastUtils.show(BaseApplication.getInstance(), "宝贝已被抢光，抱歉哦");
            return;
        } else if (stock != 0 && number > stock) {
            ToastUtils.show(BaseApplication.getInstance(), "宝贝库存不足，少买一些");
            return;
        }
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
            productObject.put("colorpropertiesname", "");
            productObject.put("sizepropertiesname", "");
            productObject.put("deliveryfee", productDetailBean.getDeliveryFee());
            productObject.put("deliveryname", productDetailBean.getDeliveryFeeName());
            productObject.put("supplierid", productDetailBean.getSupplierID() + "");
            productObject.put("price", pricetotal);
            productObject.put("number", number + "");
            productObject.put("colorid", "0");
            productObject.put("sizeid", "0");
            productObject.put("mainimage", productDetailBean.getMainImage());
            productObject.put("typeid", productDetailBean.getFlag() + "");
            if(TextUtils.isEmpty(productDetailBean.getGroupBuyFlag())){
                productObject.put("groupBuyID", productDetailBean.getGroupBuyFlag());
            }else{
                productObject.put("groupBuyID", "0");
            }
            productObject.put("skuID", "");
            productObject.put("customFlag", productDetailBean.getCustomFlag());

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
                        ToastUtils.show(ProductNewDetailActivity.this, "成功加入购物袋");
                    } else {
                        ToastUtils.show(ProductNewDetailActivity.this, "添加失败");
                    }
                } else {
                    ToastUtils.show(ProductNewDetailActivity.this, addCartRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(ProductNewDetailActivity.this, "添加失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(ProductNewDetailActivity.this, "添加失败，请稍后重试");

            }
        });
    }

    @OnClick(R.id.imgbtn_collection)
    public void toMessageList() {
        //收藏
        if (Utils.isLogin()) {
            productCollection();
        } else {
            imgbtn_collection.setChecked(false);
            startActivityForResult(new Intent(BaseApplication.getInstance(), LoginActivity.class), TO_LOGIN);
        }
    }

    @OnClick(R.id.imgbtn_judgement)
    public void toJudgementList() {
        //跳转商品评价列表界面
        if (null == productDetailBean.getCommentTimes() || "".equals(productDetailBean.getCommentTimes()) || "0".equals(productDetailBean.getCommentTimes())) {
            ToastUtils.show(ProductNewDetailActivity.this, "暂无更多评价");
        } else {
            startActivity(new Intent(BaseApplication.getInstance(), JudgementsActivity.class).putExtra("productBean", productDetailBean), false);
        }
    }

    @OnClick(R.id.imgbtn_menu)
    public void openMenu() {
        //导航栏菜单
        popupWindow = new SelectPicPopupWindow(ProductNewDetailActivity.this, itemsOnClick, productDetailBean.getIsFav());
        int tv_width = imgbtn_menu.getWidth();//获取对应的控件view宽度px值
        int popup_width = Utils.dip2px(this,260);//将popupWindow宽度转为px值(这里的popup宽度是写死了的)
        int width = (tv_width - popup_width) / 2;//获取x轴偏移量px
        popupWindow.showAsDropDown(imgbtn_menu,width,0);
    }

    @OnClick(R.id.rel_check_all)
    public void toJudgeMentList() {
        //查看更多评价
        startActivity(new Intent(BaseApplication.getInstance(), JudgementsActivity.class).putExtra("productBean", productDetailBean), false);
    }

    @OnClick(R.id.rel_imgbtn_back)
    public void backtopre(View view) {
        finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下
                productAttributeDialog.showDialog(this, teamDetailResp);
                break;
            case MotionEvent.ACTION_UP:
                //抬起
                productAttributeDialog.dismiss();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 展示评价信息
     *
     * @param commentBean
     */
    private void showJudgeInfo(CommentBean commentBean) {
        //加载头像
        new DownloadImageTask().execute(Contants.BASE_IMGURL + commentBean.getUmainImage());
        text_nickname.setText(RegexUtil.nickParse(commentBean.getShowUserName()));
        text_judgement.setText(commentBean.getCommentContent());
        if (!TextUtils.isEmpty(commentBean.getPropName())) {
            try {
                JSONArray jsonArray = new JSONArray(commentBean.getPropName().toString());
                if (null != jsonArray && jsonArray.length() > 0) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                    String info = jsonObject.getString("info");
                    String[] infos = info.split(",");
                    if (infos != null && infos.length == 2) {
                        text_productcolor.setText("颜色：" + infos[0]);
                        text_productsize.setText("尺码：" + infos[1]);
                    }
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
    }


    // 聊天界面头部商品点击事件跳转界面
    @Override
    public void onClickShowGoods(int appgoodsinfo_type, int clientgoodsinfo_type, String goods_id, String goods_name,
                                 String goods_price, String goods_image, String goods_url, String goods_showurl) {
        Intent intent = new Intent(this, NoticeWebViewActivity.class);
        intent.putExtra("url", goods_url);
        intent.putExtra("isproductH5", true);
        startActivity(intent);
    }

    /**
     * 加载图片展示
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Drawable> {
        protected Drawable doInBackground(String... urls) {
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
            drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), "image.jpg");
        } catch (IOException e) {
            Log.d("test", e.getMessage());
        }
        return drawable;
    }

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rlt_cart://跳转购物车
                    if (Utils.isLogin()) {
                        Intent intent = new Intent(BaseApplication.getInstance(), MainActivity.class);
                        intent.putExtra("from", 3);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent, true);
                    } else {
                        startActivityForResult(new Intent(BaseApplication.getInstance(), LoginActivity.class), TO_LOGIN);
                    }
                    popupWindow.dismiss();
                    break;
                case R.id.rlt_store://跳转商城
                    startActivity(new Intent(BaseApplication.getInstance(), StoreHomeActivity.class).putExtra("supplierid", productDetailBean.getSupplierID()), false);
                    popupWindow.dismiss();
                    break;
                case R.id.rlt_message://跳转消息界面
                    startActivity(new Intent(BaseApplication.getInstance(), MessageListActivity.class), true);
                    popupWindow.dismiss();
                    break;
                case R.id.rlt_share://分享
                    String shareUrl = Contants.PRODUCT_SHARE_URL + productDetailBean.getProductID();
                    ShareUtils.openShare(ProductNewDetailActivity.this, productDetailBean.getName(), shareUrl, productDetailBean.getMainImage(), productDetailBean.getInfo());
                    popupWindow.dismiss();
                    break;
            }
        }
    };
}
