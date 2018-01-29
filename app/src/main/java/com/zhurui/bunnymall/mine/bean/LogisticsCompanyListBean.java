package com.zhurui.bunnymall.mine.bean;

import com.zhurui.bunnymall.common.model.BaseRespMsg;

import java.util.List;

/**
 * Created by zhaopf on 2017/9/17 0017.
 */

public class LogisticsCompanyListBean extends BaseRespMsg{

    private List<LogisticsCompanyBean> list;

    public List<LogisticsCompanyBean> getList() {
        return list;
    }

    public void setList(List<LogisticsCompanyBean> list) {
        this.list = list;
    }
}
