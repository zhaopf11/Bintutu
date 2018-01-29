package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseRespMsg;

import java.util.List;

/**
 * Created by zhaopf on 2017/9/6 0006.
 */

public class NoticeListMessageBean extends BaseRespMsg {
    private String totalcount;
    private List<NoticeMessageBean> list;

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public List<NoticeMessageBean> getList() {
        return list;
    }

    public void setList(List<NoticeMessageBean> list) {
        this.list = list;
    }
}
