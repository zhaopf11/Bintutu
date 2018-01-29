package com.zhurui.bunnymall.viewutils;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.home.adapter.ProductAttrbuteAdapter;
import com.zhurui.bunnymall.home.bean.ChimaListBean;
import com.zhurui.bunnymall.home.bean.ProListBean;
import com.zhurui.bunnymall.home.bean.ProductDetailBean;
import com.zhurui.bunnymall.home.msg.BuyTeamDetailResp;

import java.util.List;

/**
 * Created by zhaopf on 2017/10/25 0025.
 */

public class ProductAttributeDialog {
    public  Dialog dialog;
    private ListView custom_size_list;
    private ListView custom_cladding_list;
//    private ListView custom_color_list;
    private ListView custom_other_list;
    private TextView productId;
    private TextView product_name;
    private TextView custom_time;
    private TextView text_color;
    /**
     * @param context
     */
    public void showDialog(Context context,BuyTeamDetailResp teamDetailResp) {
        dialog = new Dialog(context, R.style.CustomDialog);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dv = inflater.inflate(R.layout.layout_product_attribute, null);
        custom_size_list = (ListView) dv.findViewById(R.id.custom_size_list);
        custom_cladding_list = (ListView) dv.findViewById(R.id.custom_cladding_list);
//        custom_color_list = (ListView) dv.findViewById(R.id.custom_color_list);
        custom_other_list = (ListView) dv.findViewById(R.id.custom_other_list);
        productId = (TextView) dv.findViewById(R.id.productId);
        product_name = (TextView) dv.findViewById(R.id.product_name);
        custom_time = (TextView) dv.findViewById(R.id.custom_time);
        text_color = (TextView) dv.findViewById(R.id.text_color);
        initData(context,teamDetailResp);
        dialog.setContentView(dv);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.show();
        //设置全屏显示dialog
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.FILL_PARENT;
        window.setAttributes(lp);
    }

    private void initData(Context context,BuyTeamDetailResp teamDetailResp) {
//        String str = "定制周期<font color='#d2ba91'>  出品</font>个工作日";
//        custom_time.setTextSize(14);
//        custom_time.setText(Html.fromHtml(str));
        if (teamDetailResp != null && null != teamDetailResp.getList().get(0).getProductDetail() && teamDetailResp.getList().get(0).getProductDetail().size() > 0) {
            ProductDetailBean productDetailBean = teamDetailResp.getList().get(0).getProductDetail().get(0);
            if(productDetailBean != null){
                product_name.setText("（"+ productDetailBean.getName() + "）");
                productId.setText(productDetailBean.getProductID());
                custom_time.setText(productDetailBean.getBrandName());
            }
        }

        if (null != teamDetailResp && null != teamDetailResp.getList().get(0).getSxstr()) {
            List<ChimaListBean> chimaList = teamDetailResp.getList().get(0).getSxstr().getChimaList();
            List<ChimaListBean> yanseList = teamDetailResp.getList().get(0).getSxstr().getYanseList();
            List<ChimaListBean> mianliaoList = teamDetailResp.getList().get(0).getSxstr().getMianliaoList();
            List<ProListBean> proList = teamDetailResp.getList().get(0).getSxstr().getProList();

            text_color.setText(yanseList.size() + "种配色");
            ProductAttrbuteAdapter adapter = new ProductAttrbuteAdapter(context,"1",chimaList,null);
            custom_size_list.setAdapter(adapter);

//            ProductAttrbuteAdapter yanAdapter = new ProductAttrbuteAdapter(context,"1",yanseList,null);
//            custom_color_list.setAdapter(yanAdapter);

            ProductAttrbuteAdapter mianAdapter = new ProductAttrbuteAdapter(context,"1",mianliaoList,null);
            custom_cladding_list.setAdapter(mianAdapter);

            ProductAttrbuteAdapter proadapter = new ProductAttrbuteAdapter(context,"2",null,proList);
            custom_other_list.setAdapter(proadapter);

        }
    }

    /**
     * 关闭dialog
     */
    public void dismiss(){
        if(dialog !=null){
            dialog.dismiss();
        }
    }
}
