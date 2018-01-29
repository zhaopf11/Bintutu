package com.zhurui.bunnymall.home.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.home.bean.PreferentialObject;

import java.util.List;

/**
 * Created by zhoux on 2017/9/5.
 */

public class PreferentialRespMsg extends BaseRespMsg {

    private String totalcount;
    private List<PreferentialObject> list;

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public List<PreferentialObject> getList() {
        return list;
    }

    public void setList(List<PreferentialObject> list) {
        this.list = list;
    }
}
