package com.zhurui.bunnymall.mine.bean;

import java.util.List;

/**
 * Created by zhaopf on 2017/9/7 0007.
 */

public class OrderListBean {
    private List<OrderProductDetail> orderProductDetail;
    private List<OrderBean> orderProductList;

    public List<OrderProductDetail> getOrderProductDetail() {
        return orderProductDetail;
    }

    public void setOrderProductDetail(List<OrderProductDetail> orderProductDetail) {
        this.orderProductDetail = orderProductDetail;
    }

    public List<OrderBean> getOrderProductList() {
        return orderProductList;
    }

    public void setOrderProductList(List<OrderBean> orderProductList) {
        this.orderProductList = orderProductList;
    }
}
