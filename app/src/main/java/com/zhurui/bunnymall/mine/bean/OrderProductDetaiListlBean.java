package com.zhurui.bunnymall.mine.bean;

import com.zhurui.bunnymall.common.model.BaseRespMsg;

import java.util.List;

/**
 * Created by zhaopf on 2017/9/7 0007.
 */

public class OrderProductDetaiListlBean extends BaseRespMsg{
    private List<OrderListBean> list;

    public List<OrderListBean> getList() {
        return list;
    }

    public void setList(List<OrderListBean> list) {
        this.list = list;
    }
}
