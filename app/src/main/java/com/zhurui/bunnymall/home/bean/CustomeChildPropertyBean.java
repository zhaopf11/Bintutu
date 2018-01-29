package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/9/5.
 */

public class CustomeChildPropertyBean extends BaseBean {

//    private String sortIndex;
//    private String validFlag;
    private String name;
    private String customPropertiesID;
    private String customPropertiesValueID;
    private String customPropertiesValueImageUrl;
    private String isFootData;//非接口返回字段，根据需求逻辑自行添加，1 为足型数据属性，其他值为其他属性
    private String footSize;
    private boolean isCheck;

    public String getFootSize() {
        return footSize;
    }

    public void setFootSize(String footSize) {
        this.footSize = footSize;
    }

    public String getCustomPropertiesValueImageUrl() {
        return customPropertiesValueImageUrl;
    }

    public void setCustomPropertiesValueImageUrl(String customPropertiesValueImageUrl) {
        this.customPropertiesValueImageUrl = customPropertiesValueImageUrl;
    }

    public String getIsFootData() {
        return isFootData;
    }

    public void setIsFootData(String isFootData) {
        this.isFootData = isFootData;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

//    public void setSortIndex(String sortIndex) {
//        this.sortIndex = sortIndex;
//    }
//
//    public String getSortIndex() {
//        return sortIndex;
//    }
//
//    public void setValidFlag(String validFlag) {
//        this.validFlag = validFlag;
//    }
//
//    public String getValidFlag() {
//        return validFlag;
//    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCustomPropertiesID(String customPropertiesID) {
        this.customPropertiesID = customPropertiesID;
    }

    public String getCustomPropertiesID() {
        return customPropertiesID;
    }

    public void setCustomPropertiesValueID(String customPropertiesValueID) {
        this.customPropertiesValueID = customPropertiesValueID;
    }

    public String getCustomPropertiesValueID() {
        return customPropertiesValueID;
    }
}
