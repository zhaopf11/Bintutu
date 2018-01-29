package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseRespMsg;

import java.util.List;

/**
 * Created by zhaopf on 2017/9/6 0006.
 */

public class MessageListBean extends BaseRespMsg {
    private List<MessageBean> list;

    public List<MessageBean> getList() {
        return list;
    }

    public void setList(List<MessageBean> list) {
        this.list = list;
    }
}
