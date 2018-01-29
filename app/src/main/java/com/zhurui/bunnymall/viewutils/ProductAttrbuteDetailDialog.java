package com.zhurui.bunnymall.viewutils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.OkHttpHelper;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.home.activity.WebViewActivity;
import com.zhurui.bunnymall.home.bean.CustomGroupProperty;
import com.zhurui.bunnymall.home.bean.CustomeChildPropertyBean;
import com.zhurui.bunnymall.home.bean.FootTypeBean;
import com.zhurui.bunnymall.home.bean.ProductDetailBean;
import com.zhurui.bunnymall.home.bean.PropertyChildBean;
import com.zhurui.bunnymall.utils.BintutuUtils;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.viewutils.bean.CheckMaterialSortBean;
import com.zhurui.bunnymall.viewutils.bean.ProductAttrbuteRespMsg;
import com.zhurui.bunnymall.viewutils.bean.ProductAttrbuteSxstr;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhaopf on 2017/11/2
 */

public class ProductAttrbuteDetailDialog extends Dialog implements View.OnClickListener {

    private View view;
    private Context context;

    private ImageLoader imageLoader;
    private int num = 1;
    private String maxBuyNumber;
    private OnClickDialog onClickDialog;
    private List<FootTypeBean> footTypeList;
    private ProductAttrbuteDetailDialogAdapter productAttrbuteDetailDialogAdapter;
    private OkHttpHelper mHttpHelper = new OkHttpHelper();
    private ProductDetailBean productDetailBean;
    public static Map<Integer, Integer> childCheckMap = new HashMap<Integer, Integer>();
    private Map<String, String> propertyinfo = new HashMap<>();
    private Map<String, String> propertyname = new HashMap<>();
    private Map<String, String> allMap = new LinkedHashMap<>();
    private CustomeChildPropertyBean customeChildPropertyBean;
    private CustomGroupProperty customGroupProperty;
    public static List<CustomeChildPropertyBean> checkData = new ArrayList<>();
    private List<CustomeChildPropertyBean> tempChimaCheckData = new ArrayList<>();
    public static float tempPrice = 0;
    public static List<String[]> priceList;
    private List<CustomeChildPropertyBean> chimaList = new ArrayList<>();
    private List<CustomGroupProperty> customGroupProperties;

    //    @Bind(R.id.text_footdata_description)
//    TextView text_footdata_description;
    @Bind(R.id.text_product_name)
    TextView text_product_name;
    @Bind(R.id.text_product_price)
    TextView text_product_price;
    @Bind(R.id.btn_submit)
    Button btn_submit;
    @Bind(R.id.rlt_img_close)
    RelativeLayout rlt_img_close;
    @Bind(R.id.lin_foottype)
    LinearLayout lin_foottype;
    @Bind(R.id.img_product)
    NetworkImageView img_product;
    @Bind(R.id.amountView)
    AmountView amountView;

    @Bind(R.id.attr_expandableview)
    MyExpandableListView attr_expandableview;
    @Bind(R.id.size_foot_data)
    FlowRadioGroup size_foot_data;//量脚尺码
    String size = "";//获取足型数据里包含的尺码

    @Bind(R.id.rule_view)
    RuleView rule_view;
    @Bind(R.id.hor_scrollview)
    MySelfHorizontalScrollView myHor_scrollview;

    //选定属性
    @Bind(R.id.lin_check_material)
    LinearLayout lin_check_material;
    @Bind(R.id.check_material_text)
    TextView check_material_text;

    @Bind(R.id.lin_personality_request)
    LinearLayout lin_personality_request;
    @Bind(R.id.personality_request_text)
    TextView personality_request_text;

    @Bind(R.id.lin_customize_size)
    LinearLayout lin_customize_size;
    @Bind(R.id.customize_size_text)
    TextView customize_size_text;

    @Bind(R.id.lin_foot_desc)
    LinearLayout lin_foot_desc;
    @Bind(R.id.foot_desc_text)
    TextView foot_desc_text;


    @Bind(R.id.material_gridview)
    GridView material_gridview;
    @Bind(R.id.gexing_gridview)
    GridView gexing_gridview;
    CheckMaterialAdapter  checkMaterialAdapter;
    CheckGeXingAdapter checkGeXingAdapter;
    List<CheckMaterialSortBean> checkMaterialList = new ArrayList<>();

    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public ProductAttrbuteDetailDialog(@NonNull Context context, @StyleRes int themeResId, List<FootTypeBean> footTypeBeen, ProductDetailBean productDetailBean, String maxBuyNumber, boolean isNeedLogin, String type) {
        super(context, themeResId);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.dialog_product_attribute, null);
        ButterKnife.bind(this, view);
        if ("1".equals(type)) {
            btn_submit.setText("加入购物袋");
        } else {
            btn_submit.setText("确认定制");
        }
        this.context = context;
        this.imageLoader = BaseApplication.getInstance().getImageLoader();
        this.productDetailBean = productDetailBean;
        this.maxBuyNumber = maxBuyNumber;
        this.footTypeList = footTypeBeen;
        tempPrice = 0;
        checkData = new ArrayList<>();
        requestData();
        initView();
        initData();
        setView();
    }

    /**
     * 请求商品定制数据
     */
    private void requestData() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "76");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("productid", productDetailBean.getProductID());
            params.put("sendmsg", jsonObject.toString());
            getMaterial(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        if (null != maxBuyNumber && !"".equals(maxBuyNumber)) {
            amountView.setGoods_storage(Integer.parseInt(maxBuyNumber));
        }
        amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                num = amount;
                text_product_price.setText("" + decimalFormat.format(num * tempPrice));
            }

            @Override
            public void toMax() {
                ToastUtils.show(context, "超过最大购买量");
            }
        });
    }

    private void initData() {
        if (productDetailBean != null) {
            img_product.setImageUrl(Contants.BASE_IMGURL + productDetailBean.getMainImage(), imageLoader);
            img_product.setErrorImageResId(R.drawable.new_recommand_normal);
            img_product.setDefaultImageResId(R.drawable.new_recommand_normal);
            text_product_price.setText("" + productDetailBean.getPrice());
            text_product_name.setText("（" + productDetailBean.getName() + "）");
            tempPrice = Float.parseFloat(productDetailBean.getPrice());
        }
        //初始化adapter 展示选定材料和个性要求的适配器
        checkMaterialAdapter =new CheckMaterialAdapter(context,checkMaterialList);
        material_gridview.setAdapter(checkMaterialAdapter);

        checkGeXingAdapter = new CheckGeXingAdapter(context,checkMaterialList);
        gexing_gridview.setAdapter(checkGeXingAdapter);

        productAttrbuteDetailDialogAdapter = new ProductAttrbuteDetailDialogAdapter(context);
        attr_expandableview.setAdapter(productAttrbuteDetailDialogAdapter);
        attr_expandableview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;//返回true表示父项不可点击
            }
        });
        productAttrbuteDetailDialogAdapter.setOnItemClick(new ProductAttrbuteDetailDialogAdapter.OnItemClick() {
            @Override
            public void groupClick(int groupPosition, int childPosition, List<CustomGroupProperty> propertyGroupList, List<CustomeChildPropertyBean> propertyChildList) {
                if (propertyChildList != null && propertyGroupList != null) {
                    customeChildPropertyBean = propertyChildList.get(childPosition);
                    customGroupProperty = propertyGroupList.get(groupPosition);
                    String propertyInfo = customeChildPropertyBean.getCustomPropertiesID() + ":" + customeChildPropertyBean.getCustomPropertiesValueID();
                    String propertyName = customGroupProperty.getName() + ":" + customeChildPropertyBean.getName();
                    propertyinfo.put(propertyGroupList.get(groupPosition).getCustomPropertiesID(), propertyInfo);
                    propertyname.put(propertyGroupList.get(groupPosition).getCustomPropertiesID(), propertyName);
                    moveRepeatData(customeChildPropertyBean);
                    //展示已选择的定制属性
                    showCheckAttrData();
                }
            }
        });

        //展示选择的属性
        showCheckAttrData();
        //展示量脚尺码数据
        if (null != footTypeList && footTypeList.size() > 0) {
            for (int i = 0; i < footTypeList.size(); i++) {
                RadioButton footbutton = (RadioButton) LayoutInflater.from(context).inflate(R.layout.adapter_item_attrbtn, null);
                footbutton.setText(footTypeList.get(i).getName() + "的量脚数据");
                footbutton.setHeight((int) context.getResources().getDimension(R.dimen.margin_60));
                footbutton.setTag(footTypeList.get(i));
                size_foot_data.addView(footbutton, i);
            }
        }
        size_foot_data.setOnCheckedChangeListener(onCheckedChangeListener);
        btn_submit.setOnClickListener(this);
    }


    private void showRuleView() {

        myHor_scrollview.setOverScrollMode(View.OVER_SCROLL_NEVER);// 去掉超出滑动后出现的阴影效果
        // 设置水平滑动
        rule_view.setHorizontalScrollView(myHor_scrollview);
        //设置尺子的默认值
        rule_view.setDefaultScaleValue(0);
        // 当滑动尺子的时候
        myHor_scrollview.setOnScrollListener(new MySelfHorizontalScrollView.OnScrollListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                rule_view.setScrollerChanaged(l, t, oldl, oldt);
            }
        });
        rule_view.refreshDataView(chimaList);
        final DecimalFormat format = new DecimalFormat("0.0");
        rule_view.onChangedListener(new RuleView.onChangedListener() {
            @Override
            public void onSlide(CustomeChildPropertyBean childBean) {
                //过滤尺码重复数据
                for (Iterator<CustomeChildPropertyBean> iter = checkData.iterator(); iter.hasNext(); ) {
                    CustomeChildPropertyBean child = iter.next();
                    if (child.equals(childBean)) {
                        if (priceList != null && priceList.size() > 0) {
                            for (int a = 0; a < priceList.size(); a++) {
                                if ((priceList.get(a)[0] + "").equals(child.getCustomPropertiesValueID())) {
                                    tempPrice = tempPrice - Float.parseFloat(priceList.get(a)[1]);
                                }
                            }
                        }
                        iter.remove();
                    }
                }
                checkData.add(childBean);
                if (priceList != null && priceList.size() > 0) {
                    for (int a = 0; a < priceList.size(); a++) {
                        if ((priceList.get(a)[0] + "").equals(childBean.getCustomPropertiesValueID())) {
                            tempPrice = tempPrice + Float.parseFloat(priceList.get(a)[1]);
                        }
                    }
                }
                //移除不同尺码数据
                moveRepeatData(childBean);
                showCheckAttrData();
            }
        });
    }

    /**
     * 展示已选择的定制属性
     */
    private void showCheckAttrData() {
        lin_foottype.setVisibility(View.VISIBLE);
        boolean isCheckColor = false;
        for (int i = 0; i < checkData.size(); i++) {
            //当为颜色的时候，显示该颜色的图片
            String customPropertiesID = checkData.get(i).getCustomPropertiesID();
            if ("1001".equals(customPropertiesID) || "17242".equals(customPropertiesID)) {
                isCheckColor = true;
                img_product.setImageUrl(Contants.BASE_IMGURL + checkData.get(i).getCustomPropertiesValueImageUrl(), imageLoader);
            }
        }
        showAttrbute();
        if (!isCheckColor) {
            img_product.setImageUrl(Contants.BASE_IMGURL + productDetailBean.getMainImage(), imageLoader);
        }
        text_product_price.setText("" + decimalFormat.format(num * tempPrice));
    }

    //根据选定的属性进行分类展示
    private void showAttrbute() {
        String checkmaterial = "";
        String personalityrequest = "";
        String customizesize = "";
        List<CheckMaterialSortBean> checkMaterialList = new ArrayList<>();//选定材料list
        List<CheckMaterialSortBean> checkGeXingList = new ArrayList<>();//选定个性要求list

        if (checkData != null && checkData.size() > 0) {
            for (int a = 0; a < checkData.size(); a++) {
                String customPropertiesID = checkData.get(a).getCustomPropertiesID();
                String checkName = checkData.get(a).getName() + "  ";
                String isfoot = checkData.get(a).getIsFootData();
                CheckMaterialSortBean checkMaterialSortBean = new CheckMaterialSortBean();
                if ("17239".equals(customPropertiesID) || "1000".equals(customPropertiesID)) {//皮料定制
                    checkmaterial = checkmaterial + checkName;
                    //重新设置对象，设置排序字段
                    checkMaterialSortBean.setName(checkName);
                    checkMaterialSortBean.setSortId(1);
                    checkMaterialList.add(checkMaterialSortBean);
                } else if ("17251".equals(customPropertiesID) || "1004".equals(customPropertiesID)) {//底材定制
                    checkMaterialSortBean.setName(checkName);
                    checkMaterialSortBean.setSortId(2);
                    checkMaterialList.add(checkMaterialSortBean);
                    checkmaterial = checkmaterial + checkName;
                } else if ("17237".equals(customPropertiesID) || "1005".equals(customPropertiesID)) {//鞋跟定制
                    checkMaterialSortBean.setName(checkName);
                    checkMaterialSortBean.setSortId(3);
                    checkMaterialList.add(checkMaterialSortBean);
                    checkmaterial = checkmaterial + checkName;
                }

                if ("17252".equals(customPropertiesID) || "1003".equals(customPropertiesID)) {//底配定制
                    checkMaterialSortBean.setName(checkName);
                    checkMaterialSortBean.setSortId(1);
                    checkGeXingList.add(checkMaterialSortBean);
                    personalityrequest = personalityrequest + checkName;
                } else if ("1010".equals(customPropertiesID)) {//个性要求
                    checkMaterialSortBean.setName(checkName);
                    checkMaterialSortBean.setSortId(2);
                    checkGeXingList.add(checkMaterialSortBean);
                    personalityrequest = personalityrequest + checkName;
                }


                if ("1".equals(isfoot)) {//如果量脚数据和尺码匹配，显示为根据 谁谁的量脚数据定制
                    customizesize = checkName;
                } else if ("17250".equals(customPropertiesID) || "1011".equals(customPropertiesID)) {//尺码
                    customizesize = customizesize + checkData.get(a).getName() + "码  ";
                    if (!size.equals(checkData.get(a).getName())) {
                        //如果量脚数据和尺码不匹配，显示为根据 标准尺码 定制
                        customizesize = "标准尺码" + checkData.get(a).getName() + "码  ";
                    }
                }
            }

            if (!TextUtils.isEmpty(checkmaterial)) {
                lin_check_material.setVisibility(View.VISIBLE);
                check_material_text.setText(checkmaterial);
                BintutuUtils.sortList(checkMaterialList);//排序从小到大
                checkMaterialAdapter.checkMaterialList = checkMaterialList;
                checkMaterialAdapter.notifyDataSetChanged();
            } else {
                lin_check_material.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(personalityrequest)) {
                lin_personality_request.setVisibility(View.VISIBLE);
                personality_request_text.setText(personalityrequest);
                BintutuUtils.sortList(checkGeXingList);//排序从小到大
                checkGeXingAdapter.checkGeXingList = checkGeXingList;
                checkGeXingAdapter.notifyDataSetChanged();
            } else {
                lin_personality_request.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(customizesize)) {
                lin_customize_size.setVisibility(View.VISIBLE);
                customize_size_text.setText(customizesize);
            } else {
                lin_customize_size.setVisibility(View.GONE);
            }
        }else{
            lin_check_material.setVisibility(View.GONE);
            lin_personality_request.setVisibility(View.GONE);
            lin_customize_size.setVisibility(View.GONE);
            checkmaterial = "";
            personalityrequest = "";
            customizesize = "";
        }
    }


    private void getMaterial(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<ProductAttrbuteRespMsg>(context) {
            @Override
            public void onSuccess(Response response, ProductAttrbuteRespMsg productAttrbuteRespMsg) {
                if (productAttrbuteRespMsg.getResult() > 0) {
                    if (productAttrbuteRespMsg != null && productAttrbuteRespMsg.getList() != null) {
                        ProductAttrbuteSxstr productAttrbuteSxstr = productAttrbuteRespMsg.getList().getSxstr();
                        if (productAttrbuteSxstr != null) {
                            priceList = productAttrbuteSxstr.getShuxingPriceList();
                            chimaList = productAttrbuteSxstr.getChimaList();

                            //拼装数据
                            customGroupProperties = assembleGroupData(productAttrbuteSxstr);
                            Map<String, List<CustomeChildPropertyBean>> map = assembleMapData(productAttrbuteSxstr);

                            productAttrbuteDetailDialogAdapter.propertyGroupBeen = customGroupProperties;
                            productAttrbuteDetailDialogAdapter.propertyMap = map;
                            if (childCheckMap != null) {
                                childCheckMap.clear();
                            }
                            productAttrbuteDetailDialogAdapter.notifyDataSetChanged();
                            for (int i = 0; i < customGroupProperties.size(); i++) {
                                attr_expandableview.expandGroup(i);
                            }
                            //展示标准尺码 尺子初始化
                            showRuleView();
                        }
                    }
                } else {
                    ToastUtils.show(context, "请求失败，请稍后重试");
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
//                ToastUtils.show(context, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
//                ToastUtils.show(context, "请求失败，请稍后重试");

            }
        });
    }


    /**
     * 关闭界面
     *
     * @param view
     */
    @OnClick(R.id.rlt_img_close)
    public void close(View view) {
        dismiss();
    }

    /**
     * 移除重复数据，并减掉价格
     *
     * @param childPropertyBean
     */
    public void moveRepeatData(CustomeChildPropertyBean childPropertyBean) {
        if (checkData != null && checkData.size() > 0) {
            //过滤重复数据
            for (Iterator<CustomeChildPropertyBean> iter = checkData.iterator(); iter.hasNext(); ) {
                CustomeChildPropertyBean customeChildBean = iter.next();
                if (customeChildBean.getCustomPropertiesID().equals(childPropertyBean.getCustomPropertiesID())
                        && !childPropertyBean.getName().equals(customeChildBean.getName())) {
                    // 底配定制是多选，其他定制为单选。
                    // 当为底配定制的时候，不过滤相同ID下的属性。
                    // 当为其他定制的时候，过滤相同ID下的属性，并减掉相应的价格
                    if (!"1003".equals(customeChildBean.getCustomPropertiesID()) && !"17252".equals(customeChildBean.getCustomPropertiesID())) {
                        if (priceList != null && priceList.size() > 0) {
                            for (int a = 0; a < priceList.size(); a++) {
                                if ((priceList.get(a)[0] + "").equals(customeChildBean.getCustomPropertiesValueID())) {
                                    tempPrice = tempPrice - Float.parseFloat(priceList.get(a)[1]);
                                }
                            }
                        }
                        iter.remove();
                    }
                }
            }
        }
    }

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) group.getChildAt(i);

                int checkedSize = 0;//根据足型数据获得相应的尺码
                if (radioButton.getId() == checkedId) {
                    String dataName = radioButton.getText() + "";
                    FootTypeBean footBean = (FootTypeBean) radioButton.getTag();
                    String dataId = footBean.getUserFootTypeDataID();
                    CustomeChildPropertyBean customeChildPropertyBean = new CustomeChildPropertyBean();
                    customeChildPropertyBean.setIsFootData("1");//  "1"为足型数据属性，
                    customeChildPropertyBean.setName(dataName);
                    customeChildPropertyBean.setCustomPropertiesID(dataId);
                    if (null != footTypeList && footTypeList.size() > 0) {
                        for (int j = 0; j < footTypeList.size(); j++) {
                            if (dataId.equals(footTypeList.get(j).getUserFootTypeDataID())) {
                                size = footTypeList.get(j).getShoeSize();
                            }
                        }
                    }
                    if (chimaList != null && chimaList.size() > 0) {
                        for (int b = 0; b < chimaList.size(); b++) {
                            if (size.equals(chimaList.get(b).getName())) {
                                checkedSize = b + 1;
                            }
                        }
                    }

                    checkData.add(customeChildPropertyBean);
                    if (checkData != null && checkData.size() > 0) {
                        //过滤不同的足型数据数据
                        for (Iterator<CustomeChildPropertyBean> iter = checkData.iterator(); iter.hasNext(); ) {
                            CustomeChildPropertyBean customeChildBean = iter.next();
                            if ("1".equals(customeChildBean.getIsFootData()) && !dataId.equals(customeChildBean.getCustomPropertiesID())) {
                                iter.remove();
                            }
                        }
                    }
                    if (checkedSize == 0) {
                        ToastUtils.show(BaseApplication.getInstance(), "足型数据尺码与标准尺码匹配不上，请手动选择标准尺码");
                        //不匹配的情况下移除尺码数据
                        for (Iterator<CustomeChildPropertyBean> iter = checkData.iterator(); iter.hasNext(); ) {
                            CustomeChildPropertyBean customeChildBean = iter.next();
                            if ("1011".equals(customeChildBean.getCustomPropertiesID()) || "17250".equals(customeChildBean.getCustomPropertiesID())) {
                                if (priceList != null && priceList.size() > 0) {
                                    for (int a = 0; a < priceList.size(); a++) {
                                        if ((priceList.get(a)[0] + "").equals(customeChildBean.getCustomPropertiesValueID())) {
                                            tempPrice = tempPrice - Float.parseFloat(priceList.get(a)[1]);
                                        }
                                    }
                                }
                                iter.remove();
                            }
                        }
                        //展示已选择的定制属性
                        showCheckAttrData();
                    }
                    rule_view.setDefaultScaleValue(checkedSize);
                    if (!TextUtils.isEmpty(footBean.getFootDesc())) {
                        lin_foot_desc.setVisibility(View.VISIBLE);
                        foot_desc_text.setText(footBean.getFootDesc() + " ");
                    } else {
                        lin_foot_desc.setVisibility(View.GONE);
                    }
                }
            }
        }
    };


    private void setView() {
        view.setAlpha(1.0f);
        this.setContentView(view);
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) context.getResources().getDisplayMetrics().widthPixels; // 宽度
        dialogWindow.setAttributes(lp);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    /**
     * 开始定制
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        String priceTotal = tempPrice + "";
        String footTypeId = "";
        String infoId = "";
        String infoName = "";
        List<String> list = new ArrayList<>();

        if (checkData != null && checkData.size() > 0) {
            for (int i = 0; i < checkData.size(); i++) {
                String checkedid = checkData.get(i).getCustomPropertiesID();
                String checkedvalueid = checkData.get(i).getCustomPropertiesValueID();
                list.add(checkedid);
                if ("1".equals(checkData.get(i).getIsFootData())) {
                    footTypeId = checkData.get(i).getCustomPropertiesID();
                } else {
                    String tempriceid = "0";
                    String priceid = "0";
                    String price = "0.0";
                    if (priceList != null && priceList.size() > 0) {
                        for (int a = 0; a < priceList.size(); a++) {
                            tempriceid = priceList.get(a)[0] + "";
                            if (checkedvalueid.equals(tempriceid)) {
                                price = priceList.get(a)[1] + "";
                                priceid = tempriceid;
                            }
                        }
                    }
                    String groupName = allMap.get(checkedid);
                    infoId = infoId + checkedid + ":" + checkedvalueid + ":" + price + ",";
                    infoName = infoName + groupName + ":" + checkData.get(i).getName() + ",";
                }
            }
            //判断必选择属性项
            for (Map.Entry<String, String> vo : allMap.entrySet()) {
                String id = vo.getKey();
                String value = vo.getValue();
                if (!list.contains(id)) {
                    ToastUtils.show(BaseApplication.getInstance(), "请选择" + value + "属性");
                    return;
                }
            }

            if (!TextUtils.isEmpty(footTypeId)) {
                onClickDialog.subMit(num, infoId, infoName, footTypeId, priceTotal, "");
            } else {
                ToastUtils.show(BaseApplication.getInstance(), "请选择量脚尺码属性");
            }
        } else {
            ToastUtils.show(BaseApplication.getInstance(), "请选择定制属性");
        }
    }


    public interface OnClickDialog {
        void subMit(int amount, String infoId, String infoName, String foottypeid, String priceTotal, String image);
    }

    public void setOnClickDialog(OnClickDialog onClickDialog) {
        this.onClickDialog = onClickDialog;
    }


    private List<CustomGroupProperty> assembleGroupData(ProductAttrbuteSxstr productAttrbuteSxstr) {
        List<CustomGroupProperty> customGroupProperties = new ArrayList<CustomGroupProperty>();
        if (productAttrbuteSxstr.getYanseList() != null && productAttrbuteSxstr.getYanseList().size() > 0) {
            CustomGroupProperty customGroupProperty = new CustomGroupProperty();
            if ("0".equals(productDetailBean.getCustomFlag())) {
                customGroupProperty.setCustomPropertiesID("17242");
                customGroupProperty.setName("配色定制");
            } else {
                customGroupProperty.setCustomPropertiesID("1001");
                customGroupProperty.setName("配色定制");
            }
            customGroupProperties.add(customGroupProperty);
        }

        if (productAttrbuteSxstr.getMianliaoList() != null && productAttrbuteSxstr.getMianliaoList().size() > 0) {
            CustomGroupProperty customGroupProperty1 = new CustomGroupProperty();
            if ("0".equals(productDetailBean.getCustomFlag())) {
                customGroupProperty1.setCustomPropertiesID("17239");
                customGroupProperty1.setName("皮料定制");
            } else {
                customGroupProperty1.setCustomPropertiesID("1000");
                customGroupProperty1.setName("皮料定制");
            }
            customGroupProperties.add(customGroupProperty1);
        }

        if (productAttrbuteSxstr.getDicaiList() != null && productAttrbuteSxstr.getDicaiList().size() > 0) {
            CustomGroupProperty customGroupProperty2 = new CustomGroupProperty();
            if ("0".equals(productDetailBean.getCustomFlag())) {
                customGroupProperty2.setCustomPropertiesID("17251");
                customGroupProperty2.setName("底材定制");
            } else {
                customGroupProperty2.setCustomPropertiesID("1004");
                customGroupProperty2.setName("底材定制");
            }
            customGroupProperties.add(customGroupProperty2);
        }

        if (productAttrbuteSxstr.getDipeiList() != null && productAttrbuteSxstr.getDipeiList().size() > 0) {
            CustomGroupProperty customGroupProperty3 = new CustomGroupProperty();
            if ("0".equals(productDetailBean.getCustomFlag())) {
                customGroupProperty3.setCustomPropertiesID("17252");
                customGroupProperty3.setName("底配定制");
            } else {
                customGroupProperty3.setCustomPropertiesID("1003");
                customGroupProperty3.setName("底配定制");
            }
            customGroupProperties.add(customGroupProperty3);
        }

        if (productAttrbuteSxstr.getXiegenList() != null && productAttrbuteSxstr.getXiegenList().size() > 0) {
            CustomGroupProperty customGroupProperty4 = new CustomGroupProperty();
            if ("0".equals(productDetailBean.getCustomFlag())) {
                customGroupProperty4.setCustomPropertiesID("17237");
                customGroupProperty4.setName("鞋跟定制");
            } else {
                customGroupProperty4.setCustomPropertiesID("1005");
                customGroupProperty4.setName("鞋跟定制");
            }
            customGroupProperties.add(customGroupProperty4);
        }

        if (productAttrbuteSxstr.getGexingList() != null && productAttrbuteSxstr.getGexingList().size() > 0) {
            CustomGroupProperty customGroupProperty5 = new CustomGroupProperty();
            if ("0".equals(productDetailBean.getCustomFlag())) {

            } else {
                customGroupProperty5.setCustomPropertiesID("1010");
                customGroupProperty5.setName("个性定制");
            }
            customGroupProperties.add(customGroupProperty5);
        }
        return customGroupProperties;
    }


    private Map<String, List<CustomeChildPropertyBean>> assembleMapData(ProductAttrbuteSxstr productAttrbuteSxstr) {
        Map<String, List<CustomeChildPropertyBean>> map = new HashMap<String, List<CustomeChildPropertyBean>>();
        if (productAttrbuteSxstr.getChimaList() != null && productAttrbuteSxstr.getChimaList().size() > 0) {
            map.put(productAttrbuteSxstr.getChimaList().get(0).getCustomPropertiesID(), productAttrbuteSxstr.getChimaList());
            allMap.put(productAttrbuteSxstr.getChimaList().get(0).getCustomPropertiesID(), "标准尺码");
        }

        if (productAttrbuteSxstr.getYanseList() != null && productAttrbuteSxstr.getYanseList().size() > 0) {
            map.put(productAttrbuteSxstr.getYanseList().get(0).getCustomPropertiesID(), productAttrbuteSxstr.getYanseList());
            allMap.put(productAttrbuteSxstr.getYanseList().get(0).getCustomPropertiesID(), "配色定制");
        }

        if (productAttrbuteSxstr.getMianliaoList() != null && productAttrbuteSxstr.getMianliaoList().size() > 0) {
            map.put(productAttrbuteSxstr.getMianliaoList().get(0).getCustomPropertiesID(), productAttrbuteSxstr.getMianliaoList());
            allMap.put(productAttrbuteSxstr.getMianliaoList().get(0).getCustomPropertiesID(), "皮料定制");
        }

        if (productAttrbuteSxstr.getDicaiList() != null && productAttrbuteSxstr.getDicaiList().size() > 0) {
            map.put(productAttrbuteSxstr.getDicaiList().get(0).getCustomPropertiesID(), productAttrbuteSxstr.getDicaiList());
            allMap.put(productAttrbuteSxstr.getDicaiList().get(0).getCustomPropertiesID(), "底材定制");
        }

        if (productAttrbuteSxstr.getDipeiList() != null && productAttrbuteSxstr.getDipeiList().size() > 0) {
            map.put(productAttrbuteSxstr.getDipeiList().get(0).getCustomPropertiesID(), productAttrbuteSxstr.getDipeiList());
            allMap.put(productAttrbuteSxstr.getDipeiList().get(0).getCustomPropertiesID(), "底配定制");

        }

        if (productAttrbuteSxstr.getXiegenList() != null && productAttrbuteSxstr.getXiegenList().size() > 0) {
            map.put(productAttrbuteSxstr.getXiegenList().get(0).getCustomPropertiesID(), productAttrbuteSxstr.getXiegenList());
            allMap.put(productAttrbuteSxstr.getXiegenList().get(0).getCustomPropertiesID(), "鞋跟定制");

        }

        if (productAttrbuteSxstr.getGexingList() != null && productAttrbuteSxstr.getGexingList().size() > 0) {
            map.put(productAttrbuteSxstr.getGexingList().get(0).getCustomPropertiesID(), productAttrbuteSxstr.getGexingList());
            allMap.put(productAttrbuteSxstr.getGexingList().get(0).getCustomPropertiesID(), "个性定制");

        }

        return map;
    }
}



