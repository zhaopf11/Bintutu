package com.zhurui.bunnymall.mine.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/9/4.
 */

public class OrderShopBean extends BaseBean {
    private String shopOrderID;
    private String itemCount;
    private String autoCloseTimeMills;
    private String needPayMoney;
    private String status;
    private String xnSupplierID;
    private String transactionid;
    private String usermoney;
    private String shopOrderTypeID;
    private String totalPrice;
    private String commentFlag;

    public String getCommentFlag() {
        return commentFlag;
    }

    public void setCommentFlag(String commentFlag) {
        this.commentFlag = commentFlag;
    }

    public String getShopOrderTypeID() {
        return shopOrderTypeID;
    }

    public void setShopOrderTypeID(String shopOrderTypeID) {
        this.shopOrderTypeID = shopOrderTypeID;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getUsermoney() {
        return usermoney;
    }

    public void setUsermoney(String usermoney) {
        this.usermoney = usermoney;
    }

    public void setShopOrderID(String shopOrderID) {
        this.shopOrderID = shopOrderID;
    }

    public String getShopOrderID() {
        return shopOrderID;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setAutoCloseTimeMills(String autoCloseTimeMills) {
        this.autoCloseTimeMills = autoCloseTimeMills;
    }

    public String getAutoCloseTimeMills() {
        return autoCloseTimeMills;
    }

    public void setNeedPayMoney(String needPayMoney) {
        this.needPayMoney = needPayMoney;
    }

    public String getNeedPayMoney() {
        return needPayMoney;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setXnSupplierID(String xnSupplierID) {
        this.xnSupplierID = xnSupplierID;
    }

    public String getXnSupplierID() {
        return xnSupplierID;
    }
}
