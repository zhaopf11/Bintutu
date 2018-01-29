package com.zhurui.bunnymall.cart.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.cart.bean.CartProductBean;
import com.zhurui.bunnymall.cart.bean.CartSupplierBean;

import java.util.List;

/**
 * Created by zhoux on 2017/9/2.
 */

public class CartRespMsg extends BaseRespMsg {

    private String totalcount;
    private List<CartProductBean> list;
    private List<CartSupplierBean> supplierlist;

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public List<CartProductBean> getList() {
        return list;
    }

    public void setList(List<CartProductBean> list) {
        this.list = list;
    }

    public List<CartSupplierBean> getSupplierlist() {
        return supplierlist;
    }

    public void setSupplierlist(List<CartSupplierBean> supplierlist) {
        this.supplierlist = supplierlist;
    }
}
