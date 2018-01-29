package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseRespMsg;

import java.util.List;

/**
 * Created by zhaopf on 2017/9/6 0006.
 */

public class LogisticsListMessageBean extends BaseRespMsg {
    private String totalcount;
    private List<LogisticsMessageBean> list;

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public List<LogisticsMessageBean> getList() {
        return list;
    }

    public void setList(List<LogisticsMessageBean> list) {
        this.list = list;
    }
}
