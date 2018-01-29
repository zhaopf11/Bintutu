package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

import java.util.List;

/**
 * Created by zhoux on 2017/8/29.
 */

public class StoreObject extends BaseBean{

    private List<SupplierDetailBean> supplierDetail;
    private List<StoreFollowCount> countNumber;

    private List<BannerInfo> topBannerList;
    private List<StoreProductBean> hostProduct;
    private List<StoreProductBean> blowProduct;

    public List<SupplierDetailBean> getSupplierDetail() {
        return supplierDetail;
    }

    public void setSupplierDetail(List<SupplierDetailBean> supplierDetail) {
        this.supplierDetail = supplierDetail;
    }

    public List<StoreFollowCount> getCountNumber() {
        return countNumber;
    }

    public void setCountNumber(List<StoreFollowCount> countNumber) {
        this.countNumber = countNumber;
    }

    public List<BannerInfo> getTopBannerList() {
        return topBannerList;
    }

    public void setTopBannerList(List<BannerInfo> topBannerList) {
        this.topBannerList = topBannerList;
    }

    public List<StoreProductBean> getHostProduct() {
        return hostProduct;
    }

    public void setHostProduct(List<StoreProductBean> hostProduct) {
        this.hostProduct = hostProduct;
    }

    public List<StoreProductBean> getBlowProduct() {
        return blowProduct;
    }

    public void setBlowProduct(List<StoreProductBean> blowProduct) {
        this.blowProduct = blowProduct;
    }
}
