package com.zhurui.bunnymall.mine.msg;

import com.zhurui.bunnymall.common.model.BaseRespMsg;
import com.zhurui.bunnymall.mine.bean.PointDetailBean;

import java.util.List;

/**
 * Created by zhoux on 2017/9/1.
 */

public class PointDetailRespMsg extends BaseRespMsg {
    private String totalcount;
    private List<PointDetailBean> list;


    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public List<PointDetailBean> getList() {
        return list;
    }

    public void setList(List<PointDetailBean> list) {
        this.list = list;
    }
}
