package com.zhurui.bunnymall.viewutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.adapter.GirdDropDownAdapter;

import org.json.JSONArray;

import java.util.Arrays;

import butterknife.OnClick;

/**
 * Created by zhoux on 2017/7/17.
 */

public class MyDropDownMenu extends LinearLayout implements View.OnClickListener {

    private LinearLayout lin_colligate;
    private TextView btn_colligate;
    private Button btn_salesvolume;
    private LinearLayout lin_price;
    private TextView text_price;
    private ImageView img_up;
    private ImageView img_down;
    public Button btn_screen;
    private LinearLayout lin_top;
    private Context context;
    private boolean isColligate = true;
    private boolean isDropDownMenu = false;
    private boolean volume = false;
    private boolean isNewProduct = false;
    public static int price = -1;
    private MyDropDownMenuListDialog dialog;
    private DropDownMenuPopWindow dropDownMenuPopWindow;
    private int smalltypeid = 0;
    private int sortby = 0;
    public static int priceoder = 0;
    private int sellNumber = 0;
    public static boolean isPrice = false;

    private static JSONArray msg = new JSONArray();
    private SubMitInfo subMitInfo;
    private boolean isNew = false;
    public static String checkInfo = "";

    public MyDropDownMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.dropdownmenu_item, this, true);
        this.context = context;
        lin_colligate = (LinearLayout) findViewById(R.id.lin_colligate);
        btn_colligate = (TextView) findViewById(R.id.btn_colligate);
        btn_salesvolume = (Button) findViewById(R.id.btn_salesvolume);
        lin_price = (LinearLayout) findViewById(R.id.lin_price);
        text_price = (TextView) findViewById(R.id.text_price);
        img_up = (ImageView) findViewById(R.id.img_up);
        img_down = (ImageView) findViewById(R.id.img_down);
        btn_screen = (Button) findViewById(R.id.btn_screen);
        lin_top = (LinearLayout) findViewById(R.id.lin_top);
        lin_colligate.setOnClickListener(this);
        btn_salesvolume.setOnClickListener(this);
        lin_price.setOnClickListener(this);
        btn_screen.setOnClickListener(this);
        initData();
    }

    private void initData() {
    }

    /**
     *在下拉或者上滑的时候，显示上次选中的内容，保持两个控件同步显示
     */
    public void refreshDataUi(){
        if(!TextUtils.isEmpty(checkInfo)){//综合
            btn_colligate.setText(checkInfo);
            btn_colligate.setTextColor(getResources().getColor(R.color.color_d2ba91));
            Drawable drawable = getResources().getDrawable(R.drawable.down_checked);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            btn_colligate.setCompoundDrawables(null, null, drawable, null);
            tabInit(btn_colligate);
        }else{
            btn_colligate.setText("综合");
            btn_colligate.setTextColor(getResources().getColor(R.color.gray3));
            Drawable drawable = getResources().getDrawable(R.drawable.down_deep_normal);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            btn_colligate.setCompoundDrawables(null, null, drawable, null);
        }

        if(isPrice){
            btn_colligate.setTextColor(getResources().getColor(R.color.gray3));
            Drawable drawable = getResources().getDrawable(R.drawable.down_deep_normal);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            btn_colligate.setCompoundDrawables(null, null, drawable, null);
            switch (priceoder) {
                case 1:
                    text_price.setTextColor(getResources().getColor(R.color.color_d2ba91));
                    img_up.setImageResource(R.drawable.up_deep_normal);
                    img_down.setImageResource(R.drawable.down_checked);
                    break;
                case 2:
                    text_price.setTextColor(getResources().getColor(R.color.color_d2ba91));
                    img_up.setImageResource(R.drawable.up_checked);
                    img_down.setImageResource(R.drawable.down_deep_normal);
                    break;
            }
        }else{
            text_price.setTextColor(getResources().getColor(R.color.gray3));
            img_up.setImageResource(R.drawable.up_deep_normal);
            img_down.setImageResource(R.drawable.down_deep_normal);
            btn_colligate.setTextColor(getResources().getColor(R.color.color_d2ba91));
            Drawable drawable = getResources().getDrawable(R.drawable.down_checked);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            btn_colligate.setCompoundDrawables(null, null, drawable, null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_colligate:
                isPrice = false;
                price = 1;
                if (!isDropDownMenu) {
                    openDropDownMenu();
                } else {
                    closeDropDownMenu();
                }
                isNew = false;
                smalltypeid = 1;
                tabInit(v);
                break;
            case R.id.btn_salesvolume:
                volume = !volume;
                if (volume) {
                    smalltypeid = 2;
                    sellNumber = 2;
                    btn_salesvolume.setTextColor(getResources().getColor(R.color.color_d2ba91));
                } else {
                    btn_salesvolume.setTextColor(getResources().getColor(R.color.gray3));
                    sellNumber = 0;
                }

                if (isDropDownMenu) {
                    closeDropDownMenu();
                }
                isNew = false;
                setInfo();
                tabInit(v);

                break;
            case R.id.lin_price:
                checkInfo = "";
                isPrice = true;
                isColligate = true;
                switch (price) {
                    case -1:
                        price = 0;
                        text_price.setTextColor(getResources().getColor(R.color.color_d2ba91));
                        img_up.setImageResource(R.drawable.up_checked);
                        priceoder = 2;
                        break;
                    case 0:
                        price = 1;
                        text_price.setTextColor(getResources().getColor(R.color.color_d2ba91));
                        img_up.setImageResource(R.drawable.up_deep_normal);
                        img_down.setImageResource(R.drawable.down_checked);
                        priceoder = 1;
                        break;
                    case 1:
                        price = 0;
                        text_price.setTextColor(getResources().getColor(R.color.color_d2ba91));
                        img_up.setImageResource(R.drawable.up_checked);
                        img_down.setImageResource(R.drawable.down_deep_normal);
                        priceoder = 2;
                        break;

                }
                smalltypeid = 3;
                isNew = false;
                setInfo();
                if (isDropDownMenu) {
                    closeDropDownMenu();
                }
                tabInit(v);
                break;
            case R.id.btn_screen:
                if ("筛选".equals(btn_screen.getText().toString().trim())) {
                    ScreenDialog screenDialog = new ScreenDialog(context, R.style.CustomDialog, false);
                    screenDialog.setOnSubMit(new ScreenDialog.OnSubMit() {
                        @Override
                        public void subMit(JSONArray jsonArray) {
                            msg = jsonArray;
                            smalltypeid = 4;
                            setInfo();
                        }
                    });
                    screenDialog.show();
                } else if ("新品".equals(btn_screen.getText().toString().trim())) {
                    isNewProduct = !isNewProduct;
                    if (isNewProduct) {
                        btn_screen.setTextColor(getResources().getColor(R.color.color_d2ba91));
                        smalltypeid = 1;
                        sortby = 4;
                    } else {
                        btn_screen.setTextColor(getResources().getColor(R.color.gray3));
                        smalltypeid = 1;
                        sortby = 1;
                    }
                    isNew = true;
                    setInfo();
                }
                tabInit(v);
                break;
        }
    }


    private void setInfo() {
        subMitInfo.subMitInfo(smalltypeid, sortby, sellNumber, priceoder, msg, isNew);
    }

    public interface SubMitInfo {
        void subMitInfo(int smalltypeid, int sortby, int sellNumber, int priceoder, JSONArray jsonArray, boolean isNew);
    }

    public void setSubMitInfo(SubMitInfo subMitInfo) {
        this.subMitInfo = subMitInfo;
    }

    //点击某一个控件，其它控件状态的变为默认
    private void tabInit(View view) {
        if (view == lin_colligate) {
            if (volume) {
                volume = false;
                btn_salesvolume.setTextColor(getResources().getColor(R.color.gray3));
            }
            if (price > -1) {
                price = -1;
                text_price.setTextColor(getResources().getColor(R.color.gray3));
                img_up.setImageResource(R.drawable.up_deep_normal);
                img_down.setImageResource(R.drawable.down_deep_normal);

            }
            if(isNewProduct){
                isNewProduct = false;
                btn_screen.setTextColor(getResources().getColor(R.color.gray3));
            }
        } else if (view == btn_salesvolume) {
            if (isColligate) {
                isColligate = false;
                btn_colligate.setTextColor(getResources().getColor(R.color.gray3));
                Drawable drawable = getResources().getDrawable(R.drawable.down_deep_normal);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                btn_colligate.setCompoundDrawables(null, null, drawable, null);
                btn_colligate.setText("综合");
                SharedPreferences pref = BaseApplication.getInstance().getSharedPreferences("ISSELETCED", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("isSeletceNum", 0);
                editor.commit();
            }
            if (price > -1) {
                price = -1;
                text_price.setTextColor(getResources().getColor(R.color.gray3));
                img_up.setImageResource(R.drawable.up_deep_normal);
                img_down.setImageResource(R.drawable.down_deep_normal);
            }
            if(isNewProduct){
                isNewProduct = false;
                btn_screen.setTextColor(getResources().getColor(R.color.gray3));
            }
        } else if (view == lin_price) {
            if (isColligate) {
                isColligate = false;
                btn_colligate.setTextColor(getResources().getColor(R.color.gray3));
                Drawable drawable = getResources().getDrawable(R.drawable.down_deep_normal);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                btn_colligate.setCompoundDrawables(null, null, drawable, null);
                btn_colligate.setText("综合");
                SharedPreferences pref = BaseApplication.getInstance().getSharedPreferences("ISSELETCED", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("isSeletceNum", 0);
                editor.commit();
            }
            if (volume) {
                volume = false;
                btn_salesvolume.setTextColor(getResources().getColor(R.color.gray3));
            }
            if(isNewProduct){
                isNewProduct = false;
                btn_screen.setTextColor(getResources().getColor(R.color.gray3));
            }
        } else if (view == btn_screen) {
            btn_colligate.setTextColor(getResources().getColor(R.color.gray3));
            btn_colligate.setText("综合");
            SharedPreferences pref = BaseApplication.getInstance().getSharedPreferences("ISSELETCED", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("isSeletceNum", 0);
            editor.commit();
            Drawable drawable = getResources().getDrawable(R.drawable.down_deep_normal);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            btn_colligate.setCompoundDrawables(null, null, drawable, null);
            btn_salesvolume.setTextColor(getResources().getColor(R.color.gray3));
            text_price.setTextColor(getResources().getColor(R.color.gray3));
            img_up.setImageResource(R.drawable.up_deep_normal);
            img_down.setImageResource(R.drawable.down_deep_normal);
        }
    }

    private void openDropDownMenu() {
        btn_colligate.setTextColor(getResources().getColor(R.color.color_d2ba91));
        Drawable drawable = getResources().getDrawable(R.drawable.up_checked);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        btn_colligate.setCompoundDrawables(null, null, drawable, null);
        int[] location = new int[2];
        lin_top.getLocationInWindow(location);
        int aa = lin_top.getHeight();
        dropDownMenuPopWindow = new DropDownMenuPopWindow(context, location[1] + lin_top.getHeight());
        dropDownMenuPopWindow.setOnItemClick(new DropDownMenuPopWindow.OnItemClick() {
            @Override
            public void onItemClick(String info, int position) {
                checkInfo  = info;
                btn_colligate.setText(info);
                isDropDownMenu = !isDropDownMenu;
                sortby = position + 1;
                setInfo();
                closeDropDownMenu();
            }

            @Override
            public void onItemDismiss() {
                closeDropDownMenu();
            }
        });
        isDropDownMenu = !isDropDownMenu;
        dropDownMenuPopWindow.showAsDropDown(lin_top);
    }

    private void closeDropDownMenu() {
        dropDownMenuPopWindow.dismiss();
        isColligate = true;
        btn_colligate.setTextColor(getResources().getColor(R.color.color_d2ba91));
        Drawable drawable = getResources().getDrawable(R.drawable.down_checked);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        btn_colligate.setCompoundDrawables(null, null, drawable, null);
        isDropDownMenu = !isDropDownMenu;

    }
}
