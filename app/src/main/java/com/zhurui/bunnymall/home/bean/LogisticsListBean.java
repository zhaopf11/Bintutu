package com.zhurui.bunnymall.home.bean;

import com.zhurui.bunnymall.common.model.BaseRespMsg;

import java.util.List;

/**
 * Created by zhaopf on 2017/9/20 0020.
 */

public class LogisticsListBean extends BaseRespMsg {
    private String totalcount;
    private List<LogisticsBean> list;

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public List<LogisticsBean> getList() {
        return list;
    }

    public void setList(List<LogisticsBean> list) {
        this.list = list;
    }
}
