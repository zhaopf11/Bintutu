package com.zhurui.bunnymall.mine.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhurui.bunnymall.MainActivity;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseActivity;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.activity.LogisticalDetailActivity;
import com.zhurui.bunnymall.home.activity.PayTypeActivity;
import com.zhurui.bunnymall.home.bean.CustomeChildPropertyBean;
import com.zhurui.bunnymall.home.bean.MakeOrderBean;
import com.zhurui.bunnymall.home.msg.MakeOrderRespMsg;
import com.zhurui.bunnymall.mine.adapter.OrdersAdapter;
import com.zhurui.bunnymall.mine.bean.AddressBean;
import com.zhurui.bunnymall.mine.bean.OrderBean;
import com.zhurui.bunnymall.mine.bean.OrderShopBean;
import com.zhurui.bunnymall.mine.bean.User;
import com.zhurui.bunnymall.mine.msg.OrderRespMsg;
import com.zhurui.bunnymall.utils.BintutuUtils;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.DialogUtil;
import com.zhurui.bunnymall.viewutils.bean.ProductAttrbuteSxstr;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
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

public class OrdersActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.radiogroup_order)
    RadioGroup radiogroup_order;
    @Bind(R.id.recycler_orders)
    ExpandableListView recycler_orders;
    private OrdersAdapter ordersAdapter;
    private int order_state = 0;
    @Bind(R.id.lin_empty)
    LinearLayout lin_empty;
    DialogUtil dialogUtil;
    private boolean isFirst = false;


    private Map<String, List<OrderBean>> orderShopBeanMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ButterKnife.bind(this);
        initView();
        order_state = getIntent().getIntExtra("orderstate", 0);
//        initData();
        isFirst = true;
    }

    private void initView() {
        text_title.setText(R.string.orders_all);
        text_right.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        radiogroup_order.setOnCheckedChangeListener(changeListener);
        ordersAdapter = new OrdersAdapter(OrdersActivity.this);
        switch (order_state) {
            case 0:
                radiogroup_order.check(R.id.radio_allorders);
                break;
            case 1:
                radiogroup_order.check(R.id.radio_topay);
                break;
            case 2:
                radiogroup_order.check(R.id.radio_tosend);
                break;
            case 3:
                radiogroup_order.check(R.id.radio_toget);
                break;
            case 4:
                radiogroup_order.check(R.id.radio_tojudge);
                break;

        }
        recycler_orders.setAdapter(ordersAdapter);

        recycler_orders.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        ordersAdapter.setOnItemClickLitener(new OrdersAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int groupposition, String shoporderid) {
                startActivity(new Intent(BaseApplication.getInstance(), OrderDetailActivity.class).putExtra("shoporderid", shoporderid), true);
            }

            //1：未付款2：待配货 3: 已发货 4 ：已签收 5:交易成功 6：交易关闭 7：待审核 8：代发货
            //待支付 1 代发货  2 ，8 待收货   3  待评价 4，5
            @Override
            public void rightBtnClick(int groupposition, int position, int status) {
                switch (status) {
                    case 1:
                        //去付款
                        if("8".equals(ordersAdapter.orderShopBeen.get(groupposition).getShopOrderTypeID())){
                            if(TextUtils.isEmpty(ordersAdapter.orderShopBeen.get(groupposition).getTotalPrice())
                                    || "0".equals(ordersAdapter.orderShopBeen.get(groupposition).getShopOrderTypeID())){
                                ToastUtils.show(OrdersActivity.this,"请与商家确认价格");
                            }else {
                                goToPay(groupposition, position);
                            }
                        }else{
                            goToPay(groupposition, position);
                        }
//                        startActivity(new Intent(BaseApplication.getInstance(),PayTypeActivity.class),true);
                        break;
                    case 2:
                        // 提醒发货
                        dealOrder(ordersAdapter.orderShopBeen.get(groupposition).getShopOrderID(), "1", "", "");
                        break;
                    case 3:
                        //确认收货
                        dialogUtil = new DialogUtil();
                        dialogUtil.infoDialog(OrdersActivity.this, "确定是否已签收商品", true, true);
                        dialogUtil.setOnClick(new DialogUtil.OnClick() {
                            @Override
                            public void leftClick() {
                                dialogUtil.dialog.dismiss();
                            }

                            @Override
                            public void rightClick() {
                                dealOrder(ordersAdapter.orderShopBeen.get(groupposition).getShopOrderID(), "2", "1", "0");
                                dialogUtil.dialog.dismiss();
                            }
                        });

                        break;
                    case 4:
                        //待评价
                        if("1".equals(ordersAdapter.orderShopBeen.get(groupposition).getCommentFlag())){
                            ToastUtils.show(BaseApplication.getInstance(),"此商品已评价，不能重复评价");
                        }else {
                            List<OrderBean> orderBeanList = ordersAdapter.orderBeanMap.get(ordersAdapter.orderShopBeen.get(groupposition).getShopOrderID());
                            startActivity(new Intent(BaseApplication.getInstance(), EvaluateActivity.class).putExtra("orderBeanList", (Serializable) orderBeanList), true);
                        }
                        break;
                    case 5:
                        //待评价
                        if("1".equals(ordersAdapter.orderShopBeen.get(groupposition).getCommentFlag())){
                            ToastUtils.show(BaseApplication.getInstance(),"此商品已评价，不能重复评价");
                        }else {
                            List<OrderBean> orderBeanList1 = ordersAdapter.orderBeanMap.get(ordersAdapter.orderShopBeen.get(groupposition).getShopOrderID());
                            startActivity(new Intent(BaseApplication.getInstance(), EvaluateActivity.class).putExtra("orderBeanList", (Serializable) orderBeanList1), true);
                        }
                        break;
                    case 6:
                        toChat(ordersAdapter.orderBeanMap.get(ordersAdapter.orderShopBeen.get(groupposition).getShopOrderID()).get(position));
                        break;
                    case 8:
                        //提醒发货
                        dealOrder(ordersAdapter.orderShopBeen.get(groupposition).getShopOrderID(), "1", "", "");
                        break;

                }
            }

            @Override
            public void leftBtnClick(int groupposition, int position, int status) {
                List<OrderBean> orderBeanList = ordersAdapter.orderBeanMap.get(ordersAdapter.orderShopBeen.get(groupposition).getShopOrderID());
                switch (status) {
                    case 1:
                        toChat(orderBeanList.get(position));
                        break;
                    case 2:
                        toChat(orderBeanList.get(position));
                        break;
                    case 3:
                        checkLogisticsDetail(groupposition, position);
                        break;
                    case 4:
                        checkLogisticsDetail(groupposition, position);
                        break;
                    case 5:
                        checkLogisticsDetail(groupposition, position);
                        break;
                    case 8:
                        toChat(orderBeanList.get(position));
                        break;
                }
            }

            @Override
            public void cancelOrder(int groupposition) {
                //删除订单
                DialogUtil dialogUtil = new DialogUtil();
                dialogUtil.infoDialog(OrdersActivity.this,"确定删除",true,true);
                dialogUtil.setOnClick(new DialogUtil.OnClick() {
                    @Override
                    public void leftClick() {
                        dialogUtil.dialog.dismiss();
                    }

                    @Override
                    public void rightClick() {
                        dealOrder(ordersAdapter.orderShopBeen.get(groupposition).getShopOrderID(), "2", "4", "0");
                        dialogUtil.dialog.dismiss();
                    }
                });
            }
        });
        initInfo();
        isFirst = false;
    }

    private void initInfo() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "46");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("pageno", "1");
            jsonObject.put("pagesize", "100");
            jsonObject.put("status", order_state + "");
            params.put("sendmsg", jsonObject.toString());
            getOrderList(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getOrderList(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<OrderRespMsg>(this) {
            @Override
            public void onSuccess(Response response, OrderRespMsg orderRespMsg) {
                if (orderRespMsg.getResult() > 0) {
                    if (null != orderRespMsg.getList() && orderRespMsg.getList().size() > 0) {
                        orderShopBeanMap = new HashMap<String, List<OrderBean>>();
                        for (OrderShopBean orderShopBean : orderRespMsg.getList().get(0).getShopOrderList()) {
                            List<OrderBean> orderBeen = new ArrayList<OrderBean>();
                            if (!orderShopBeanMap.containsKey(orderShopBean.getShopOrderID())) {
                                orderShopBeanMap.put(orderShopBean.getShopOrderID(), orderBeen);
                            }
                        }

                        for (OrderBean orderBean : orderRespMsg.getList().get(0).getOrderProductList()) {
                            if (orderShopBeanMap.containsKey(orderBean.getShopOrderID())) {
                                orderShopBeanMap.get(orderBean.getShopOrderID()).add(orderBean);
                            }
                        }
                        if (null != orderShopBeanMap && orderShopBeanMap.size() > 0) {
                            recycler_orders.setVisibility(View.VISIBLE);
                            lin_empty.setVisibility(View.GONE);
                            ordersAdapter.orderShopBeen = orderRespMsg.getList().get(0).getShopOrderList();
                            ordersAdapter.orderBeanMap = orderShopBeanMap;
                            ordersAdapter.notifyDataSetChanged();
                            int groupCount = ordersAdapter.getGroupCount();
                            for (int i = 0; i < groupCount; i++) {
                                recycler_orders.expandGroup(i);
                            }
                            ;
                        } else {
                            recycler_orders.setVisibility(View.GONE);
                            lin_empty.setVisibility(View.VISIBLE);
                        }

                    }


                } else {
                    ToastUtils.show(OrdersActivity.this, orderRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(OrdersActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(OrdersActivity.this, "请求失败，请稍后重试");
            }
        });
    }

    RadioGroup.OnCheckedChangeListener changeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                case R.id.radio_allorders:
                    order_state = 0;
                    if(!isFirst){
                        initInfo();
                    }
                    break;
                case R.id.radio_topay:
                    order_state = 1;
                    if(!isFirst){
                        initInfo();
                    }
                    break;
                case R.id.radio_tosend:
                    order_state = 2;
                    if(!isFirst){
                        initInfo();
                    }
                    break;
                case R.id.radio_toget:
                    order_state = 3;
                    if(!isFirst){
                        initInfo();
                    }
                    break;
                case R.id.radio_tojudge:
                    order_state = 4;
                    if(!isFirst){
                        initInfo();
                    }
                    break;
            }
        }
    };


    private void dealOrder(String shoporderid, String actionid, String typeid, String returngoodstypeid) {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "52");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("shoporderid", shoporderid);
            jsonObject.put("actionid", actionid);
            jsonObject.put("typeid", typeid);
            jsonObject.put("returngoodstypeid", returngoodstypeid);
            params.put("sendmsg", jsonObject.toString());
            dealsOrder(params, typeid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void dealsOrder(Map<String, Object> params, String typeid) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getResult() > 0) {
                    if ("".equals(typeid)) {
                        ToastUtils.show(OrdersActivity.this, "提醒成功");
                    } else if ("1".equals(typeid)) {
                        initView();
                        order_state = 4;
                        isFirst =true;
                        initData();
                    } else if ("4".equals(typeid)) {
                        ToastUtils.show(OrdersActivity.this, "删除订单成功");
                        initView();
                        initData();
                    }
                } else {
                    ToastUtils.show(OrdersActivity.this, baseRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(OrdersActivity.this, "处理失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(OrdersActivity.this, "处理失败，请稍后重试");

            }
        });
    }

    @OnClick(R.id.text_stroll)
    public void toStroll(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("from", 0);
        startActivity(intent);
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }

    /**
     * 联系商家
     * @param orderBean
     */
    public void toChat(OrderBean orderBean) {
        if(orderBean != null && orderBean.getDingZhiShuXing() != null){
            List<CustomeChildPropertyBean> list = orderBean.getDingZhiShuXing().getYanseList();
            String yansemage = "";
            if(list!= null && list.size() > 0){
                yansemage = list.get(0).getCustomPropertiesValueImageUrl();
            }
            BintutuUtils.connectionSeller(this,orderBean.getProductID(),orderBean.getName(),orderBean.getPrice(),yansemage,orderBean.getXnSupplierID());
        }
    }

    /**
     * 查看物流信息
     * 并传递到下一个界面产品的信息以及订单号
     *
     * @param position
     */
    public void checkLogisticsDetail(int groupposition, int position) {
        Intent intent = new Intent(BaseApplication.getInstance(), LogisticalDetailActivity.class);
        intent.putExtra("shoporderid", ordersAdapter.orderShopBeen.get(groupposition).getShopOrderID());
        intent.putExtra("isOrders", true);
        List<OrderBean> orderBeanList = ordersAdapter.orderBeanMap.get(ordersAdapter.orderShopBeen.get(groupposition).getShopOrderID());
        ProductAttrbuteSxstr productSxstr = orderBeanList.get(position).getDingZhiShuXing();
        String yanseImage = "";
        if(productSxstr.getYanseList() != null && productSxstr.getYanseList().size() > 0){//选中颜色的图片
            yanseImage = productSxstr.getYanseList().get(0).getCustomPropertiesValueImageUrl();
        }
        if (orderBeanList != null && orderBeanList.size() > 0) {
            if(TextUtils.isEmpty(yanseImage)){
                intent.putExtra("image", orderBeanList.get(position).getMainImage());
            }else{
                intent.putExtra("image", yanseImage);
            }
            intent.putExtra("size", orderBeanList.get(position).getNumber());
        }
        startActivity(intent);
    }

    /**
     * 去付款
     *
     * @param groupposition
     * @param position
     */
    public void goToPay(int groupposition, int position) {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "67");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("shoporderid", ordersAdapter.orderShopBeen.get(groupposition).getShopOrderID());
            params.put("sendmsg", jsonObject.toString());
            getOrdersInfo(params, groupposition);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getOrdersInfo(Map<String, Object> params, int groupposition) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<MakeOrderRespMsg>(this) {
            @Override
            public void onSuccess(Response response, MakeOrderRespMsg makeOrderRespMsg) {
                if (makeOrderRespMsg.getResult() > 0) {
                    if (makeOrderRespMsg.getList() != null && makeOrderRespMsg.getList().size() > 0) {
                        MakeOrderBean makeOrderBean = makeOrderRespMsg.getList().get(0);
                        if (makeOrderBean != null) {
                            Intent intent = new Intent(BaseApplication.getInstance(), PayTypeActivity.class);
                            intent.putExtra("shopOrderID", makeOrderBean.getShoporderid());
                            intent.putExtra("needPayMoney", makeOrderBean.getNeedpaymoney());
                            intent.putExtra("transactionid", makeOrderBean.getTransactionid());
                            intent.putExtra("usermoney", makeOrderBean.getUsermoney());

                            List<OrderBean> orderBeanList = ordersAdapter.orderBeanMap.get(ordersAdapter.orderShopBeen.get(groupposition).getShopOrderID());
                            intent.putExtra("orderBeanList", (Serializable) orderBeanList);
                            intent.putExtra("isOrders", true);
                            startActivity(intent);
                        }
                    }
                } else {
                    ToastUtils.show(OrdersActivity.this, makeOrderRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(OrdersActivity.this, "处理失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(OrdersActivity.this, "处理失败，请稍后重试");

            }
        });
    }
}
