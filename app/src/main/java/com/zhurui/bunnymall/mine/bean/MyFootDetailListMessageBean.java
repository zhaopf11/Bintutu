package com.zhurui.bunnymall.mine.bean;

import com.zhurui.bunnymall.common.model.BaseRespMsg;

import java.util.List;

/**
 * Created by zhaopengfei on 2017/9/2 0002.
 */

public class MyFootDetailListMessageBean extends BaseRespMsg {
    private List<UserFootDataBean> list;

    public List<UserFootDataBean> getList() {
        return list;
    }

    public void setList(List<UserFootDataBean> list) {
        this.list = list;
    }
}
