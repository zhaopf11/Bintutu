package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

import java.util.List;

/**
 * Created by zhoux on 2017/9/5.
 */

public class PreferentialObject extends BaseBean {

    private List<PreferentialBannerBean> bannerList;
    private List<PreferentialBean> groupPurchaseList;


    public List<PreferentialBannerBean> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<PreferentialBannerBean> bannerList) {
        this.bannerList = bannerList;
    }

    public List<PreferentialBean> getGroupPurchaseList() {
        return groupPurchaseList;
    }

    public void setGroupPurchaseList(List<PreferentialBean> groupPurchaseList) {
        this.groupPurchaseList = groupPurchaseList;
    }
}
