package com.zhurui.bunnymall.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.cart.bean.CartProductBean;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.adapter.MakeOrderAdapter;
import com.zhurui.bunnymall.home.bean.ProductCheckAttrBean;
import com.zhurui.bunnymall.home.bean.ProductDetailBean;
import com.zhurui.bunnymall.home.bean.ProductInfoObject;
import com.zhurui.bunnymall.home.msg.MakeOrderRespMsg;
import com.zhurui.bunnymall.home.msg.ProductInfoRespMsg;
import com.zhurui.bunnymall.mine.activity.ManagerAddressActivity;
import com.zhurui.bunnymall.mine.activity.OrdersActivity;
import com.zhurui.bunnymall.mine.bean.AddressBean;
import com.zhurui.bunnymall.mine.bean.UserFootDataDetailBean;
import com.zhurui.bunnymall.utils.BintutuUtils;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;

public class MakeOrdersActivity extends BaseActivity {


    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;

    @Bind(R.id.text_name)
    TextView text_name;
    @Bind(R.id.text_normal)
    TextView text_normal;
    @Bind(R.id.text_telnum)
    TextView text_telnum;
    @Bind(R.id.text_address)
    TextView text_address;
    @Bind(R.id.text_price)
    TextView text_price;
    @Bind(R.id.text_total)
    TextView text_total;
    @Bind(R.id.text_productcount)
    TextView text_productcount;
    @Bind(R.id.recycler_makeorder)
    RecyclerView recycler_makeorder;
    @Bind(R.id.edit_message)
    EditText edit_message;
    @Bind(R.id.text_favourable)
    TextView text_favourable;
    @Bind(R.id.rel_favourabletype)
    RelativeLayout rel_favourabletype;
    @Bind(R.id.text_sendtype)
    TextView text_sendtype;
    @Bind(R.id.rel_toAddAddress)
    RelativeLayout rel_toAddAddress;
    @Bind(R.id.text_addaddress)
    TextView text_addaddress;
    @Bind(R.id.text_invoice)
    TextView text_invoice;
    private MakeOrderAdapter makeOrderAdapter;

    private ProductDetailBean productDetailBean;
    private int number;
    private String colorId = "0";
    private String sizeId = "0";
    private String addressId = "";
    private String colorName = "";
    private String sizeName = "";
    private boolean buyNow;
    private static final int REQUEST_CODE = 1;
    private static final int FAVOURABLE_TYPE = 2;
    private static final int INVOICE = 3;
    private int preferential = 0;
    private String preferentialvalue = "0";

    private int invoiceflag = 0;
    private int invoicetypeid = 1;
    private String invoicetitle = "";
    private String invoicenum = "";
    private String actionid;

    private ProductInfoObject productInfoObject;
    private float favourablemoney;
    private String propertyInfo = "";
    private String propertyName = "";
    private boolean isCustomized;
    private String imageName = "";
    private String pricetotal = "";

    private List<CartProductBean> cartProductBeen = null;
    //是否有普通商品，只有存在普通商品，才能有优惠选择
    private boolean hasNormal = false;
    //所有商品总价
    private float total;
    //所有商品邮费
    private float totalDelievey;
    //是否所有商品全都是私人定制的，默认是true
    private boolean isAllCustomized = true;

    //是否有私人定制商品
    private boolean isHasCustomized = false;
    private String foottypeid;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_orders);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        text_title.setText(R.string.make_order);
        text_right.setVisibility(View.GONE);
    }

    private void initData() {
        makeOrderAdapter = new MakeOrderAdapter(BaseApplication.getInstance());
        makeOrderAdapter.setOnItemClickLitener(new MakeOrderAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onChat(View view, String productId, String productName, String price, String productMainImage, String xnSupplierID) {
               //联系客服
                BintutuUtils.connectionSeller(MakeOrdersActivity.this, productId, productName, price, productMainImage, xnSupplierID);
            }

            @Override
            public void setOnEdit(String desc, UserFootDataDetailBean footDataDetail) {
                Log.i("footDataId: ",footDataDetail.getUserFootTypeDataID());
                //保存修改后的足型数据描述
                if(!TextUtils.isEmpty(desc) && !desc.equals(footDataDetail.getFootDesc())){
                    reviseFootData(desc,footDataDetail);
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        recycler_makeorder.setLayoutManager(linearLayoutManager);
        recycler_makeorder.setAdapter(makeOrderAdapter);
        propertyInfo = getIntent().getStringExtra("propertyInfo");
        propertyName = getIntent().getStringExtra("propertyName");
        isCustomized = getIntent().getBooleanExtra("isCustomized", false);
        buyNow = getIntent().getBooleanExtra("buyNow", false);
        actionid = getIntent().getStringExtra("actionid");
        if (buyNow) {
            pricetotal = getIntent().getStringExtra("pricetotal");
            foottypeid = getIntent().getStringExtra("foottypeid");
            number = getIntent().getIntExtra("number", 1);
            productDetailBean = (ProductDetailBean) getIntent().getSerializableExtra("product");
            makeOrderAdapter.productDetailBean = productDetailBean;
            makeOrderAdapter.number = number;
            makeOrderAdapter.pricetotal = pricetotal;
            if ("2".equals(productDetailBean.getCustomFlag())) {
                text_price.setText("议价");
                text_total.setText("小计：" + "议价");
            } else {
                total = number * Float.parseFloat(pricetotal);
                text_price.setText("¥" + decimalFormat.format(total));
                text_total.setText("小计：" + decimalFormat.format(total));
            }
            text_productcount.setText("共" + number + "件商品");
            if ("0".equals(productDetailBean.getDeliveryFee()) || "".equals(productDetailBean.getDeliveryFee())) {
                text_sendtype.setText("快递  " + "免费");
            } else {
                text_sendtype.setText("快递  " + productDetailBean.getDeliveryFee());
            }
        } else {
            cartProductBeen = (List<CartProductBean>) getIntent().getSerializableExtra("productlist");
            for (CartProductBean cartProductBean : cartProductBeen) {
                number = number + Integer.parseInt(cartProductBean.getNumber());
                if (!"".equals(cartProductBean.getFee()) && !"0".equals(cartProductBean.getFee())) {
                    totalDelievey = totalDelievey + Float.parseFloat(cartProductBean.getFee());
                }
                if ("2".equals(cartProductBean.getCustomFlag())) {
                    isHasCustomized = true;
                } else {
                    total = total + ((Float.parseFloat(cartProductBean.getPrice()) + Float.parseFloat(cartProductBean.getShuxingPrice())) * Integer.parseInt(cartProductBean.getNumber()));
                    hasNormal = true;
                    isAllCustomized = false;
                }
            }
            text_productcount.setText("共" + number + "件商品");
            if (isAllCustomized) {
                text_price.setText("议价");
                text_total.setText("小计：" + "议价");
            } else {
                text_price.setText("¥" + decimalFormat.format(total));
                text_total.setText("小计：" + decimalFormat.format(total));
            }
            if (0 == totalDelievey) {
                text_sendtype.setText("快递  " + "免邮");
            } else {
                text_sendtype.setText("快递  " + totalDelievey);
            }
            makeOrderAdapter.cartProductBeen = cartProductBeen;
        }
        makeOrderAdapter.buyNow = buyNow;
        if (null == foottypeid || "".equals(foottypeid)) {
            foottypeid = "0";
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (null == addressId || "".equals(addressId)) {
            initProductInfo();
        }
    }

    private void initProductInfo() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "45");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("actionid", actionid + "");
            JSONArray jsonArray = new JSONArray();
            if (buyNow) {
                JSONObject productObj = new JSONObject();
                productObj.put("productid", productDetailBean.getProductID());
                productObj.put("supplierid", productDetailBean.getSupplierID());
                productObj.put("number", number + "");
                productObj.put("colorid", "0");
                productObj.put("sizeid", "0");
                productObj.put("price", pricetotal);
                productObj.put("typeid", productDetailBean.getFlag());
                productObj.put("userfoottypedataid", foottypeid);
                productObj.put("custompropertiesinfo", propertyInfo);
                productObj.put("custompropertiesinfoname", propertyName + "");
                productObj.put("cartID", 0);
                productObj.put("customFlag", productDetailBean.getCustomFlag());
                if(TextUtils.isEmpty(productDetailBean.getGroupBuyFlag())){
                    productObj.put("groupBuyID", productDetailBean.getGroupBuyFlag());
                }else{
                    productObj.put("groupBuyID", "0");
                }
                jsonArray.put(productObj);
            } else {
                for (CartProductBean cartProductBean : cartProductBeen) {
                    JSONObject productObj = new JSONObject();
                    productObj.put("productid", cartProductBean.getProductID());
                    productObj.put("supplierid", cartProductBean.getSupplierID());
                    productObj.put("number", cartProductBean.getNumber() + "");
                    productObj.put("colorid", "0");
                    productObj.put("sizeid", "0");
                    productObj.put("price", cartProductBean.getPrice());
                    productObj.put("typeid", cartProductBean.getCartItemTypeID());
                    productObj.put("customFlag", cartProductBean.getCustomFlag());
                    productObj.put("userfoottypedataid", cartProductBean.getFootTypeDataID());
                    productObj.put("cartID", cartProductBean.getCartID());
                    if(TextUtils.isEmpty(cartProductBean.getGroupBuyID())){
                        productObj.put("groupBuyID", cartProductBean.getGroupBuyID());
                    }else{
                        productObj.put("groupBuyID", "0");
                    }
                    productObj.put("custompropertiesinfo", cartProductBean.getCustomPropInfo());
                    productObj.put("custompropertiesinfoname", cartProductBean.getCustomPropInfoName());
                    jsonArray.put(productObj);
                }
            }
            jsonObject.put("productlist", jsonArray);
            params.put("sendmsg", jsonObject.toString());
            searchInfo(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void searchInfo(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<ProductInfoRespMsg>(this) {
            @Override
            public void onSuccess(Response response, ProductInfoRespMsg productInfoRespMsg) {
                if (productInfoRespMsg.getResult() > 0) {
                    if (null != productInfoRespMsg.getList() && productInfoRespMsg.getList().size() > 0) {
                        productInfoObject = productInfoRespMsg.getList().get(0);
                        if (null == productInfoObject.getPromotionactive() && null == productInfoObject.getPoint() && null == productInfoObject.getCardList()) {
                            rel_favourabletype.setVisibility(View.GONE);
                        }
                        //展示选中商品的信息以及选中的定制属性
                        if(productInfoObject != null ){
                            List<ProductCheckAttrBean> xuanzhongsxlist = productInfoObject.getXuanZhongsx();
                            if(xuanzhongsxlist !=null && xuanzhongsxlist.size() > 0){
                                makeOrderAdapter.xuanzhongsxList = xuanzhongsxlist;
                                makeOrderAdapter.notifyDataSetChanged();
                            }
                        }
                        if (null != productInfoRespMsg.getList().get(0).getAddressList() && productInfoRespMsg.getList().get(0).getAddressList().size() > 0) {
                            rel_toAddAddress.setVisibility(View.VISIBLE);
                            text_addaddress.setVisibility(View.GONE);
                            List<AddressBean> addressBeanList = productInfoRespMsg.getList().get(0).getAddressList();
                            if (1 == addressBeanList.size()) {
                                AddressBean addressBean = addressBeanList.get(0);
                                text_name.setText(addressBean.getShouHuoRen());
                                text_normal.setVisibility(View.VISIBLE);
                                text_address.setText(addressBean.getProvinceName() + addressBean.getCityName() + addressBean.getTownName() + addressBean.getAddress());
                                text_telnum.setText(addressBean.getMobile());
                                addressId = addressBean.getAddressID() + "";
                            } else {
                                for (AddressBean addressBean : addressBeanList) {
                                    if ("1".equals(addressBean.getDefaultFlag())) {
                                        rel_toAddAddress.setVisibility(View.VISIBLE);
                                        text_addaddress.setVisibility(View.GONE);
                                        text_name.setText(addressBean.getShouHuoRen());
                                        text_normal.setVisibility(View.VISIBLE);
                                        text_address.setText(addressBean.getProvinceName() + addressBean.getCityName() + addressBean.getTownName() + addressBean.getAddress());
                                        text_telnum.setText(addressBean.getMobile());
                                        addressId = addressBean.getAddressID() + "";
                                        return;
                                    } else {
                                        rel_toAddAddress.setVisibility(View.INVISIBLE);
                                        text_addaddress.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        } else {
                            rel_toAddAddress.setVisibility(View.INVISIBLE);
                            text_addaddress.setVisibility(View.VISIBLE);
                        }
                    } else {
                        ToastUtils.show(MakeOrdersActivity.this, "暂无收货地址");
                    }
                } else if (productInfoRespMsg.getResult() == -18) {
                    ToastUtils.show(MakeOrdersActivity.this, "宝贝已被抢光，抱歉哦");
                } else {
                    ToastUtils.show(MakeOrdersActivity.this, productInfoRespMsg.getRetmsg());
                }
                dismissDialog();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(MakeOrdersActivity.this, "请求失败，请稍后重试");
                dismissDialog();
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(MakeOrdersActivity.this, "请求失败，请稍后重试");
                dismissDialog();

            }

            @Override
            public void onFailure(Request request, Exception e) {
                dismissDialog();
            }
        });
    }

    @OnClick(R.id.rel_toAddress)
    public void toAddress() {
        startActivityForResult(new Intent(BaseApplication.getInstance(), ManagerAddressActivity.class).putExtra("fromOrder", true), true, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (null != data) {
                    rel_toAddAddress.setVisibility(View.VISIBLE);
                    text_addaddress.setVisibility(View.GONE);
                    AddressBean addressBean = (AddressBean) data.getSerializableExtra("address");
                    text_name.setText(addressBean.getShouHuoRen());
                    if ("1".equals(addressBean.getDefaultFlag())) {
                        text_normal.setVisibility(View.VISIBLE);
                    } else {
                        text_normal.setVisibility(View.GONE);
                    }
                    text_address.setText(addressBean.getProvinceName() + addressBean.getCityName() + addressBean.getTownName() + addressBean.getAddress());
                    text_telnum.setText(addressBean.getMobile());
                    addressId = addressBean.getAddressID() + "";

                }
                break;
            case FAVOURABLE_TYPE:
                if (null != data) {
                    preferential = data.getIntExtra("preferential", 0);
                    preferentialvalue = data.getStringExtra("value");
                    favourablemoney = data.getFloatExtra("favourablemoney", 0);
                    if (!isCustomized || !isAllCustomized) {
                        float nowTotal = total - favourablemoney;
                        if (nowTotal < 0) {
                            text_price.setText("¥" + decimalFormat.format(0));
                        } else {
                            text_price.setText("¥" + decimalFormat.format(nowTotal));
                        }

                    }

                    text_favourable.setText(data.getStringExtra("showInfo"));

                }
                break;
            case INVOICE:
                if (null != data) {
                    invoiceflag = data.getIntExtra("invoiceflag", 0);
                    invoicetypeid = data.getIntExtra("invoicetypeid", 1);
                    invoicetitle = data.getStringExtra("invoicetitle");
                    invoicenum = data.getStringExtra("invoicenum");
                    text_invoice.setText(invoicetitle);
                }
                break;
        }
    }

    private void initOrderInfo() {

        if (null == addressId || "".equals(addressId) || "0".equals(addressId)) {
            ToastUtils.show(MakeOrdersActivity.this, "请选择收货地址");
        } else {
            Map<String, Object> params = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("cmd", "33");
                jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID() + "");
                jsonObject.put("token", BaseApplication.getInstance().getToken());
                jsonObject.put("addressid", addressId);
                JSONObject preferentialObj = new JSONObject();
                preferentialObj.put("key", preferential + "");
                preferentialObj.put("value", preferentialvalue + "");
                jsonObject.put("preferentialinfo", preferentialObj);
                jsonObject.put("actionid", actionid);
                JSONArray jsonArray = new JSONArray();
                if (buyNow) {
                    JSONObject productObj = new JSONObject();
                    productObj.put("productid", productDetailBean.getProductID());
                    productObj.put("name", productDetailBean.getName());
                    productObj.put("supplierid", productDetailBean.getSupplierID());
                    productObj.put("suppliername", productDetailBean.getSupplierName());
                    productObj.put("number", number + "");
                    productObj.put("colorid", "0");
                    productObj.put("colorpropertiesname", "");
                    productObj.put("sizeid", "0");
                    productObj.put("sizepropertiesname", "");
                    productObj.put("deliveryfee", productDetailBean.getDeliveryFee());
                    productObj.put("deliveryname", productDetailBean.getDeliveryFeeName());
                    productObj.put("price",pricetotal);
                    productObj.put("typeid", productDetailBean.getFlag());
                    productObj.put("userfoottypedataid", foottypeid);
                    productObj.put("custompropertiesinfo", propertyInfo + "");
                    productObj.put("mainimage", productDetailBean.getMainImage());
                    productObj.put("orderproductattachimage", imageName + "");
                    productObj.put("custompropertiesinfoname", propertyName + "");
                    productObj.put("customFlag", productDetailBean.getCustomFlag());
                    if(TextUtils.isEmpty(productDetailBean.getGroupBuyFlag())){
                        productObj.put("groupBuyID", productDetailBean.getGroupBuyFlag());
                    }else{
                        productObj.put("groupBuyID", "0");
                    }
                    jsonArray.put(productObj);
                } else {
                    for (CartProductBean cartProduct : cartProductBeen) {
                        JSONObject productObj = new JSONObject();
                        productObj.put("productid", cartProduct.getProductID());
                        productObj.put("name", cartProduct.getProductName());
                        productObj.put("supplierid", cartProduct.getSupplierID());
                        productObj.put("suppliername", cartProduct.getSupplierName());
                        productObj.put("number", cartProduct.getNumber() + "");
                        productObj.put("colorid", "0");
                        productObj.put("sizeid", "0");
                        productObj.put("colorpropertiesname", cartProduct.getColorName());
                        productObj.put("sizepropertiesname", cartProduct.getSizeName());
                        productObj.put("deliveryfee", cartProduct.getFee());
                        productObj.put("deliveryname", "");
                        productObj.put("price", cartProduct.getPrice());
                        productObj.put("typeid", cartProduct.getCartItemTypeID());
                        productObj.put("userfoottypedataid", cartProduct.getFootTypeDataID());
                        productObj.put("custompropertiesinfo", cartProduct.getCustomPropInfo() + "");
                        productObj.put("mainimage", cartProduct.getProductMainImage());
                        productObj.put("orderproductattachimage", "");
                        productObj.put("custompropertiesinfoname", cartProduct.getCustomPropInfoName());
                        productObj.put("cartID", cartProduct.getCartID());
                        productObj.put("customFlag", cartProduct.getCustomFlag());
                        if(TextUtils.isEmpty(cartProduct.getGroupBuyID())){
                            productObj.put("groupBuyID", cartProduct.getGroupBuyID());
                        }else{
                            productObj.put("groupBuyID", "0");
                        }
                        jsonArray.put(productObj);
                    }
                }
                jsonObject.put("productlist", jsonArray);
                JSONObject invoiceObj = new JSONObject();
                invoiceObj.put("invoiceflag", invoiceflag + "");
                invoiceObj.put("invoicetypeid", invoicetypeid + "");
                invoiceObj.put("invoicetitle", invoicetitle + "");
                invoiceObj.put("invoiceidentitynumber", invoicenum + "");
                jsonObject.put("invoiceinfo", invoiceObj);
                jsonObject.put("note", edit_message.getText().toString().trim() + "");
                params.put("sendmsg", jsonObject.toString());
                subMit(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private void subMit(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<MakeOrderRespMsg>(this) {
            @Override
            public void onSuccess(Response response, MakeOrderRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    if (isCustomized || (!buyNow && isAllCustomized)) {
                        ToastUtils.show(MakeOrdersActivity.this, "议价商品将会到您的待付款商品列表中，请跟店主商讨具体价格后进行支付");
                        startActivity(new Intent(BaseApplication.getInstance(), OrdersActivity.class), true);
                    } else {
                        if (isHasCustomized) {
                            ToastUtils.show(MakeOrdersActivity.this, "议价商品已自动为你添加到待支付列表中，请你与店主商讨价格后进行支付");
                        }
                        Intent intent = new Intent(BaseApplication.getInstance(), PayTypeActivity.class);
                        intent.putExtra("result", baseRespMsg.getList().get(0));
                        intent.putExtra("buyNow", buyNow);
                        if (buyNow) {
                            intent.putExtra("product", productDetailBean);
                        } else {
                            intent.putExtra("product", (Serializable) cartProductBeen);
                        }
                        startActivity(intent, true);
                    }
                    finish();
                } else if (baseRespMsg.getResult() == -18) {
                    ToastUtils.show(MakeOrdersActivity.this, "宝贝已被抢光，抱歉哦");
                } else {
                    ToastUtils.show(MakeOrdersActivity.this, "生成订单失败");
                }
                dismissDialog();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(MakeOrdersActivity.this, "提交失败，请稍后重试");
                dismissDialog();
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(MakeOrdersActivity.this, "提交失败，请稍后重试");
                dismissDialog();

            }

            @Override
            public void onFailure(Request request, Exception e) {
                dismissDialog();
            }
        });
    }

    @OnClick(R.id.rel_favourabletype)
    public void toFavourable() {
        if (buyNow) {
            if (("1".equals(productDetailBean.getCustomFlag()) || "0".equals(productDetailBean.getFlag()) || "4".equals(productDetailBean.getFlag())) && ((null != productInfoObject.getPromotionactive() && productInfoObject.getPromotionactive().size() > 0) || (null != productInfoObject.getPoint() && productInfoObject.getPoint().size() > 0) || (null != productInfoObject.getCardList() && productInfoObject.getCardList().size() > 0))) {
                startActivityForResult(new Intent(BaseApplication.getInstance(), FavourableTypeActivity.class).putExtra("productInfoObject", productInfoObject)
                        .putExtra("preferential", preferential + "").putExtra("preferentialvalue", preferentialvalue), true, FAVOURABLE_TYPE);
            } else {
                ToastUtils.show(MakeOrdersActivity.this, "此类商品暂无优惠活动");
            }
        } else {
            if (hasNormal) {
                if (((null != productInfoObject.getPromotionactive() && productInfoObject.getPromotionactive().size() > 0) || (null != productInfoObject.getPoint() && productInfoObject.getPoint().size() > 0) || (null != productInfoObject.getCardList() && productInfoObject.getCardList().size() > 0))) {
                    startActivityForResult(new Intent(BaseApplication.getInstance(), FavourableTypeActivity.class).putExtra("productInfoObject", productInfoObject)
                            .putExtra("preferential", preferential + "").putExtra("preferentialvalue", preferentialvalue), true, FAVOURABLE_TYPE);
                } else {
                    ToastUtils.show(MakeOrdersActivity.this, "暂不满足优惠活动");
                }
            } else {
                ToastUtils.show(MakeOrdersActivity.this, "此类商品暂无优惠活动");
            }
        }

    }

    @OnClick(R.id.rel_invoice)
    public void toInvoice() {
        Intent intent = new Intent(BaseApplication.getInstance(), MakeInvoiceActivity.class);
        intent.putExtra("invoiceflag", invoiceflag);
        intent.putExtra("invoicetypeid", invoicetypeid);
        intent.putExtra("invoicetitle", invoicetitle);
        intent.putExtra("invoicenum", invoicenum);
        startActivityForResult(intent, true, INVOICE);

    }

    @OnClick(R.id.btn_topay)
    public void toPay() {
        initOrderInfo();
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }

    /**
     * 编辑修改足型数据描述
     *
     * @param desc
     */
    public void reviseFootData(String desc,UserFootDataDetailBean footDataDetail) {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "41");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("typeid", "4");
            jsonObject.put("userfoottypedataid", footDataDetail.getUserFootTypeDataID());
            jsonObject.put("sex", footDataDetail.getSex());
            jsonObject.put("age", footDataDetail.getAge());
            jsonObject.put("stature", footDataDetail.getStature());
            jsonObject.put("weight", footDataDetail.getWeight());
            jsonObject.put("shoesize", footDataDetail.getShoeSize());
            jsonObject.put("name", footDataDetail.getName());
            jsonObject.put("measurecode", footDataDetail.getMeasureCode());
            jsonObject.put("footvarusvalgusid", "0");
            jsonObject.put("footarchid", "0");
            //描述
            jsonObject.put("footDesc", desc);
            params.put("sendmsg", jsonObject.toString());
            modifyFootMessage(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void modifyFootMessage(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    ToastUtils.show(BaseApplication.getInstance(), "保存成功");
                } else {
                    ToastUtils.show(BaseApplication.getInstance(), baseRespMsg.getRetmsg());
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
}
