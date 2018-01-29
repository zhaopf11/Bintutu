package com.zhurui.bunnymall.mine.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/9/1.
 */

public class BalanceDetailBean extends BaseBean {

    private String c_userMoneyHistoryTypeName;
    private String userMoneyHistoryID;
    private String userID;
    private String relatedID;
    private String userMoneyHistoryTypeID;
    private String money;
    private String info;
    private String addTime;
    private String shopID;

    public void setC_userMoneyHistoryTypeName(String c_userMoneyHistoryTypeName) {
        this.c_userMoneyHistoryTypeName = c_userMoneyHistoryTypeName;
    }

    public String getC_userMoneyHistoryTypeName() {
        return c_userMoneyHistoryTypeName;
    }

    public void setUserMoneyHistoryID(String userMoneyHistoryID) {
        this.userMoneyHistoryID = userMoneyHistoryID;
    }

    public String getUserMoneyHistoryID() {
        return userMoneyHistoryID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setRelatedID(String relatedID) {
        this.relatedID = relatedID;
    }

    public String getRelatedID() {
        return relatedID;
    }

    public void setUserMoneyHistoryTypeID(String userMoneyHistoryTypeID) {
        this.userMoneyHistoryTypeID = userMoneyHistoryTypeID;
    }

    public String getUserMoneyHistoryTypeID() {
        return userMoneyHistoryTypeID;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMoney() {
        return money;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getShopID() {
        return shopID;
    }
}
