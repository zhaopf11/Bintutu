package com.zhurui.bunnymall.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.home.activity.NoticeWebViewActivity;
import com.zhurui.bunnymall.mine.bean.UserFootDataDetailBean;
import com.zhurui.bunnymall.viewutils.bean.CheckMaterialSortBean;
import com.zhurui.bunnymall.viewutils.bean.ProductAttrbuteSxstr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.xiaoneng.coreapi.ChatParamsBody;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.uiapi.OnMsgUrlClickListener;
import cn.xiaoneng.utils.CoreData;

/**
 * Created by zhaopf on 2017/10/11 0011.
 */

public class BintutuUtils {

    /**
     * 联系卖家
     *
     * @param productId
     * @param productName
     * @param productPrice
     * @param productImage
     * @param productXnSupplierID
     */
    public static void connectionSeller(Activity activity, String productId, String productName, String productPrice, String productImage, String productXnSupplierID) {
        String[] permissions = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        Ntalker.getExtendInstance().ntalkerSystem().requestPermissions(activity, permissions);
        String shareUrl = Contants.PRODUCT_SHARE_URL + productId;
        String headUrl = Contants.IMAGE_HEADURL + BaseApplication.getInstance().getUser().getMainImage();
        ChatParamsBody chatparams = new ChatParamsBody();
        // 咨询发起页（专有参数）
        chatparams.startPageTitle = productName;
        chatparams.startPageUrl = shareUrl;
        // erp参数
        chatparams.erpParam = "";
        // 此参数不传就默认在sdk外部打开，即在onClickUrlorEmailorNumber方法中打开
        chatparams.clickurltoshow_type = CoreData.CLICK_TO_SDK_EXPLORER;
        // 商品展示（专有参数）
        chatparams.itemparams.appgoodsinfo_type = CoreData.SHOW_GOODS_BY_WIDGET;
//        chatparams.itemparams.clientgoodsinfo_type = CoreData.SHOW_GOODS_BY_WIDGET;
//        chatparams.itemparams.clicktoshow_type = CoreData.CLICK_TO_SDK_EXPLORER;//小能内部sdk实现
        chatparams.itemparams.clicktoshow_type = CoreData.CLICK_TO_APP_COMPONENT;//自定义实现
        chatparams.itemparams.goods_image = Contants.BASE_IMGURL + productImage;
        chatparams.itemparams.goods_price = productPrice;
        chatparams.itemparams.goods_name = productName;
        chatparams.itemparams.goods_url = shareUrl;
        chatparams.itemparams.itemparam = "";
        chatparams.headurl = headUrl;  //必须以http:开头 访客头像设置
        //使用id方式，（SHOW_GOODS_BY_ID）
        chatparams.itemparams.goods_id = productId;//ntalker_test au_1000_9999
        chatparams.itemparams.clientgoodsinfo_type = CoreData.SHOW_GOODS_BY_ID;
        int startChat = Ntalker.getBaseInstance().startChat(BaseApplication.getInstance(), productXnSupplierID + "_9999", null, chatparams);
        if (0 == startChat) {
            Log.e("startChat", "打开聊窗成功");
        } else {
            Log.e("startChat", "打开聊窗失败，错误码:" + startChat);
        }
    }

    /**
     * 将list包含的数据从小到大排序
     *
     * @param list
     */
    public static void sortList(List<CheckMaterialSortBean> list) {
        Collections.sort(list, new Comparator<CheckMaterialSortBean>() {
            @Override
            public int compare(CheckMaterialSortBean checkMaterialSortBean1, CheckMaterialSortBean checkMaterialSortBean2) {
                Integer id1 = checkMaterialSortBean1.getSortId();
                Integer id2 = checkMaterialSortBean2.getSortId();
                return id1.compareTo(id2);
            }
        });
    }

    /**
     * 拼装选定材料属性以及设置样式
     *
     * @param context
     * @param producSxstr
     * @return
     */
    public static List<Object> assembleCheckMaterial(Context context, ProductAttrbuteSxstr producSxstr) {
        //需求要求下划线断开，富文本来写。下划线颜色随着字体的颜色改变，
        // 因此在该控件上方放置另一个控件，来实现字体和下划线颜色不一样
        List<Object> checkMaterialList = new ArrayList<>();
        String checkmaterial = "";
        SpannableStringBuilder materialStr = new SpannableStringBuilder("");
        SpannableStringBuilder materialStr1 = new SpannableStringBuilder("");
        SpannableStringBuilder materialStr2 = new SpannableStringBuilder("");

        if (producSxstr != null) {//定制属性
            if (producSxstr.getMianliaoList() != null && producSxstr.getMianliaoList().size() > 0) {//面料
                checkmaterial = checkmaterial + producSxstr.getMianliaoList().get(0).getName() + "  ";
                materialStr = new SpannableStringBuilder(producSxstr.getMianliaoList().get(0).getName() + "  ");
                materialStr.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_d2ba91)), 0, materialStr.length() - 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                materialStr.setSpan(new UnderlineSpan(), 0, materialStr.length() - 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            if (producSxstr.getDicaiList() != null && producSxstr.getDicaiList().size() > 0) {//底材
                checkmaterial = checkmaterial + producSxstr.getDicaiList().get(0).getName() + "  ";
                materialStr1 = new SpannableStringBuilder(producSxstr.getDicaiList().get(0).getName() + "  ");
                materialStr1.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_d2ba91)), 0, materialStr1.length() - 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                materialStr1.setSpan(new UnderlineSpan(), 0, materialStr1.length() - 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            if (producSxstr.getXiegenList() != null && producSxstr.getXiegenList().size() > 0) {//鞋跟
                checkmaterial = checkmaterial + producSxstr.getXiegenList().get(0).getName() + "  ";
                materialStr2 = new SpannableStringBuilder(producSxstr.getXiegenList().get(0).getName() + "  ");
                materialStr2.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_d2ba91)), 0, materialStr2.length() - 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                materialStr2.setSpan(new UnderlineSpan(), 0, materialStr2.length() - 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            materialStr.append(materialStr1);
            materialStr.append(materialStr2);
            checkMaterialList.add(checkmaterial);
            checkMaterialList.add(materialStr);
        }
        return checkMaterialList;
    }

    /**
     * 拼装个性要求以及设置样式
     *
     * @param context
     * @param producSxstr
     * @return
     */
    public static List<Object> assembleGexingRequire(Context context, ProductAttrbuteSxstr producSxstr) {
        List<Object> gexingRequireList = new ArrayList<>();
        String personalityrequest = "";
        SpannableStringBuilder gexingStr1 = new SpannableStringBuilder("");
        SpannableStringBuilder gexingStr2 = new SpannableStringBuilder("");

        if (producSxstr.getDipeiList() != null && producSxstr.getDipeiList().size() > 0) {//底配  底配为多选,循环取出选中的属性
            for (int i = 0; i < producSxstr.getDipeiList().size(); i++) {
                personalityrequest = personalityrequest + producSxstr.getDipeiList().get(i).getName() + "  ";
                SpannableStringBuilder sp = new SpannableStringBuilder(producSxstr.getDipeiList().get(i).getName() + "  ");
                sp.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_d2ba91)), 0, sp.length() - 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                sp.setSpan(new UnderlineSpan(), 0, sp.length() - 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                gexingStr1.append(sp);
            }
        }
        if (producSxstr.getGexingList() != null && producSxstr.getGexingList().size() > 0) {//个性
            personalityrequest = personalityrequest + producSxstr.getGexingList().get(0).getName() + "  ";
            gexingStr2 = new SpannableStringBuilder(producSxstr.getGexingList().get(0).getName() + "  ");
            gexingStr2.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_d2ba91)), 0, gexingStr2.length() - 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            gexingStr2.setSpan(new UnderlineSpan(), 0, gexingStr2.length() - 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        gexingStr1.append(gexingStr2);
        gexingRequireList.add(personalityrequest);
        gexingRequireList.add(gexingStr1);
        return gexingRequireList;
    }

    /**
     * 设置足型数据的样式
     *
     * @param context
     * @param producSxstr
     * @param footDataDetail
     * @return
     */
    public static List<Object> assembleFootData(Context context, ProductAttrbuteSxstr producSxstr, UserFootDataDetailBean footDataDetail) {
        List<Object> checkFootDataList = new ArrayList<>();
        String customizesize = "";
        SpannableStringBuilder chimaStr2 = new SpannableStringBuilder("");

        if (producSxstr.getChimaList() != null && producSxstr.getChimaList().size() > 0) {//尺码
            if (footDataDetail != null) {//选中的一条足型数据
                if (footDataDetail.getShoeSize().equals(producSxstr.getChimaList().get(0).getName())) {
                    //如果选中足型数据的尺码和标准尺码一致，显示根据谁谁的足型数据定制
                    customizesize = "根据  " + customizesize + footDataDetail.getName() + "的足型数据  ";
                } else {
                    //如果选中足型数据的尺码和标准尺码不一致，显示根据 标准尺码 45定制
                    customizesize = "根据  标准尺码";
                }
            }
            customizesize = customizesize + producSxstr.getChimaList().get(0).getName() + "码  定制  ";
            chimaStr2 = new SpannableStringBuilder(customizesize);
            chimaStr2.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_d2ba91)), 0, chimaStr2.length() - 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            chimaStr2.setSpan(new UnderlineSpan(), 0, chimaStr2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        checkFootDataList.add(customizesize);
        checkFootDataList.add(chimaStr2);

        return checkFootDataList;
    }

}
