package com.zhurui.bunnymall.mine.bean;

import com.zhurui.bunnymall.common.model.BaseRespMsg;

import java.util.List;

/**
 * Created by zhaopengfei on 2017/9/2 0002.
 */

public class MyFootListMessageBean extends BaseRespMsg {
    private String totalcount;
    private List<FootDataList> list;

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public List<FootDataList> getList() {
        return list;
    }

    public void setList(List<FootDataList> list) {
        this.list = list;
    }
}
