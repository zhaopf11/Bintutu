package com.zhurui.bunnymall.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.activity.LogisticalDetailActivity;
import com.zhurui.bunnymall.home.activity.PayTypeActivity;
import com.zhurui.bunnymall.home.activity.ProductNewDetailActivity;
import com.zhurui.bunnymall.home.bean.CustomeChildPropertyBean;
import com.zhurui.bunnymall.home.bean.MakeOrderBean;
import com.zhurui.bunnymall.home.msg.MakeOrderRespMsg;
import com.zhurui.bunnymall.mine.adapter.OrderDetailAdapter;
import com.zhurui.bunnymall.mine.bean.CancleReasonBaen;
import com.zhurui.bunnymall.mine.bean.OrderBean;
import com.zhurui.bunnymall.mine.bean.OrderProductDetaiListlBean;
import com.zhurui.bunnymall.mine.bean.OrderProductDetail;
import com.zhurui.bunnymall.mine.bean.User;
import com.zhurui.bunnymall.mine.msg.OrderRespMsg;
import com.zhurui.bunnymall.utils.BintutuUtils;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.CancleReasonDialog;
import com.zhurui.bunnymall.viewutils.DialogUtil;
import com.zhurui.bunnymall.viewutils.bean.ProductAttrbuteSxstr;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaoneng.coreapi.ChatParamsBody;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.utils.CoreData;
import okhttp3.Response;

public class OrderDetailActivity extends BaseActivity {
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.recycler_orders)
    RecyclerView recycler_orders;

    @Bind(R.id.text_ordertime)
    TextView text_ordertime;
    @Bind(R.id.text_ordernum)
    TextView text_ordernum;
    @Bind(R.id.text_buyname)
    TextView text_buyname;
    @Bind(R.id.text_phonenum)
    TextView text_phonenum;
    @Bind(R.id.text_address)
    TextView text_address;
    @Bind(R.id.text_paytype)
    TextView text_paytype;
    @Bind(R.id.text_price)
    TextView text_price;
    @Bind(R.id.text_fare)
    TextView text_fare;
    @Bind(R.id.text_pricepay)
    TextView text_pricepay;

    private OrderDetailAdapter orderDetailAdapter;
    public static OrderProductDetail orderProductDetail;
    private CancleReasonDialog cancleReasonDialog;

    private String shoporderid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        shoporderid = getIntent().getStringExtra("shoporderid");
        initView();
    }

    private void initView(){
        text_title.setText(R.string.order_detail);
        text_right.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData(){
        orderDetailAdapter= new OrderDetailAdapter(BaseApplication.getInstance());
//        orderDetailAdapter.ORDER_STATE = ORDER_STATE;
        orderDetailAdapter.setOnItemClickLitener(new OrderDetailAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                String productid = orderDetailAdapter.orderProductList.get(position).getProductID();
                startActivity(new Intent(BaseApplication.getInstance(), ProductNewDetailActivity.class).putExtra("productId", Long.parseLong(productid)), false);
            }

            @Override
            public void rightBtnClick(int position, int status) {
                //1，付款 2, 3, 4, 5, 8 联系卖家
                switch (status){
                    case 1:
                        //待完善传值
                        if("8".equals(orderProductDetail.getShopOrderTypeID())){
                            if(TextUtils.isEmpty(orderProductDetail.getTotalPrice()) || "0".equals(orderProductDetail.getShopOrderTypeID())){
                                ToastUtils.show(OrderDetailActivity.this,"请与商家确认价格");
                            }else {
                                goToPay();
                            }
                        }else{
                            goToPay();
                        }
                        break;
                    case 2:
                        toChat(orderDetailAdapter.orderProductList.get(position));
                        break;
                    case 3:
                        toChat(orderDetailAdapter.orderProductList.get(position));
                        break;
                    case 4:
                        toChat(orderDetailAdapter.orderProductList.get(position));
                        break;
                    case 5:
                        toChat(orderDetailAdapter.orderProductList.get(position));
                        break;
                    case 8:
                        toChat(orderDetailAdapter.orderProductList.get(position));
                        break;

                }
            }

            @Override
            public void centerBtnClick(int position, int status) {
                //1，联系卖家 2，8 退款 3，查看物流 4,5评价商品 ,
                switch (status){
                    case 1:
                        toChat(orderDetailAdapter.orderProductList.get(position));
                        break;
                    case 2:
                        cancleOrder(status,position,true);
                        break;
                    case 3:
                        checkLogisticsDetail(position);
                        break;
                    case 4:
                        if("1".equals(orderProductDetail.getCommentFlag())){
                            ToastUtils.show(BaseApplication.getInstance(),"此商品已评价，不能重复评价");
                        }else {
                            startActivity(new Intent(BaseApplication.getInstance(),EvaluateActivity.class).putExtra("orderBeanList",(Serializable)orderDetailAdapter.orderProductList),true);
                        }
                        break;
                    case 5:
                        if("1".equals(orderProductDetail.getCommentFlag())){
                            ToastUtils.show(BaseApplication.getInstance(),"此商品已评价，不能重复评价");
                        }else {
                            startActivity(new Intent(BaseApplication.getInstance(),EvaluateActivity.class).putExtra("orderBeanList",(Serializable)orderDetailAdapter.orderProductList),true);
                        }
                        break;
                    case 8:
                        cancleOrder(status,position,true);
                        break;
                }
            }

            @Override
            public void leftBtnClick(int position, int status) {
                //1：取消订单
                switch (status){
                    case 1:
                        DialogUtil dialogUtil = new DialogUtil();
                        dialogUtil.infoDialog(OrderDetailActivity.this, "是否确认取消订单", true, true);
                        dialogUtil.setOnClick(new DialogUtil.OnClick() {
                            @Override
                            public void leftClick() {
                                dialogUtil.dialog.dismiss();
                            }

                            @Override
                            public void rightClick() {
                                dealOrder(orderDetailAdapter.orderProductList.get(position).getShopOrderID(),"2","2","0");
                                dialogUtil.dialog.dismiss();
                            }
                        });
                        break;
                }
            }

            @Override
            public void returnRightBtnClick(int position, int status, String returnGoodsStatus) {
                //退货,当拒绝退货的时候，不允许发起第二次退货申请
                if(!TextUtils.isEmpty(returnGoodsStatus)){
                    if("4".equals(returnGoodsStatus)){
                        ToastUtils.show(BaseApplication.getInstance(),"您已超过次数，请联系卖家进行协商或直接联系平台客服");
                    }
                }else{
                    cancleOrder(status,position,false);
                }
            }

            @Override
            public void retrunCenterBtnClick(int position, int status, String returnGoodsStatus) {
                switch (status) {
                    case 4:
                        //客户退货发货
                        Intent intent = new Intent(BaseApplication.getInstance(),DeliveryActivity.class);
                        intent.putExtra("shoporderid",orderDetailAdapter.orderProductList.get(position).getShopOrderID());
                        intent.putExtra("productid",orderDetailAdapter.orderProductList.get(position).getProductID());
                        startActivity(intent);
                        break;
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        recycler_orders.setLayoutManager(linearLayoutManager);

        recycler_orders.setAdapter(orderDetailAdapter);
        initInfo();
    }


    private void initInfo(){
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "47");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("shoporderid",shoporderid);
            params.put("sendmsg", jsonObject.toString());
            getOrderDetail(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void dealOrder(String shoporderid,String actionid,String typeid,String returngoodstypeid){
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "52");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("shoporderid",shoporderid);
            jsonObject.put("actionid", actionid);
            jsonObject.put("typeid",typeid);
            jsonObject.put("returngoodstypeid", returngoodstypeid);
            params.put("sendmsg", jsonObject.toString());
            dealsOrder(params,typeid,shoporderid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void dealsOrder(Map<String, Object> params,String typeid,String shrid)  {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    if("2".equals(typeid)){
                        ToastUtils.show(OrderDetailActivity.this, "取消订单成功");
                        initView();
                        shoporderid = shrid;
                        initData();
                    }else if("3".equals(typeid)){
                        ToastUtils.show(OrderDetailActivity.this, "退货处理中");
                        initView();
                        shoporderid = shrid;
                        initData();
                    }else if("5".equals(typeid)){
//                        ToastUtils.show(OrderDetailActivity.this, "退款成功");
                        initView();
                        shoporderid = shrid;
                        initData();
                    }
                } else {
                    ToastUtils.show(OrderDetailActivity.this, baseRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(OrderDetailActivity.this, "处理失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(OrderDetailActivity.this, "处理失败，请稍后重试");

            }
        });
    }

    public void getOrderDetail(Map<String, Object> params){
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<OrderProductDetaiListlBean>(this) {
            @Override

            public void onSuccess(Response response, OrderProductDetaiListlBean orderProductDetaiListlBean) {
                if (orderProductDetaiListlBean.getResult() > 0) {
                    if(orderProductDetaiListlBean.getList() != null && orderProductDetaiListlBean.getList().size() > 0){
                        List<OrderProductDetail> orderProductDetailList = orderProductDetaiListlBean.getList().get(0).getOrderProductDetail();
                        List<OrderBean> orderProductList = orderProductDetaiListlBean.getList().get(0).getOrderProductList();
                        if(orderProductDetailList != null && orderProductDetailList.size() > 0){
                            orderProductDetail = orderProductDetailList.get(0);
                            updataToUi(orderProductDetail);
                        }
                        orderDetailAdapter.orderProductList = orderProductList;
                        orderDetailAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.show(OrderDetailActivity.this, orderProductDetaiListlBean.getRetmsg());
                }
            }
            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(OrderDetailActivity.this, "请求失败，请稍后重试");
            }
            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(OrderDetailActivity.this, "请求失败，请稍后重试");

            }
        });
    }

    private void updataToUi(OrderProductDetail orderProductDetail) {
        if(orderProductDetail != null){
            text_ordertime.setText(orderProductDetail.getOrderTime());
            text_ordernum.setText(orderProductDetail.getShopOrderID());
            text_buyname.setText(orderProductDetail.getShouHuoRen());
            text_phonenum.setText(orderProductDetail.getMobile());
            text_address.setText(orderProductDetail.getProvincename() + orderProductDetail.getCityname()
                    + orderProductDetail.getTownname() + orderProductDetail.getAddress());
            if("3".equals(orderProductDetail.getPayTypeID())){
                text_paytype.setText("支付宝");
            }else if("15".equals(orderProductDetail.getPayTypeID())){
                text_paytype.setText("微信");
            }else {
                text_paytype.setText("余额/积分");
            }
            if(!TextUtils.isEmpty(orderProductDetail.getTotalPrice())){
                text_price.setText("¥"+ orderProductDetail.getProductMoney());
            }
            if(!TextUtils.isEmpty(orderProductDetail.getDeliveryMoney())){
                text_fare.setText("¥"+ orderProductDetail.getDeliveryMoney());
            }
            if(!TextUtils.isEmpty(orderProductDetail.getNeedPayMoney())){
                text_pricepay.setText("¥"+ orderProductDetail.getNeedPayMoney());
            }
        }

    }


    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }

    /**
     * 查看物流信息
     * 并传递到下一个界面产品的信息以及订单号
     * @param position
     */
    public void checkLogisticsDetail(int position){
        Intent intent = new Intent(BaseApplication.getInstance(), LogisticalDetailActivity.class);
        intent.putExtra("shoporderid",orderProductDetail.getShopOrderID());
        intent.putExtra("isOrders",true);
        List<OrderBean> orderBeanList = orderDetailAdapter.orderProductList;
        ProductAttrbuteSxstr productSxstr = orderBeanList.get(position).getDingZhiShuXing();
        String yanseImage = "";
        if(productSxstr.getYanseList() != null && productSxstr.getYanseList().size() > 0){//选中颜色的图片
            yanseImage = productSxstr.getYanseList().get(0).getCustomPropertiesValueImageUrl();
        }
        if(orderBeanList !=null && orderBeanList.size() > 0){
            if(TextUtils.isEmpty(yanseImage)){
                intent.putExtra("image",orderBeanList.get(position).getMainImage());
            }else{
                intent.putExtra("image",yanseImage);
            }
            intent.putExtra("size",orderBeanList.get(position).getNumber());
        }
        startActivity(intent);
    }

    public void cancleOrder(int states,int position,boolean isCancleOrder){
        cancleReasonDialog = new CancleReasonDialog(OrderDetailActivity.this, R.style.CustomDialog,isCancleOrder,false,null);
        cancleReasonDialog.setBtnOnClice(new CancleReasonDialog.BtnOnClick(){

            @Override
            public void btnOnClick(List<CancleReasonBaen> info) {
                String returngoodstypeid = "";
                String typeid = "";
                if(info != null && info.size() > 0){
                    for(int i =0;i < info.size();i++){
                        if(info.get(i).getSelect()){
                            returngoodstypeid = info.get(i).getReansonId();
                        }
                    }
                }
                if(states == 1){
                    typeid = "2";//取消订单
                }else if(states == 2 || states == 8 ){
                    typeid = "5"; //退款
                }else if(states == 4){
                    typeid = "3";//退货
                }
                if(TextUtils.isEmpty(returngoodstypeid)){
                    if("5".equals(typeid )){
                        ToastUtils.show(OrderDetailActivity.this, "请选择退款的原因");
                    }else if("3".equals(typeid )){
                        ToastUtils.show(OrderDetailActivity.this, "请选择退货的原因");
                    }
                }else{
                    dealOrder(orderDetailAdapter.orderProductList.get(position).getShopOrderID(),"2",typeid,(Integer.parseInt(returngoodstypeid)+1)+"");
                }
            }
        });
        cancleReasonDialog.show();
    }

    /**
     * 去付款
     */
    public void goToPay(){
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "67");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("shoporderid",orderProductDetail.getShopOrderID());
            params.put("sendmsg", jsonObject.toString());
            getOrdersInfo(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getOrdersInfo(Map<String, Object> params)  {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<MakeOrderRespMsg>(this) {
            @Override
            public void onSuccess(Response response, MakeOrderRespMsg makeOrderRespMsg) {
                if (makeOrderRespMsg.getResult() > 0) {
                    if(makeOrderRespMsg.getList() != null && makeOrderRespMsg.getList().size() > 0){
                        MakeOrderBean makeOrderBean = makeOrderRespMsg.getList().get(0);
                        if(makeOrderBean != null){
                            Intent intent = new Intent(BaseApplication.getInstance(), PayTypeActivity.class);
                            intent.putExtra("shopOrderID", makeOrderBean.getShoporderid());
                            intent.putExtra("needPayMoney", makeOrderBean.getNeedpaymoney());
                            intent.putExtra("transactionid", makeOrderBean.getTransactionid());
                            intent.putExtra("usermoney", makeOrderBean.getUsermoney());

                            List<OrderBean> orderBeanList = orderDetailAdapter.orderProductList;
                            intent.putExtra("orderBeanList",(Serializable) orderBeanList);
                            intent.putExtra("isOrders",true);
                            startActivity(intent);
                        }
                    }
                } else {
                    ToastUtils.show(OrderDetailActivity.this, makeOrderRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(OrderDetailActivity.this, "处理失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(OrderDetailActivity.this, "处理失败，请稍后重试");

            }
        });
    }

    /**
     * 联系商家
     * @param orderBean
     */
    public void toChat(OrderBean orderBean) {
        if(orderBean != null && orderBean.getDingZhiShuXing() != null) {
            List<CustomeChildPropertyBean> list = orderBean.getDingZhiShuXing().getYanseList();
            String yansemage = "";
            if (list != null && list.size() > 0) {
                yansemage = list.get(0).getCustomPropertiesValueImageUrl();
            }
            BintutuUtils.connectionSeller(this,orderBean.getProductID(),orderBean.getName(),orderBean.getPrice(),yansemage,orderBean.getXnSupplierID());
        }
    }
}
