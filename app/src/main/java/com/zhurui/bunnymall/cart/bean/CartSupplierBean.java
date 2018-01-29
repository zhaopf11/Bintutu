package com.zhurui.bunnymall.cart.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

/**
 * Created by zhoux on 2017/9/2.
 */

public class CartSupplierBean extends BaseBean {
    private String suppliername;
    private String supplierid;
    private String xnsupplierId;

    public String getXnsupplierId() {
        return xnsupplierId;
    }

    public void setXnsupplierId(String xnsupplierId) {
        this.xnsupplierId = xnsupplierId;
    }

    public String getSuppliername() {
        return suppliername;
    }

    public void setSuppliername(String suppliername) {
        this.suppliername = suppliername;
    }

    public String getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(String supplierid) {
        this.supplierid = supplierid;
    }
}
