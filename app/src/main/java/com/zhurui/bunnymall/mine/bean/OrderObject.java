package com.zhurui.bunnymall.mine.bean;

import com.zhurui.bunnymall.common.model.BaseBean;

import java.util.List;

/**
 * Created by zhoux on 2017/9/4.
 */

public class OrderObject extends BaseBean {


    private List<OrderShopBean> shopOrderList;
    private List<OrderBean> orderProductList;

    public List<OrderShopBean> getShopOrderList() {
        return shopOrderList;
    }

    public void setShopOrderList(List<OrderShopBean> shopOrderList) {
        this.shopOrderList = shopOrderList;
    }

    public List<OrderBean> getOrderProductList() {
        return orderProductList;
    }

    public void setOrderProductList(List<OrderBean> orderProductList) {
        this.orderProductList = orderProductList;
    }
}
