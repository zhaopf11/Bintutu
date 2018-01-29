package com.zhurui.bunnymall.viewutils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.http.OkHttpHelper;
import com.zhurui.bunnymall.common.http.SpotsCallBack;
import com.zhurui.bunnymall.home.activity.ProductGridNormalActivity;
import com.zhurui.bunnymall.home.bean.CityBean;
import com.zhurui.bunnymall.home.bean.PriceAreaBean;
import com.zhurui.bunnymall.home.bean.PropertyChildBean;
import com.zhurui.bunnymall.home.bean.PropertyGroupBean;
import com.zhurui.bunnymall.home.bean.ScreenObject;
import com.zhurui.bunnymall.home.msg.ScreenResp;
import com.zhurui.bunnymall.mine.bean.User;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.utils.PermissionsActivity;
import com.zhurui.bunnymall.utils.PermissionsChecker;
import com.zhurui.bunnymall.utils.ToastUtils;
import com.zhurui.bunnymall.utils.UserLocalData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Response;

/**
 * Created by zhoux on 2017/8/11.
 */

public class ScreenDialog extends Dialog {
    private View view;
    private Context context;
    private TextView text_shanghai;

    private TextView text_location;

    private FlowRadioGroup flow_address;
    private CheckBox checkbox_address;

    private LinearLayout lin_price;
    private EditText edit_low;
    private EditText edit_high;
    private FlowRadioGroup flow_price;

    private ScreenAdapter screenAdapter;
    private MyExpandableListView expandable;
    public static Map<Integer, String> checkMap = new HashMap<Integer, String>();
    public static Map<Integer, Integer> childCheckMap = new HashMap<Integer, Integer>();
    private Map<String, List<PropertyChildBean>> propertyMap = new HashMap<>();
    private Button btn_reset;
    private Button btn_submit;
    private int addressCheckPosition = -1;
    public OkHttpHelper mHttpHelper;
    public LocationClient mLocationClient = null;
    public BDAbstractLocationListener myListener = new MyLocationListener();
    private boolean isFirstLocation = false;
    private ScreenObject screenObject = null;
    private JSONArray jsonArray;
    private OnSubMit onSubMit;
    private boolean isCustomezed;

    private JSONObject cityObject;
    private JSONObject priceObject;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    public static String[] strings ;
    public static String radioString;

    public ScreenDialog(@NonNull Context context, @StyleRes int themeResId, boolean isCustomezed) {
        super(context, themeResId);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.dialog_screen, null);
        this.context = context;
        this.isCustomezed = isCustomezed;
        mLocationClient = new LocationClient(context);
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        mHttpHelper = new OkHttpHelper();
        cityObject = new JSONObject();
        priceObject = new JSONObject();
        mPermissionsChecker = new PermissionsChecker(context);

        initView();
        setView();
        initLocation();
    }

    private void initView() {

        expandable = (MyExpandableListView) view.findViewById(R.id.expandable);
        screenAdapter = new ScreenAdapter(context);
        screenAdapter.setOnItemClick(new ScreenAdapter.OnItemClick() {
            @Override
            public void groupClick(boolean isChecked, int groupposition) {
                if (isChecked) {
                    expandable.expandGroup(groupposition);
                } else {
                    expandable.collapseGroup(groupposition);
                }
            }

            @Override
            public void childClick(int groupPosition, String string, int childPosition) {
                checkMap.put(groupPosition, string);
                screenAdapter.checkMap = checkMap;
                childCheckMap.put(groupPosition, childPosition);
                screenAdapter.notifyDataSetChanged();


            }
        });
        expandable.setAdapter(screenAdapter);
        text_shanghai = (TextView) view.findViewById(R.id.text_shanghai);
        text_location = (TextView) view.findViewById(R.id.text_location);
        flow_address = (FlowRadioGroup) view.findViewById(R.id.flow_address);
        checkbox_address = (CheckBox) view.findViewById(R.id.checkbox_address);
        lin_price = (LinearLayout) view.findViewById(R.id.lin_price);
        if (isCustomezed) {
            lin_price.setVisibility(View.GONE);
        }

        flow_price = (FlowRadioGroup) view.findViewById(R.id.flow_price);
        edit_low = (EditText) view.findViewById(R.id.edit_low);
        edit_high = (EditText) view.findViewById(R.id.edit_high);

        btn_reset = (Button) view.findViewById(R.id.btn_reset);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        edit_low.setFocusable(true);
        edit_low.setFocusableInTouchMode(true);
        edit_low.requestFocus();
        if (strings != null && strings.length == 2) {
            edit_low.setText(strings[0]);
            edit_high.setText(strings[1]);
        } else if (strings != null && strings.length == 1) {
            edit_low.setText("");
            edit_high.setText("");
        }
        jsonArray = new JSONArray();
        text_shanghai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"定位失败".equals(text_shanghai.getText().toString().trim())) {
                    checkbox_address.setText(text_shanghai.getText().toString().trim());
                }
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childCheckMap = new ArrayMap<Integer, Integer>();
                checkMap = new ArrayMap<Integer, String>();
                screenAdapter.checkMap = checkMap;
                screenAdapter.notifyDataSetChanged();
                edit_high.setText("");
                edit_low.setText("");
                if (-1 != addressCheckPosition) {
                    RadioButton radioButton = (RadioButton) flow_address.getChildAt(addressCheckPosition);
                    radioButton.setChecked(false);
                    addressCheckPosition = -1;
                    checkbox_address.setText("");
                }
                cityObject = new JSONObject();
                priceObject = new JSONObject();
                jsonArray = new JSONArray();
                radioString = "";
                if (screenObject != null && null != screenObject.getPricearealist() && screenObject.getPricearealist().size() > 0) {
                    flow_price.removeAllViews();
                    for (int a = 0; a < screenObject.getPricearealist().size(); a++) {
                        PriceAreaBean priceAreaBean = screenObject.getPricearealist().get(a);
                        RadioButton button = (RadioButton) LayoutInflater.from(context).inflate(R.layout.radiobutton_screen, null);
                        button.setWidth((int) context.getResources().getDimension(R.dimen.ui_170));
                        button.setHeight((int) context.getResources().getDimension(R.dimen.margin_60));
                        button.setText(priceAreaBean.getC_priceAreaName());
                        flow_price.addView(button, a);
                    }
                }
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lowstr = edit_low.getText().toString().trim();
                String highstr = edit_high.getText().toString().trim();
                strings = new String[]{lowstr,highstr};
                if ((null == lowstr || "".equals(lowstr)) && (null != highstr && !"".equals(highstr))) {
                    lowstr = "100";
                } else if ((null != lowstr && !"".equals(lowstr)) && (null == highstr || "".equals(highstr))) {
                    highstr = "100000";
                }
                if (!TextUtils.isEmpty(lowstr) && !TextUtils.isEmpty(highstr)) {
                    if (!highstr.contains("元")) {
                        highstr = highstr + "元";
                    }
                    try {
                        priceObject.put("key", "price");
                        priceObject.put("value", lowstr + "-" + highstr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (null != cityObject && !"".equals(cityObject) && 0 != cityObject.length()) {
                    jsonArray.put(cityObject);
                }
                if (null != priceObject && !"".equals(priceObject) && 0 != priceObject.length()) {
                    jsonArray.put(priceObject);
                }
                for (Map.Entry<Integer, Integer> entry : childCheckMap.entrySet()) {
                    PropertyGroupBean propertyGroupBean = screenObject.getPropertieslist().get(entry.getKey());
                    PropertyChildBean propertyChildBean = propertyMap.get(propertyGroupBean.getCustomPropertiesID()).get(entry.getValue());
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("key", propertyChildBean.getCustomPropertiesID());
                        jsonObject.put("value", propertyChildBean.getCustomPropertiesValueID());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                onSubMit.subMit(jsonArray);
                dismiss();

            }
        });
        text_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (mPermissionsChecker.lacksPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {

                        ToastUtils.show(context, "没有权限,请手动开启定位权限");
                    } else {
                        isFirstLocation = true;
                        if (mLocationClient != null && mLocationClient.isStarted()) {
                            mLocationClient.requestLocation();
                        } else {
                            mLocationClient.start();
                        }
                    }
                }
            }
        });

        checkbox_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    flow_address.setVisibility(View.VISIBLE);
                } else {
                    flow_address.setVisibility(View.GONE);

                }
            }
        });

        flow_address.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) group.getChildAt(i);
                    if (checkedId == radioButton.getId()) {
                        checkbox_address.setText(radioButton.getText());
                        try {
                            cityObject.put("key", "city");
                            cityObject.put("value", radioButton.getText().toString().trim());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        addressCheckPosition = i;
                    }
                }
            }
        });


        flow_price.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) group.getChildAt(i);

                    if (checkedId == radioButton.getId()) {
                        radioString = radioButton.getText().toString();
                        strings = radioButton.getText().toString().split("-");
                        if (strings != null && strings.length == 2) {
                            edit_low.setText(strings[0]);
                            edit_high.setText(strings[1]);
                        } else if (strings != null && strings.length == 1) {
                            edit_low.setText("");
                            edit_high.setText("");
                        }

                        if (null != screenObject.getPricearealist() && screenObject.getPricearealist().size() > 0) {
                            flow_price.removeAllViews();
                            for (int a = 0; a < screenObject.getPricearealist().size(); a++) {
                                PriceAreaBean priceAreaBean = screenObject.getPricearealist().get(a);
                                RadioButton button = (RadioButton) LayoutInflater.from(context).inflate(R.layout.radiobutton_screen, null);
                                button.setWidth((int) context.getResources().getDimension(R.dimen.ui_170));
                                button.setHeight((int) context.getResources().getDimension(R.dimen.margin_60));
                                button.setText(priceAreaBean.getC_priceAreaName());
                                //判断是否选中过
                                if (priceAreaBean != null && !TextUtils.isEmpty(radioString) && priceAreaBean.getC_priceAreaName().equals(radioString)) {
                                    button.setChecked(true);
                                }
                                flow_price.addView(button, a);
                            }
                        }
                    }
                }
            }
        });

        ScreenObject screenObject = UserLocalData.getScreeObj(BaseApplication.getInstance());
        if (null != screenObject.getPricearealist() && screenObject.getPricearealist().size() > 0) {
            this.screenObject = screenObject;
            initUI();
        } else {
            initProperty();
        }

    }


    public interface OnSubMit {
        void subMit(JSONArray jsonArray);
    }

    public void setOnSubMit(OnSubMit onSubMit) {
        this.onSubMit = onSubMit;
    }

    private void initProperty() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "39");
            jsonObject.put("uid", BaseApplication.getInstance().getUser().getUserID());
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("curretadress", "上海");
            jsonObject.put("propertiesID", "1");
            params.put("sendmsg", jsonObject.toString());
            getProperty(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getProperty(Map<String, Object> params) {
        mHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<ScreenResp>(context) {
            @Override
            public void onSuccess(Response response, ScreenResp screenResp) {
                if (screenResp.getResult() > 0) {
                    if (null != screenResp.getList() && screenResp.getList().size() > 0) {
                        screenObject = screenResp.getList().get(0);
                        initUI();
                    }
                } else {
                    ToastUtils.show(context, screenResp.getRetmsg());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(context, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(context, "请求失败，请稍后重试");

            }
        });
    }


    private void initUI() {
        if (null != screenObject.getPropertieslist() && screenObject.getPropertieslist().size() > 0) {
            for (PropertyGroupBean propertyGroupBean : screenObject.getPropertieslist()) {
                List<PropertyChildBean> childBeanList = new ArrayList<PropertyChildBean>();
                propertyMap.put(propertyGroupBean.getCustomPropertiesID(), childBeanList);
            }

            if (null != screenObject.getPropertiesvaluelist() && screenObject.getPropertiesvaluelist().size() > 0) {
                for (PropertyChildBean propertyChildBean : screenObject.getPropertiesvaluelist()) {
                    if (propertyMap.containsKey(propertyChildBean.getCustomPropertiesID())) {
                        propertyMap.get(propertyChildBean.getCustomPropertiesID()).add(propertyChildBean);
                    }
                }
            }
            screenAdapter.propertyGroupBeen = screenObject.getPropertieslist();
            screenAdapter.propertyChildMap = propertyMap;
            screenAdapter.notifyDataSetChanged();

        }
        if (null != screenObject.getCitylist() && screenObject.getCitylist().size() > 0) {
            for (int i = 0; i < screenObject.getCitylist().size(); i++) {
                CityBean cityBean = screenObject.getCitylist().get(i);
                RadioButton button = (RadioButton) LayoutInflater.from(context).inflate(R.layout.radiobutton_screen, null);
                button.setWidth((int) context.getResources().getDimension(R.dimen.ui_170));
                button.setHeight((int) context.getResources().getDimension(R.dimen.margin_60));
                button.setText(cityBean.getName());
                flow_address.addView(button, i);
            }
        }
        if (null != screenObject.getPricearealist() && screenObject.getPricearealist().size() > 0) {
            for (int i = 0; i < screenObject.getPricearealist().size(); i++) {
                PriceAreaBean priceAreaBean = screenObject.getPricearealist().get(i);
                RadioButton button = (RadioButton) LayoutInflater.from(context).inflate(R.layout.radiobutton_screen, null);
                button.setWidth((int) context.getResources().getDimension(R.dimen.ui_170));
                button.setHeight((int) context.getResources().getDimension(R.dimen.margin_60));
                button.setText(priceAreaBean.getC_priceAreaName());
                //判断是否选中过
                if (priceAreaBean != null && !TextUtils.isEmpty(radioString) && priceAreaBean.getC_priceAreaName().equals(radioString)) {
                    button.setChecked(true);
                }
                flow_price.addView(button, i);
            }
        }
    }


    private boolean isOpenGps() {
        LocationManager locationManager = (LocationManager) context.
                getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    public void upGroupData() {

    }

    @Override
    public void dismiss() {
        super.dismiss();
        mLocationClient.stop();
    }

    private void setView() {
        view.setAlpha(1.0f);
        this.setContentView(view);
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.RIGHT);
//        dialogWindow.setWindowAnimations(R.style.AnimRight);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) context.getResources().getDisplayMetrics().widthPixels; // 宽度
        dialogWindow.setAttributes(lp);
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int left = view.findViewById(R.id.scroll_screen).getLeft();
                int y = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < left) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    private void initLocation() {

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死


        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要


        mLocationClient.setLocOption(option);
    }


    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null == location) {
                //定位失败
                text_shanghai.setText("定位失败");
            } else {
                if (isFirstLocation) {
                    String cityName = location.getCity();
                    //获取城市信息
                    if (null != cityName && !"".equals(cityName)) {
                        text_shanghai.setText(cityName);
                    } else {
                        text_shanghai.setText("定位失败");
                    }
                    //设置定位城市显示
                    isFirstLocation = false;
                }


            }

        }

    }

}
