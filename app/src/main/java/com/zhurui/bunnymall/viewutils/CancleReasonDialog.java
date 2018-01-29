package com.zhurui.bunnymall.viewutils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.mine.adapter.CancleReasonAdapter;
import com.zhurui.bunnymall.mine.bean.CancleReasonBaen;
import com.zhurui.bunnymall.mine.bean.CityModel;
import com.zhurui.bunnymall.mine.bean.DistrictModel;
import com.zhurui.bunnymall.mine.bean.LogisticsCompanyBean;
import com.zhurui.bunnymall.mine.bean.ProvinceModel;
import com.zhurui.bunnymall.utils.GetProvinceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaopf on 2017/7/26.
 */

public class CancleReasonDialog extends Dialog {

    private View view;
    private Button btn_sure;
    private RecyclerView recycler_canclereason;
    private List<DistrictModel> districtModels = null;
    private Context context;
    private CancleReasonAdapter cancleReasonAdapter;
    private int chooseposition = -1;
    private BtnOnClick btnOnClick;
    private boolean isCancleOrder;
    private boolean isDelivery;
    private  String[] reasonStr;
    private  List<LogisticsCompanyBean> list;

    public CancleReasonDialog(@NonNull Context context, @StyleRes int themeResId,boolean isCancleOrder,boolean isDelivery,List<LogisticsCompanyBean> list) {
        super(context, themeResId);
        this.context = context;
        this.isCancleOrder = isCancleOrder;
        this.list = list;
        this.isDelivery = isDelivery;
//        LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(context);
        view= inflater.inflate(R.layout.dialog_canclereason,null);
        btn_sure=(Button)view.findViewById(R.id.btn_sure);
        recycler_canclereason = (RecyclerView)view.findViewById(R.id.recycler_canclereason);
        initData();
        setView();
    }

    private void initData(){
        List<CancleReasonBaen> cancleReasonlist = new ArrayList();
        if(isDelivery){
            if(list != null && list.size() > 0){
                for(int i =0;i < list.size();i++){
                    CancleReasonBaen cancleReasonBaen = new CancleReasonBaen();
                    cancleReasonBaen.setReansonStr(list.get(i).getC_logisticsCompanyName());
                    cancleReasonBaen.setSelect(false);
                    cancleReasonBaen.setReansonId(list.get(i).getC_logisticsCompanyID());
                    cancleReasonlist.add(i,cancleReasonBaen);
                }
            }
        }else{
            if(isCancleOrder){
                reasonStr = new String[]{"裂帮、严重脱胶、帮底断线", "裂浆、裂面、真皮脱落", "裂跟、断跟、小跟面脱落","裂底、钉头突出、泛硝、塌芯"};
            }else{
                reasonStr = new String[]{"裂帮、严重脱胶、帮底断线", "裂浆、裂面、真皮脱落", "裂跟、断跟、小跟面脱落","裂底、钉头突出、泛硝、塌芯"};
            }
            for(int i =0;i<reasonStr.length;i++){
                CancleReasonBaen cancleReasonBaen = new CancleReasonBaen();
                cancleReasonBaen.setReansonStr(reasonStr[i]);
                cancleReasonBaen.setSelect(false);
                cancleReasonBaen.setReansonId(i + "");
                cancleReasonlist.add(i,cancleReasonBaen);
            }
        }

        cancleReasonAdapter = new CancleReasonAdapter(context,cancleReasonlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recycler_canclereason.setLayoutManager(linearLayoutManager);
        recycler_canclereason.setAdapter(cancleReasonAdapter);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOnClick.btnOnClick(cancleReasonAdapter.cancleReasonlist);
                dismiss();
            }
        });
    }

    private void setView(){
        view.setAlpha(1.0f);
        this.setContentView(view);
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.AnimBottom);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int)context.getResources().getDisplayMetrics().widthPixels; // 宽度
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

    public interface BtnOnClick{
        void btnOnClick(List<CancleReasonBaen> info);
    }

    public void setBtnOnClice(BtnOnClick btnOnClick){
        this.btnOnClick  = btnOnClick;

    }
}
