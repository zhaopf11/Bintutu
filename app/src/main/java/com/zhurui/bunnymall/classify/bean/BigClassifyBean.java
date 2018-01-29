package com.zhurui.bunnymall.classify.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/8/18.
 */

public class BigClassifyBean extends BaseBean {

    private long smallTypeID;
    private long bigTypeID;
    private String name;
    private int validFlag;
    private String sortIndex;
    private String isShowFlag;
    private String seo;
    private String supplierID;
    private String smallTypeImage;

    public String getSmallTypeImage() {
        return smallTypeImage;
    }

    public void setSmallTypeImage(String smallTypeImage) {
        this.smallTypeImage = smallTypeImage;
    }

    public long getSmallTypeID() {
        return smallTypeID;
    }

    public void setSmallTypeID(long smallTypeID) {
        this.smallTypeID = smallTypeID;
    }

    public long getBigTypeID() {
        return bigTypeID;
    }

    public void setBigTypeID(long bigTypeID) {
        this.bigTypeID = bigTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(int validFlag) {
        this.validFlag = validFlag;
    }

    public String getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(String sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getIsShowFlag() {
        return isShowFlag;
    }

    public void setIsShowFlag(String isShowFlag) {
        this.isShowFlag = isShowFlag;
    }

    public String getSeo() {
        return seo;
    }

    public void setSeo(String seo) {
        this.seo = seo;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }
}
