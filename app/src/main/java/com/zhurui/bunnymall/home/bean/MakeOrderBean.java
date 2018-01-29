package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/8/31.
 */

public class MakeOrderBean extends BaseBean {
    private String transactionid;
    private String usermoney;
    private String shoporderid;
    private String needpaymoney;

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

    public String getShoporderid() {
        return shoporderid;
    }

    public void setShoporderid(String shoporderid) {
        this.shoporderid = shoporderid;
    }

    public String getNeedpaymoney() {
        return needpaymoney;
    }

    public void setNeedpaymoney(String needpaymoney) {
        this.needpaymoney = needpaymoney;
    }
}
