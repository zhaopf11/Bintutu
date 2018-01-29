package com.zhurui.bunnymall.cart.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhurui.bunnymall.MainActivity;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.cart.adapter.ShopCartAdapter;
import com.zhurui.bunnymall.cart.bean.Cart;
import com.zhurui.bunnymall.cart.bean.CartBean;
import com.zhurui.bunnymall.cart.bean.CartProductBean;
import com.zhurui.bunnymall.cart.bean.CartSupplierBean;
import com.zhurui.bunnymall.cart.msg.CartRespMsg;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.BaseFragment;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.activity.MakeOrdersActivity;
import com.zhurui.bunnymall.home.activity.ProductNewDetailActivity;
import com.zhurui.bunnymall.home.msg.AddCartRespMsg;
import com.zhurui.bunnymall.mine.activity.LoginActivity;
import com.zhurui.bunnymall.mine.bean.User;
import com.zhurui.bunnymall.mine.msg.AccountLoginResp;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.utils.Utils;
import com.zhurui.bunnymall.viewutils.DialogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;


/**
 */
public class CartFragment extends BaseFragment {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtn_back;
    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_right)
    TextView text_right;
    @Bind(R.id.xrecyclerView)
    RecyclerView xRecyclerView;

    @Bind(R.id.rel_bottom)
    RelativeLayout rel_bottom;
    @Bind(R.id.checkbox_all)
    CheckBox checkBox_all;
    @Bind(R.id.text_price)
    TextView text_price;
    @Bind(R.id.text_cartbottom)
    TextView text_cartbottom;
    @Bind(R.id.lin_empty)
    LinearLayout lin_empty;
    @Bind(R.id.lin_price)
    LinearLayout lin_price;

    private static final int REQUEST_CODE = 1;
    private boolean isEdit;

    //    private List<Cart> carts = null;
    private ShopCartAdapter shopCartAdapter;
    //判断全选是主动点击还是由于购物车内的商品的点击造成的状态的变化，默认是true即主动的
    private boolean checkBox_All = true;

    private int chooseCount = 0;

    public Map<Integer, Integer> numMap = new HashMap<>();

    private float totalPrice = 0;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private List<CartProductBean> cartProductList = new ArrayList<>();
    private List<CartBean> cartBeanList = new ArrayList<>();
    private Map<String, List<CartProductBean>> listMap;
    private boolean isRefreshData = false;

//actionid  1
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {
        initView();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isRefreshData){
            if(Utils.isLogin()){
                initReuqestInfo();
                isRefreshData = false;
            }
        }
    }

    private void initView() {
        imgbtn_back.setVisibility(View.INVISIBLE);
        text_title.setText(getString(R.string.cart));
        text_right.setText("编辑");
    }

    private void initData() {
        xRecyclerView.setLayoutManager(new LinearLayoutManager(BaseApplication.getInstance()));
        shopCartAdapter = new ShopCartAdapter(BaseApplication.getInstance());
        shopCartAdapter.isEdit = isEdit;
        xRecyclerView.setAdapter(shopCartAdapter);
        countChoose();
        shopCartAdapter.setOnItemClick(new ShopCartAdapter.OnItemClick() {

            @Override
            public void groupCheck(boolean isCheck, int position) {
                for (int i = 0; i < cartBeanList.size(); i++) {
                    if (cartProductList.get(position).getSupplierID().equals(cartProductList.get(i).getSupplierID())) {
                        cartBeanList.get(i).setCheck(isCheck);
                    }

                }
                checkBox_All = false;
                if (isAllCheck()) {
                    checkBox_all.setChecked(true);
                } else {
                    checkBox_all.setChecked(false);

                }
                countChoose();
                shopCartAdapter.notifyDataSetChanged();
                checkBox_All = true;

            }

            @Override
            public void childCheck(boolean isCheck, int postion) {
                //如果是选中状态，则要判断是否这个店商品全选，和所有商品是否全选，不是选中状态，则该店不选中，全选按钮不选中
                if (isCheck) {
                    cartBeanList.get(postion).setCheck(true);
                    CartBean checkCartBean = cartBeanList.get(postion);
                    CartProductBean checkCartProductBean = cartProductList.get(postion);
                    boolean noCheck = true;//判断选中商品中的店铺中是不是有商品没有被选中，默认为都是选中的，遍历所有商品，查看该店铺中是否有没有选中的
                    for (int i = 0; i < cartProductList.size(); i++) {
                        CartProductBean cartProductBean = cartProductList.get(i);
                        CartBean cartBean = cartBeanList.get(i);
                        if (checkCartProductBean.getSupplierID().equals(cartProductBean.getSupplierID()) && !cartProductBean.getSupplierID().equals(cartProductBean.getProductID())) {
                            if (!cartBean.isCheck()) {
                                noCheck = false;
                            }
                        }
                    }
                    int groupPosition = checkCartBean.getGroupPosition();
                    //全部选中，该店铺也是选中状态
                    if (noCheck) {
                        cartBeanList.get(groupPosition).setCheck(true);
                        if (isAllCheck()) {
                            checkBox_all.setChecked(true);
                        } else {
                            checkBox_all.setChecked(false);
                        }
                    } else {
                        cartBeanList.get(groupPosition).setCheck(false);
                        if (checkBox_all.isChecked()) {
                            checkBox_All = false;
                            checkBox_all.setChecked(false);
                        }
                    }


                } else {
                    cartBeanList.get(postion).setCheck(false);
                    cartBeanList.get(cartBeanList.get(postion).getGroupPosition()).setCheck(false);
                    if (checkBox_all.isChecked()) {
                        checkBox_All = false;
                        checkBox_all.setChecked(false);
                    }

                }
                shopCartAdapter.notifyDataSetChanged();
                checkBox_All = true;
//                if (isEdit) {
                countChoose();
//                }
            }

            @Override
            public void itemClick(int postion) {
                String productid = cartProductList.get(postion).getProductID();
                startActivity(new Intent(BaseApplication.getInstance(), ProductNewDetailActivity.class).putExtra("productId", Long.parseLong(productid)), false);
            }

            @Override
            public void numChange(int position, int amount) {
                cartProductList.get(position).setNumber(amount + "");

            }


        });
        refData();
    }

    /*
         判断所有商品是否全都选中
     */
    private boolean isAllCheck() {
        boolean isCheckAll = true;
        for (CartBean cart : cartBeanList) {
            if (!cart.isCheck()) {
                isCheckAll = false;
                break;
            }
        }
        return isCheckAll;
    }

    /*
       计算被选中的商品
     */

    private void countChoose() {
        chooseCount = 0;
        totalPrice = 0;

        if (null != cartProductList && cartProductList.size() > 0) {
            for (int i = 0; i < cartProductList.size(); i++) {
                if (cartBeanList.get(i).isCheck() && !cartProductList.get(i).getSupplierID().equals(cartProductList.get(i).getProductID()) && !"2".equals(cartProductList.get(i).getCustomFlag())) {
                    chooseCount++;
                    totalPrice = totalPrice + ((Float.parseFloat(cartProductList.get(i).getPrice()) + + Float.parseFloat(cartProductList.get(i).getShuxingPrice())) * Integer.parseInt(cartProductList.get(i).getNumber()));
                }
            }
        }
        text_price.setText(decimalFormat.format(totalPrice) + "");
    }

    @OnClick(R.id.text_right)
    public void isEdit() {
        isEdit = !isEdit;
        shopCartAdapter.isEdit = isEdit;
        shopCartAdapter.notifyDataSetChanged();
        if (isEdit) {
            text_right.setText("完成");
            lin_price.setVisibility(View.GONE);
            text_cartbottom.setText("删除所选");
            checkBox_all.setText("全选");
        } else {
            text_right.setText("编辑");
            lin_price.setVisibility(View.VISIBLE);
            text_cartbottom.setText("结算");
            checkBox_all.setText("全选");
            JSONArray jsonArray = getEditProductList("2");
            if (null != jsonArray && jsonArray.length() > 0) {
                initEditInfo("2", jsonArray);
            }
        }
        countChoose();
    }

    @OnCheckedChanged(R.id.checkbox_all)
    public void checkedAll() {
        if (checkBox_All && null != cartBeanList && cartBeanList.size() > 0) {
            for (int i = 0; i < cartBeanList.size(); i++) {
                cartBeanList.get(i).setCheck(checkBox_all.isChecked());
            }
            shopCartAdapter.notifyDataSetChanged();
//            if (isEdit) {
            countChoose();
//            }
        }
    }

    @OnClick(R.id.text_cartbottom)
    public void bottomClick() {
        if (isEdit) {
            //在编辑情况下进行删除操作
            JSONArray jsonArray = getEditProductList("3");
            if (null != jsonArray && jsonArray.length() > 0) {
                deleteCheckProduct(jsonArray);
            }else{
                ToastUtils.show(getActivity(),"请选中要删除的商品");
            }
        } else {
            List<CartProductBean> cartProductBeenChecked = new ArrayList<>();
            for (int i = 0; i < cartBeanList.size(); i++) {
                if (cartBeanList.get(i).isCheck() && !cartProductList.get(i).getSupplierID().equals(cartProductList.get(i).getProductID())) {
                    cartProductBeenChecked.add(cartProductList.get(i));
                }
            }
            if (null != cartProductBeenChecked && cartProductBeenChecked.size() > 0) {
                Intent intent = new Intent(BaseApplication.getInstance(), MakeOrdersActivity.class);
                intent.putExtra("actionid", "1");
                intent.putExtra("buyNow", false);
                intent.putExtra("productlist", (Serializable) cartProductBeenChecked);
                startActivity(intent, false);
                isRefreshData = true;
            } else {
                ToastUtils.show(getActivity(), "请选择商品进行下单");
            }
        }
    }

    /**
     *删除已选中的商品
     * @param jsonArray
     */
    private void deleteCheckProduct(JSONArray jsonArray) {
        DialogUtil dialogUtil = new DialogUtil();
        dialogUtil.infoDialog(getActivity(),"确定要删除选中的商品吗",true,true);
        dialogUtil.setOnClick(new DialogUtil.OnClick() {
            @Override
            public void leftClick() {
                dialogUtil.dialog.dismiss();
            }

            @Override
            public void rightClick() {
                initEditInfo("3", jsonArray);
                dialogUtil.dialog.dismiss();
            }
        });
    }

    public void refData() {
        if (null == BaseApplication.getInstance().getUser() || 0 == BaseApplication.getInstance().getUser().getUserID() || "".equals(BaseApplication.getInstance().getToken())) {
            startActivityForResult(new Intent(BaseApplication.getInstance(), LoginActivity.class), REQUEST_CODE);
        } else {
            initReuqestInfo();
            text_right.setText("编辑");
            lin_price.setVisibility(View.VISIBLE);
            text_cartbottom.setText("结算");
            checkBox_all.setText("全选");
            isEdit =false;
            shopCartAdapter.isEdit = isEdit;
            text_price.setText("0.00");
        }
    }


    private void initReuqestInfo() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "54");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID() + "");
            jsonObject.put("token", BaseApplication.getInstance().getToken() + "");
            jsonObject.put("keyword", "");
            params.put("sendmsg", jsonObject.toString());
            getCartInfo(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCartInfo(Map<String, Object> params) {
        if(mHttpHelper != null) {
            mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<CartRespMsg>(getActivity()) {
                @Override
                public void onSuccess(Response response, CartRespMsg cartRespMsg) {
                    if (cartRespMsg.getResult() > 0) {
                        if (null != cartRespMsg.getList() && cartRespMsg.getList().size() > 0) {
                            cartProductList.clear();
                            cartBeanList.clear();
                            Map<String, CartSupplierBean> map = new HashMap<String, CartSupplierBean>();
                            for (CartSupplierBean cartSupplierBean : cartRespMsg.getSupplierlist()) {
                                map.put(cartSupplierBean.getSupplierid(), cartSupplierBean);
                            }
                            for (Map.Entry<String, CartSupplierBean> entry : map.entrySet()) {
                                CartSupplierBean cartSupplierBean = entry.getValue();
                                int groupPositon = cartProductList.size();
                                CartBean cartBean = new CartBean();
                                cartBean.setCheck(false);
                                cartBean.setGroupPosition(groupPositon);
                                cartBeanList.add(cartBean);
                                CartProductBean cartProductBean = new CartProductBean();
                                cartProductBean.setXnsupplierId(cartSupplierBean.getXnsupplierId());
                                cartProductBean.setSupplierID(cartSupplierBean.getSupplierid());
                                cartProductBean.setSupplierName(cartSupplierBean.getSuppliername());
                                cartProductBean.setProductID(cartSupplierBean.getSupplierid());
                                cartProductList.add(cartProductBean);
                                for (CartProductBean cartProduct : cartRespMsg.getList()) {
                                    if (cartSupplierBean.getSupplierid().equals(cartProduct.getSupplierID())) {
                                        cartProductList.add(cartProduct);
                                        CartBean cartBean1 = new CartBean();
                                        cartBean1.setCheck(false);
                                        cartBean1.setGroupPosition(groupPositon);
                                        cartBean1.setNumber(cartProduct.getNumber());
                                        cartBeanList.add(cartBean1);
                                    }
                                }
                            }

                            if (null != cartProductList && cartProductList.size() > 0) {
                                lin_empty.setVisibility(View.GONE);
                                xRecyclerView.setVisibility(View.VISIBLE);
                                rel_bottom.setVisibility(View.VISIBLE);
                                text_right.setVisibility(View.VISIBLE);
                                shopCartAdapter.cartBeanList = cartBeanList;
                                shopCartAdapter.cartProductBeen = cartProductList;
                                shopCartAdapter.notifyDataSetChanged();
                                checkBox_all.setChecked(false);
                            } else {
                                lin_empty.setVisibility(View.VISIBLE);
                                xRecyclerView.setVisibility(View.GONE);
                                rel_bottom.setVisibility(View.GONE);
                                text_right.setVisibility(View.GONE);
                                checkBox_all.setChecked(false);
                            }

                        } else {
                            lin_empty.setVisibility(View.VISIBLE);
                            xRecyclerView.setVisibility(View.GONE);
                            rel_bottom.setVisibility(View.GONE);
                            text_right.setVisibility(View.GONE);
                            checkBox_all.setChecked(false);
                        }
                    } else {
                        ToastUtils.show(getActivity(), cartRespMsg.getRetmsg());
                    }
                }

                @Override
                public void onError(Response response, int code, Exception e) {
                    dismissDialog();
                    ToastUtils.show(getActivity(), "请求失败，请稍后重试");
                }

                @Override
                public void onServerError(Response response, int code, String errmsg) {
                    dismissDialog();
                    ToastUtils.show(BaseApplication.getInstance(), "请求失败，请稍后重试");
                }

                @Override
                public void onFailure(Request request, Exception e) {
                    super.onFailure(request, e);
                    dismissDialog();
                    ToastUtils.show(BaseApplication.getInstance(), "请求失败，请稍后重试");
                }
            });
        }
    }

    private JSONArray getEditProductList(String actionId) {
        JSONArray jsonArray = new JSONArray();
        if ("2".equals(actionId)) {
            for (int i = 0; i < cartProductList.size(); i++) {
                if (!cartProductList.get(i).getSupplierID().equals(cartProductList.get(i).getProductID()) && !cartProductList.get(i).getNumber().equals(cartBeanList.get(i).getNumber())) {
                    CartProductBean cartProductBean = cartProductList.get(i);
                    JSONObject jsonObject = initJSonObject(cartProductBean);
                    jsonArray.put(jsonObject);
                }
            }
        } else if ("3".equals(actionId)) {
            if (cartProductList != null && cartProductList.size() > 0) {
                for (int i = 0; i < cartProductList.size(); i++) {
                    if (!cartProductList.get(i).getSupplierID().equals(cartProductList.get(i).getProductID()) && cartBeanList.get(i).isCheck()) {
                        CartProductBean cartProductBean = cartProductList.get(i);
                        JSONObject jsonObject = initJSonObject(cartProductBean);
                        jsonArray.put(jsonObject);
                    }
                }
            }
        }
        return jsonArray;
    }


    private JSONObject initJSonObject(CartProductBean cartProductBean) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("typeid", cartProductBean.getCartItemTypeID());
            jsonObject.put("productid", cartProductBean.getProductID());
            jsonObject.put("name", cartProductBean.getProductName());
            jsonObject.put("supplierid", cartProductBean.getSupplierID());
            jsonObject.put("suppliername", cartProductBean.getSupplierName());
            jsonObject.put("number", cartProductBean.getNumber());
            jsonObject.put("colorid", cartProductBean.getColorID());
            jsonObject.put("colorpropertiesname", cartProductBean.getColorName());
            jsonObject.put("sizeid", cartProductBean.getSizeID());
            jsonObject.put("sizePropertiesName", cartProductBean.getSizeName());
            jsonObject.put("price", cartProductBean.getPrice());
            jsonObject.put("deliveryfee", cartProductBean.getFee());
            jsonObject.put("deliveryname", "");
            jsonObject.put("cartID",cartProductBean.getCartID());
            jsonObject.put("userfoottypedataid", cartProductBean.getFootTypeDataID());
            jsonObject.put("custompropertiesinfo", cartProductBean.getCustomPropInfo());
            jsonObject.put("mainimage", cartProductBean.getProductMainImage());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void initEditInfo(String actionid, JSONArray jsonArray) {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "44");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID() + "");
            jsonObject.put("token", BaseApplication.getInstance().getToken() + "");
            jsonObject.put("actionid", actionid);
            jsonObject.put("productlist", jsonArray);
            params.put("sendmsg", jsonObject.toString());
            editInfo(actionid, params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void editInfo(String actionid, Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<AddCartRespMsg>(getActivity()) {
            @Override
            public void onSuccess(Response response, AddCartRespMsg addCartRespMsg) {
                if (addCartRespMsg.getResult() > 0) {
                    if ("1".equals(addCartRespMsg.getList().get(0).getIssuccess())) {
                        if ("2".equals(actionid)) {
                            ToastUtils.show(getActivity(), "修改成功");
                        } else if ("3".equals(actionid)) {
                            List<Integer> deletePosition = new ArrayList<Integer>();
                            for (int i = cartBeanList.size() - 1; i > -1; i--) {
                                if (cartBeanList.get(i).isCheck()) {
                                    deletePosition.add(i);
                                }
                            }
                            for (Integer integer : deletePosition) {
                                CartBean cartBean = cartBeanList.get(integer);
                                CartProductBean cartProductBean = cartProductList.get(integer);
                                cartBeanList.remove(cartBean);
                                cartProductList.remove(cartProductBean);
                            }
                            shopCartAdapter.cartBeanList = cartBeanList;
                            shopCartAdapter.cartProductBeen = cartProductList;
                            shopCartAdapter.notifyDataSetChanged();
                            if (null == cartProductList || 0 == cartProductList.size()) {
                                lin_empty.setVisibility(View.VISIBLE);
                                xRecyclerView.setVisibility(View.GONE);
                                rel_bottom.setVisibility(View.GONE);
                                text_right.setVisibility(View.GONE);
//                                isEdit = isEdit;
                                shopCartAdapter.isEdit = isEdit;
                            }
                            ToastUtils.show(getActivity(), "删除成功");

                        }


                    } else {
                        ToastUtils.show(getActivity(), "修改失败");
                    }
                } else {
                    ToastUtils.show(getActivity(), addCartRespMsg.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(getActivity(), "修改失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(getActivity(), "修改失败，请稍后重试");

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (null == BaseApplication.getInstance().getUser() || 0 == BaseApplication.getInstance().getUser().getUserID() || "".equals(BaseApplication.getInstance().getToken())) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    if (-1 != Contants.beforePosition) {
                        mainActivity.mTabhost.setCurrentTab(Contants.beforePosition);
                    } else {
                        mainActivity.mTabhost.setCurrentTab(0);

                    }
                }else {
                    isRefreshData = true;
                }
                break;
        }
    }

}
